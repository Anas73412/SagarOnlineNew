package com.ecom.sagaronline.Config;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;


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

    public void updateIntent(Context context){
        Intent i=new Intent("Cart");
        i.putExtra("type","update");
        context.sendBroadcast(i);
    }
}
