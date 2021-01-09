package com.ecom.sagaronline.Activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Model.NewProductModel;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.Session_management;
import com.ecom.sagaronline.Utils.WishlistHandler;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ecom.sagaronline.Config.BaseURL.GET_PRODUCT_DETAIL_URL;
import static com.ecom.sagaronline.Config.BaseURL.IMG_PRODUCT_URL;
import static com.ecom.sagaronline.Config.BaseURL.KEY_ID;

public class NewDetailFragment extends Fragment {
    TextView tv_details_product_price,tv_details_product_mrp,tv_details_product_off,tv_details_product_description,tv_descriptionTitle,tv_details_product_name;
    Button btn_add,btn_buy_now,btn_checkout;
    TextView dialog_unit_type;
    NumberPicker product_qty;
    ArrayList<NewProductModel> list;
    RelativeLayout rel_out,lin_img;
    ImageView wish_before,wish_after;
    CarouselView img_slider;
    WishlistHandler db_wish ;
    public static ArrayList<String> image_list;
    NewProductModel newProductModel;
    RecyclerView top_selling_recycler;
    String atr_price,atr_mrp;
    String user_id;
    String attribute_value,attribute_name;
    Session_management session_management;
    String product_id="1";
    String in_stock,stock,stock_value;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        db_wish=new WishlistHandler( getActivity() );
        session_management = new Session_management(getContext());
        user_id=session_management.getUserDetails().get(KEY_ID);
        tv_details_product_price=view.findViewById(R.id.details_product_price);
        tv_details_product_mrp=view.findViewById(R.id.details_product_mrp);
        tv_details_product_mrp.setPaintFlags(tv_details_product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv_details_product_off=view.findViewById(R.id.details_product_per);
        tv_details_product_description=view.findViewById(R.id.details_product_description);
        tv_descriptionTitle=view.findViewById(R.id.descriptionTitle);
        tv_details_product_name=view.findViewById(R.id.details_product_name);
        wish_before=view.findViewById(R.id.wish_before);
        wish_after=view.findViewById(R.id.wish_after);
        rel_out= view.findViewById( R.id.rel_out );
        lin_img = view.findViewById(R.id.relative_layout_img);
        img_slider = view.findViewById(R.id.img_slider);
        top_selling_recycler = view.findViewById(R.id.top_selling_recycler);
        dialog_unit_type=(TextView)view.findViewById(R.id.unit_type);


        btn_checkout = view.findViewById(R.id.btn_f_Add_to_cart);
        btn_add=view.findViewById(R.id.btn_add);
        btn_buy_now=view.findViewById(R.id.btn_buy_now);
        product_qty=view.findViewById(R.id.product_qty);


        image_list=new ArrayList<String>();
        list=new ArrayList<>();
        productDetail(product_id);
        Bundle bundle=getArguments();
       // product_id=bundle.getString("product_id");


        wish_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wish_after.setVisibility( View.VISIBLE );
//                wish_before.setVisibility( View.INVISIBLE );

                if (session_management.isLoggedIn()) {
                    int stck = Integer.parseInt(stock);

//                    if (stck < 1 || in_stock.equals("0")) {
                   // wish_after.setVisibility(View.GONE);
//                        Toast.makeText( getActivity(), "Out Of Stock", Toast.LENGTH_LONG ).show();
//                    }
//                    else {
                        wish_after.setVisibility( View.VISIBLE );
                        wish_before.setVisibility( View.INVISIBLE );
                        HashMap<String, String> mapProduct = new HashMap<String, String>();

                        mapProduct.put( "product_id",product_id );
                        mapProduct.put( "product_images",list.get(0).getProduct_image());
                        mapProduct.put( "cat_id", list.get(0).getCategory_id());
                        mapProduct.put( "product_name",list.get(0).getProduct_name());
                        mapProduct.put( "price",list.get(0).getPrice());
                        mapProduct.put( "product_description",list.get(0).getProduct_description_arb());
                        mapProduct.put( "rewards",list.get(0).getRewards());
                        mapProduct.put("user_id",user_id.toString());
                        mapProduct.put( "unit_value", list.get(0).getUnit_value());
                        mapProduct.put( "unit", list.get(0).getUnit());
                        mapProduct.put( "increment",list.get(0).getIncreament());
                        mapProduct.put( "stock",list.get(0).getStock());
                        mapProduct.put( "title", list.get(0).getTitle());
                        mapProduct.put( "mrp", list.get(0).getMrp());
                        mapProduct.put( "product_attribute",list.get(0).getProduct_attribute());
                        mapProduct.put( "in_stock", list.get(0).getIn_stock());
                        try {

                            boolean tr = db_wish.setwishTable( mapProduct );
                            if (tr == true) {

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText( getActivity(), "Added to Wishlist", Toast.LENGTH_LONG ).show();
                                Log.e("check_size",mapProduct.toString()+"  "+list.size());


                            } else {
                                Toast.makeText( getActivity(), "Something Went Wrong" + db_wish.getWishtableCount( user_id ), Toast.LENGTH_LONG ).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
//                    }
                }
                else
                {
                    Intent intent=new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }

            }
        });
        wish_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wish_after.setVisibility( View.INVISIBLE );
                wish_before.setVisibility( View.VISIBLE );
                db_wish.removeItemFromWishtable(product_id,user_id);
                Toast.makeText(getActivity(), "removed from Wishlist" +db_wish.getWishtableCount(user_id), Toast.LENGTH_LONG).show();
            }
        });
       btn_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               btn_add.setText("Update Cart");
               Toast.makeText(getContext(),"Cart Updated!",Toast.LENGTH_SHORT).show();
           }
       });


        return view;
    }

    public void productDetail(String productId)
    {
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("pid","539");

        CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, GET_PRODUCT_DETAIL_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e("productDetail",params+response.toString());
                    Boolean reps = response.getBoolean("responce");
                    if (reps)
                    {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            NewProductModel model = new NewProductModel();

                             model.setPrice(object.getString("price"));
                             model.setProduct_id(object.getString("product_id"));
                             model.setUnit_value(object.getString("unit_value"));
                            model.setProduct_name(object.getString("product_name"));
                            model.setCategory_id(object.getString("category_id"));
                           // model.setProduct_description(object.getString("product_description"));
                          //  model.setProduct(object.getString("product_name_hindi"));
                            model.setProduct_name_arb(object.getString("product_name_arb"));
                            model.setProduct_description_arb(object.getString("product_description_arb"));
                            model.setProduct_description(object.getString("product_description"));
                            model.setDeal_price(object.getString("deal_price"));
                            model.setStart_date(object.getString("start_date"));
                            model.setStart_time(object.getString("start_time"));
                            model.setEnd_date(object.getString("end_date"));
                            model.setEnd_time(object.getString("end_time"));
                            model.setProduct_attribute(object.getString("product_attribute"));
                            model.setStatus(object.getString("status"));
                            model.setIn_stock(object.getString("in_stock"));
                            model.setUnit(object.getString("unit"));
                            model.setIncreament(object.getString("increament"));
                            model.setRewards(object.getString("rewards"));
                            model.setStock(object.getString("stock"));
                            model.setSize(object.getString("size"));
                            model.setColor(object.getString("color"));
                            model.setTitle(object.getString("title"));
                            model.setMrp(object.getString("mrp"));
                            model.setProduct_image(object.getString("product_image"));
                            list.add(model);

                            stock=object.getString("stock");
                            in_stock=object.getString("in_stock");

                            tv_details_product_name.setText(list.get(i).getProduct_name());
                            tv_details_product_description.setText(list.get(i).getProduct_description());
                            tv_details_product_price.setText(list.get(i).getPrice());
                            tv_details_product_mrp.setText(list.get(i).getMrp());


                            atr_price=object.getString("price");
                            atr_mrp=object.getString("mrp");

                            int atr_dis=getDiscount(atr_price,atr_mrp);
                          //  tv_details_product_off.setText(String.valueOf(atr_dis));
                            if(atr_dis<=0)
                            {
                                tv_details_product_off.setVisibility(View.GONE);
                            }
                            else
                            {
                                tv_details_product_off.setVisibility(View.VISIBLE);
                                tv_details_product_off.setText(""+String.valueOf(atr_dis)+"%"+" OFF");
                            }

                            String string = object.getString("product_attribute");
                            JSONArray arr = new JSONArray(string);
                            JSONObject obj = arr.getJSONObject(i);
                            String attribute_value=obj.getString("attribute_value");
                            String attribute_name=obj.getString("attribute_name");
                            String attribute_mrp=obj.getString("attribute_mrp");
                            String attribute_size=obj.getString("attribute_size");
                            String attribute_color=obj.getString("attribute_color");
                            stock_value=obj.getString("stock_value");
                            String status=obj.getString("status");

                            dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);

                            String str = object.getString("product_image");
                            JSONArray array = new JSONArray(str);

//
//                        if(product_image.equals(null))
//                        {
//                            Toast.makeText(getActivity(),"There is no image for this product",Toast.LENGTH_LONG).show();
//                        }
//                        else
//                        {
                            for(int j=0; j<=array.length()-1;j++)
                            {
                                HashMap<String, String> img_map= new HashMap<>();

                                image_list.add(array.get(j).toString());

                            }

                            img_slider.setImageListener(new ImageListener() {
                                @Override
                                public void setImageForPosition(int position, ImageView imageView) {
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
                                    imageView.setLayoutParams(layoutParams);
                                    Glide.with(getActivity())
                                            .load(IMG_PRODUCT_URL + image_list.get(position))
                                            .thumbnail(0.9f)
                                            .centerCrop()
                                            .into(imageView);
                                }
                            });
                            img_slider.setPageCount(image_list.size());
                            Log.e("image", String.valueOf(image_list.size()));


//                        }

//                            String msg = response.getString("message");
//                            Toast.makeText(getContext(),"slx,km "+msg,Toast.LENGTH_SHORT).show();

                        }
                        Log.e("listSize_detail", String.valueOf(list.size()));

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
    public int getDiscount(String price, String mrp)
    {
        double mrp_d=Double.parseDouble(mrp);
        double price_d=Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df=Math.round(per);
        int d=(int)df;
        return d;
    }

}
