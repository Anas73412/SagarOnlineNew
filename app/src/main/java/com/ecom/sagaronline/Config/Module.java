package com.ecom.sagaronline.Config;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Model.GetCongifDataModel;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.OnDialogItemClickListener;
import com.ecom.sagaronline.Utils.OnGetConfigData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.sagaronline.Config.BaseURL.GET_VERSION_URL;


public class Module {

    Context context;

    public Module(Context context) {
        this.context = context;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public String VolleyErrorMessage(VolleyError error)
    {
        String str_error ="";
        if (error instanceof TimeoutError) {
            str_error="Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error="Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof NetworkError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error="An Unknown error occur";
        }else if(error instanceof NoConnectionError){
            str_error="No Internet Connection";
        }

        return str_error;
    }

    public void preventMultipleClick(final View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 1000);
    }

    public void getCongifData(OnGetConfigData onGetConfigData)
    {
        HashMap<String, String> param = new HashMap<String, String>();

        CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, GET_VERSION_URL, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("getConfigData",response.toString());
                    Boolean resp=response.getBoolean("responce");
                    if (resp)
                    {
                        JSONObject jsonObject=response.getJSONObject("data");
                        String id = jsonObject.getString("id");
                        String data =jsonObject.getString("data");
                        String app_version = jsonObject.getString("app_version");
                        String msg_status=jsonObject.getString("msg_status");
                        String whatsapp_no=jsonObject.getString("whatsapp_no");
                        String call_no=jsonObject.getString("call_no");
                        String note=jsonObject.getString("note");

                        GetCongifDataModel getCongifDataModel= new GetCongifDataModel();

                        ArrayList<GetCongifDataModel> list = new ArrayList<>();
                        getCongifDataModel.setId(id);
                        getCongifDataModel.setData(data);
                        getCongifDataModel.setMsg_status(msg_status);
                        getCongifDataModel.setCall_no(call_no);
                        getCongifDataModel.setApp_version(app_version);
                        getCongifDataModel.setWhatsapp_no(whatsapp_no);
                        getCongifDataModel.setNote(note);
                        Log.e("sASa", "onResponse: "+getCongifDataModel.getWhatsapp_no() );
                       onGetConfigData.onGetConfigData(getCongifDataModel);
//                        list.add(getCongifDataModel);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }

    /*public void addToCart(Context context, String cart_id, String product_id, String product_image, String cat_id, String product_name, String price, String unit_price, String unit, String mrp, String stock, String type, float qty)
    {
        DatabaseCartHandler db_cart=new DatabaseCartHandler(context);
        HashMap<String, String> mapProduct = new HashMap<String, String>();
        mapProduct.put("cart_id",cart_id);
        mapProduct.put("product_id", product_id);
        mapProduct.put("product_image", product_image);
        mapProduct.put("cat_id", cat_id);
        mapProduct.put("product_name", product_name);
        mapProduct.put("price", price);
        mapProduct.put("unit_price", unit_price);
        mapProduct.put("unit", unit);
        mapProduct.put("mrp", mrp);
        mapProduct.put("stock", stock);
        mapProduct.put("type", type);
        try {

            boolean tr = db_cart.setCart(mapProduct, qty);
            if (tr == true) {
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            } else if (tr == false) {
                Toast.makeText(context, "Cart Updated", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    public void addToWishlist(Context context , String product_id , String product_images , String cat_id , String product_name , String price,
                              String product_desc , String rewards , String unit_value , String unit , String increment , String stock , String in_stock,
                              String title , String mrp, String product_attribute, String user_id, String hndName)

    {
       WishlistHandler db_wish = new WishlistHandler( context );
        HashMap<String, String> mapProduct = new HashMap<String, String>();
        mapProduct.put( "product_id",product_id );
        mapProduct.put( "product_images",product_images );
        mapProduct.put( "cat_id", cat_id );
        mapProduct.put( "product_name",product_name );
        mapProduct.put( "price",price );
        mapProduct.put( "product_description",product_desc );
        mapProduct.put( "rewards",rewards);
        mapProduct.put("user_id",user_id);
        mapProduct.put( "unit_value",unit_value );
        mapProduct.put( "unit", unit );
        mapProduct.put( "increment",increment );
        mapProduct.put( "stock",stock );
        mapProduct.put( "title",title);
        mapProduct.put( "mrp", mrp );
        mapProduct.put( "product_attribute",product_attribute );
        mapProduct.put( "in_stock", in_stock );
        mapProduct.put( "product_hindi_name", hndName );

        try {

            boolean tr =db_wish.setwishTable(mapProduct);
            if (tr == true) {

                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                Toast.makeText(context, "Added to Wishlist" , Toast.LENGTH_SHORT).show();
                updatewishintent();



            }
            else
            {
                Toast.makeText(context, "Something Went Wrong" , Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //  Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }*/

    public void showToast(String s)
    {
        Toast.makeText(context,""+s, Toast.LENGTH_SHORT).show();
    }

    public void updatewishintent() {
        Intent updates = new Intent("Grocery_wish");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
    }
    public String checkNull(String str){
        String s="";
        if(str==null || str.isEmpty() || str.equalsIgnoreCase("null")){
            s="";
        }else {
            s=str;
        }
        return s;
    }
    public boolean checkNullCondition(String str){
        boolean s=false;
        if(str==null || str.isEmpty()|| str.equalsIgnoreCase("null")){
            s=true;
        }else {
            s=false;
        }
        return s;
    }

    public void updateIntent(Context context){
        Intent i=new Intent("Cart");
        i.putExtra("type","update");
        context.sendBroadcast(i);
    }

    public void postRequest(String url,HashMap<String,String> params,Response.Listener<String> listener,Response.ErrorListener errorListener){
        if(!ConnectivityReceiver.isConnected()){
            showToast("No Internet Connection");
            return;
        }
        Log.e("url", ""+url );
        StringRequest request=new StringRequest(Request.Method.POST,url,listener,errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("params", ""+params );
                return params;
            }
        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(mRetryPolicy);
        AppController.getInstance().addToRequestQueue(request,"req");
    }
    public void showFilterDialog(OnDialogItemClickListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_filters);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView btn_cancel = dialog.findViewById(R.id.tv_cancel);
        LinearLayout ln_sort=dialog.findViewById(R.id.ln_sort);
        LinearLayout ln_filters=dialog.findViewById(R.id.ln_filters);
        dialog.show();

        ln_filters.setOnClickListener(view -> {

            dialog.dismiss();
            listener.onItemClick("filter");
        });

        ln_sort.setOnClickListener(view -> {
            dialog.dismiss();
            listener.onItemClick("sort");
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public int checkNullNumber(String str){
        if(str==null || str.isEmpty() || str.equalsIgnoreCase("null")){
            return 0;
        }else{
            return Integer.parseInt(str);
        }
    }
}
