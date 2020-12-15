package com.example.sagaronlineyash.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Fragments.DetailsFragment;
import com.example.sagaronlineyash.Fragments.WishlistFragment;
import com.example.sagaronlineyash.Model.ProductVariantModel;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.DatabaseCartHandler;
import com.example.sagaronlineyash.Utils.Session_management;
import com.example.sagaronlineyash.Utils.WishlistHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;
import static com.example.sagaronlineyash.Config.BaseURL.KEY_ID;

public class Wishlist_Adapter extends RecyclerView.Adapter<Wishlist_Adapter.WishHolder> {


    String atr_id="";
    String atr_product_id="";
    String attribute_name="";
    String attribute_value="";
    String attribute_mrp="";

    ArrayList<ProductVariantModel> variantList;
    ArrayList<ProductVariantModel> attributeList;
    ProductVariantAdapter productVariantAdapter;
    ArrayList<HashMap<String, String>> list;
    Activity activity;
    int status=0;

    SharedPreferences preferences;
    String language;
    float qty = 1;
    DatabaseCartHandler db_cart;
    Session_management sessionManagement ;
    String user_id ;
    public Wishlist_Adapter(ArrayList<HashMap<String, String>> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        db_cart = new DatabaseCartHandler(activity);
    }



    @Override
    public WishHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate( R.layout.row_wish, parent, false);
        return new Wishlist_Adapter.WishHolder( view );
    }

    @Override
    public void onBindViewHolder(final WishHolder holder, final int position) {
//        final Product_model mList = modelList.get(position);
        final HashMap<String, String> map = list.get(position);

        String img_array=map.get("product_images");
        String img_name = null;
        try {
            JSONArray array=new JSONArray(img_array);
            img_name=array.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sessionManagement = new Session_management( activity );
        user_id=sessionManagement.getUserDetails().get(KEY_ID);

        final float stock = Float.parseFloat( map.get("stock") );
//        holder.elegantNumberButton.setRange( 0, (int) (stock+1) );
        Glide.with(activity)
                .load( BaseURL.IMG_PRODUCT_URL + img_name)
                .centerCrop()
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy( DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_icon);
        preferences = activity.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        holder.product_name.setText( map.get( "product_name" ));

        int stk= Integer.parseInt(map.get("stock"));

        if(stk<=0)
        {
            holder.rel_out.setVisibility( View.VISIBLE);

        }
        else {
            holder.rel_out.setVisibility( View.GONE );
        }





        String atr= String.valueOf(map.get("product_attribute"));

        if(atr.equals("[]"))
        {


            status=1;
            String p= String.valueOf(map.get("price"));
            String m= String.valueOf(map.get("mrp"));
            holder.product_price.setText(activity.getResources().getString(R.string.currency)+ map.get("price"));
            holder.product_mrp.setText(activity.getResources().getString(R.string.currency)+map.get("mrp")+"/"+map.get("unit_value")+" "+map.get("unit"));

            //Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
        }

        else
        {
            status=2;
            attributeList.clear();
//            String atr=String.valueOf(mList.getProduct_attribute());
            JSONArray jsonArr = null;
            try {

                jsonArr = new JSONArray(atr);
                for (int i = 0; i < jsonArr.length(); i++)
                {
                    ProductVariantModel model=new ProductVariantModel();
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    String atrid=jsonObj.getString("id");
                    String atrproductid=jsonObj.getString("product_id");
                    String attributename=jsonObj.getString("attribute_name");
                    String attributevalue=jsonObj.getString("attribute_value");
                    String attributemrp=jsonObj.getString("attribute_mrp");

                    model.setId(atrid);
                    model.setProduct_id(atrproductid);
                    model.setAttribute_value(attributevalue);
                    model.setAttribute_name(attributename);
                    model.setAttribute_mrp(attributemrp);

                    attributeList.add(model);

                    //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                    //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            try
            {




                atr_id=attributeList.get(0).getId();
                atr_product_id=attributeList.get(0).getProduct_id();
                attribute_name=attributeList.get(0).getAttribute_name();
                attribute_value=attributeList.get(0).getAttribute_value();
                attribute_mrp=attributeList.get(0).getAttribute_mrp();



                //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();



                String atr_price= String.valueOf(attribute_value);
                String atr_mrp= String.valueOf(attribute_mrp);
                int atr_dis=getDiscount(atr_price,atr_mrp);
                holder.product_price.setText("\u20B9"+attribute_value.toString());
                holder.product_mrp.setText("\u20B9"+attribute_mrp.toString()+"/"+attribute_value);


            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }


        }

        String product_id= String.valueOf(map.get("product_id"));
        if(atr.equals("[]"))
        {
            boolean st=db_cart.isInCart(product_id);
            if(st==true)
            {
                holder.add.setVisibility(View.GONE);
                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                holder.elegantNumberButton.setVisibility(View.VISIBLE);
            }

        }
        else
        { ;
            String at_id = "1234";
            boolean st=db_cart.isInCart(at_id);
            if(st==true)
            {
                holder.add.setVisibility(View.GONE);
                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(at_id));
                holder.elegantNumberButton.setVisibility(View.VISIBLE);
            }
            else {

            }
        }





        /*holder.rel_variant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final HashMap<String, String> map = list.get(position);
                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row=layoutInflater.inflate(R.layout.dialog_vairant_layout,null);
                variantList.clear();
                String atr= String.valueOf(map.get("product_attribute"));
                JSONArray jsonArr = null;
                try {

                    jsonArr = new JSONArray(atr);
                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        ProductVariantModel model=new ProductVariantModel();
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        String atr_id=jsonObj.getString("id");
                        String atr_product_id=jsonObj.getString("product_id");
                        String attribute_name=jsonObj.getString("attribute_name");
                        String attribute_value=jsonObj.getString("attribute_value");
                        String attribute_mrp=jsonObj.getString("attribute_mrp");


                        model.setId(atr_id);
                        model.setProduct_id(atr_product_id);
                        model.setAttribute_value(attribute_value);
                        model.setAttribute_name(attribute_name);
                        model.setAttribute_mrp(attribute_mrp);

                        variantList.add(model);

                        //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                        //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListView l1=(ListView)row.findViewById(R.id.list_view_varaint);
                productVariantAdapter=new ProductVariantAdapter(activity,variantList);
                //productVariantAdapter.notifyDataSetChanged();
                l1.setAdapter(productVariantAdapter);


                builder.setView(row);
                final AlertDialog ddlg=builder.create();
                ddlg.show();
                l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                        holder.dialog_unit_type.setText("\u20B9"+variantList.get(i).getAttribute_value()+"/"+variantList.get(i).getAttribute_name());
                        holder.dialog_txtId.setText(variantList.get(i).getId()+"@"+i);
                        holder.dialog_txtVar.setText(variantList.get(i).getAttribute_value()+"@"+variantList.get(i).getAttribute_name()+"@"+variantList.get(i).getAttribute_mrp());
                        //    txtPer.setText(String.valueOf(df)+"% off");

                        holder.product_price.setText("\u20B9"+variantList.get(i).getAttribute_value().toString());
                        holder.product_mrp.setText("\u20B9"+variantList.get(i).getAttribute_mrp().toString());
                        String pr= String.valueOf(variantList.get(i).getAttribute_value());
                        String mr= String.valueOf(variantList.get(i).getAttribute_mrp());
                        int atr_dis=getDiscount(pr,mr);
                        holder.discount.setText(""+atr_dis+"% OFF");
                        String atr= String.valueOf(map.get("product_attribute"));
                        String product_id= String.valueOf(map.get("product_id"));
                        if(atr.equals("[]"))
                        {
                            boolean st=db_cart.isInCart(product_id);
                            if(st==true)
                            {
                                holder.add.setVisibility(View.GONE);
                                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                                holder.elegantNumberButton.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            String str_id=holder.dialog_txtId.getText().toString();
                            String[] str=str_id.split("@");
                            String at_id= String.valueOf(str[0]);
                            boolean st=db_cart.isInCart(at_id);
                            if(st==true)
                            {
                                holder.add.setVisibility(View.GONE);
                                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(at_id));
                                holder.elegantNumberButton.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                holder.add.setVisibility(View.VISIBLE);

                                holder.elegantNumberButton.setVisibility(View.GONE);
                            }
                        }

                        ddlg.dismiss();
                    }
                });


            }
        });*/



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                builder.setMessage("Are You Sure?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                holder.db_wish.removeItemFromWishtable(map.get("product_id"),user_id);
                                list.remove(position);
                                notifyDataSetChanged();

                                if(list.size()<=0)
                                {
                                    WishlistFragment.rel_no.setVisibility(View.VISIBLE);
                                    WishlistFragment.rv_wishlist.setVisibility(View.GONE);
                                }

                                // db_cart.getCartAll()
                                updateintent();
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });
        holder.rel_wishlist.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetails(view);

            }

            public void openDetails(View view) {
                Bundle args = new Bundle();
                args.putString("cat_id",map.get( "cat_id" ));
                args.putString("product_id",map.get( "product_id" ));
                args.putString("product_images",map.get( "product_images" ));
                args.putString("product_name",map.get("product_name"));
                args.putString("product_description",map.get( "product_description" ));
                args.putString("product_attribute",map.get( "product_attribute" ));
                args.putString("stock",map.get("stock"));
                args.putString("in_stock",map.get("in_stock"));
                args.putString("price",map.get("price"));
                args.putString("mrp",map.get("mrp"));
                args.putString("unit_value",map.get( "unit_value" ));
                args.putString("unit",map.get("unit"));
                args.putString("rewards",map.get("rewards"));
                args.putString("increment",map.get("increment"));
                args.putString("title",map.get( "title" ));
                // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                DetailsFragment fm = new DetailsFragment();
                fm.setArguments(args);
//                FragmentManager fragmentManager = .beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();

                AppCompatActivity activity=(AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fm)
                        .addToBackStack(null)
                        .commit();
            }
        } );
        holder.add.setOnClickListener( new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                openDetails(view);

            }
            public void openDetails(View view) {
                Bundle args = new Bundle();
                args.putString("cat_id",map.get( "cat_id" ));
                args.putString("product_id",map.get( "product_id" ));
                args.putString("product_images",map.get( "product_images" ));
                args.putString("product_name",map.get("product_name"));
                args.putString("product_description",map.get( "product_description" ));
                args.putString("product_attribute",map.get( "product_attribute" ));
                args.putString("stock",map.get("stock"));
                args.putString("in_stock",map.get("in_stock"));
                args.putString("price",map.get("price"));
                args.putString("mrp",map.get("mrp"));
                args.putString("unit_value",map.get( "unit_value" ));
                args.putString("unit",map.get("unit"));
                args.putString("rewards",map.get("rewards"));
                args.putString("increment",map.get("increment"));
                args.putString("title",map.get( "title" ));
                // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                DetailsFragment fm = new DetailsFragment();
                fm.setArguments(args);
//                FragmentManager fragmentManager = .beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();

                AppCompatActivity activity=(AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fm)
                        .addToBackStack(null)
                        .commit();
            }
        } );

       /* holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                final HashMap<String, String> map = list.get(position);
                float stck= Float.parseFloat(map.get("stock"));
                if (newValue > stck) {
                    holder.elegantNumberButton.setNumber(String.valueOf(stock));
                    Toast.makeText(activity,"not Available", Toast.LENGTH_SHORT).show();
                }
                else {
                    String atr = String.valueOf(map.get("product_attribute"));
                    if (newValue <= 0) {
                        String p_id = String.valueOf(map.get("product_id"));
                        boolean st = checkAttributeStatus(atr);
                        if (st == false) {
                            db_cart.removeItemFromCart(p_id);
                        } else if (st == true) {

                            String str_id = holder.dialog_txtId.getText().toString();
                            String[] str = str_id.split("@");
                            String at_id = String.valueOf(str[0]);
                            db_cart.removeItemFromCart(at_id);
                        }

                        holder.elegantNumberButton.setVisibility(View.GONE);
                        holder.add.setVisibility(View.VISIBLE);
                    } else {


                        float qty = Float.parseFloat(String.valueOf(newValue));

                        //String atr=String.valueOf(modelList.get(position).getProduct_attribute());
                        if (atr.equals("[]")) {
                            double pr = Double.parseDouble(map.get("price"));
                            double amt = pr * qty;
                            HashMap<String, String> mapProduct = new HashMap<String, String>();

                            String unt = String.valueOf(map.get("unit_value") + " " + map.get("unit"));
                            mapProduct.put("cart_id", map.get("product_id"));
                            mapProduct.put("product_id", map.get("product_id"));
                            mapProduct.put("product_image", map.get("product_images"));
                            mapProduct.put("cat_id", map.get("cat_id"));
                            mapProduct.put("product_name", map.get("product_name"));
                            mapProduct.put("price", map.get("price"));
                            mapProduct.put("stock", map.get("stock"));
                            mapProduct.put("unit_price", map.get("price"));
                            mapProduct.put("unit", unt);
                            mapProduct.put("mrp", map.get("mrp"));
                            mapProduct.put("type", "p");
                            try {

                                boolean tr = db_cart.setCart(mapProduct, qty);
                                if (tr == true) {
                                    MainActivity mainActivity = new MainActivity();
                                    //mainActivity.setCartCounter("" + db_cart.getCartCount());

                                    //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                    Toast.makeText(activity, "Added to Cart", Toast.LENGTH_SHORT).show();
                                    int n = db_cart.getCartCount();
                                    updateintent();


                                } else if (tr == false) {
                                    Toast.makeText(activity, "Cart Updated", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception ex) {
                                Toast.makeText(activity, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                        } else {
                            //ProductVariantModel model=variantList.get(position);

                            String str_id = holder.dialog_txtId.getText().toString();


                            String s = holder.dialog_txtVar.getText().toString();
                            String[] st = s.split("@");
                            String st0 = String.valueOf(st[0]);
                            String st1 = String.valueOf(st[1]);
                            String st2 = String.valueOf(st[2]);
                            String[] str = str_id.split("@");
                            String at_id = String.valueOf(str[0]);
                            int k = Integer.parseInt(String.valueOf(str[1]));
                            double pr = Double.parseDouble(st0);
                            double amt = pr * qty;
                            //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();
                            HashMap<String, String> mapProduct = new HashMap<String, String>();
                            mapProduct.put("cart_id", at_id);
                            mapProduct.put("product_id", map.get("product_id"));
                            mapProduct.put("product_image", map.get("product_images"));
                            mapProduct.put("cat_id", map.get("cat_id"));
                            mapProduct.put("product_name", map.get("product_name"));
                            mapProduct.put("price", st0);
                            mapProduct.put("unit_price", st0);
                            mapProduct.put("stock", map.get("stock"));
                            mapProduct.put("unit", st1);
                            mapProduct.put("mrp", st2);
                            mapProduct.put("type", "a");
                            //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                            try {

                                boolean tr = db_cart.setCart(mapProduct, qty);
                                if (tr == true) {
                                    MainActivity mainActivity = new MainActivity();
                                    //mainActivity.setCartCounter("" + db_cart.getCartCount());

                                    //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                    Toast.makeText(activity, "Added to Cart", Toast.LENGTH_SHORT).show();
                                    int n = db_cart.getCartCount();
                                    updateintent();


                                } else if (tr == false) {
                                    Toast.makeText(activity, "Cart Updated", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception ex) {
                                Toast.makeText(activity, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }

            }
        });*/
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WishHolder extends RecyclerView.ViewHolder {
        public WishlistHandler db_wish;
        TextView product_name ,product_price ,product_mrp ;
        ImageView rel_out;
        ImageView iv_icon , delete ;
        Button add ;
        LinearLayout rel_wishlist;
        DatabaseCartHandler db_cart ;
        ElegantNumberButton elegantNumberButton ;

        public WishHolder(View itemView) {
            super( itemView );
            product_name = (TextView)itemView.findViewById( R.id.name_product );
            product_price=(TextView)itemView.findViewById( R.id.product_total );
            rel_out =itemView.findViewById( R.id.out_stock );
            product_mrp=(TextView)itemView.findViewById( R.id.rate_product );
            add=itemView.findViewById( R.id.btn_add );
            iv_icon=(ImageView)itemView.findViewById( R.id.img_product );
            delete=(ImageView)itemView.findViewById( R.id.wish_after );
            db_cart=new DatabaseCartHandler(activity);
            elegantNumberButton =(ElegantNumberButton)itemView.findViewById( R.id.product_qty);
            db_wish = new WishlistHandler( activity );
            attributeList=new ArrayList<>();
            variantList=new ArrayList<>();
            rel_wishlist = itemView.findViewById(R.id.rel_wishlist);
        }
    }
    private void updateintent() {
        Intent updates = new Intent("Cart");
        updates.putExtra("type", "update");
        activity.sendBroadcast(updates);
    }

    public int getDiscount(String price, String mrp)
    {
        double mrp_d= Double.parseDouble(mrp);
        double price_d= Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df= Math.round(per);
        int d=(int)df;
        return d;
    }

    public boolean checkAttributeStatus(String atr)
    {
        boolean sts=false;
        if(atr.equals("[]"))
        {
            sts=false;
        }
        else
        {
            sts=true;
        }
        return sts;
    }


    public void showDeleteDialog()
    {

    }

}
