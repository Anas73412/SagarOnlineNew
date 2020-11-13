package com.example.sagaronlineyash.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.sagaronlineyash.Activity.LoginActivity;
import com.example.sagaronlineyash.Activity.MainActivity;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Fragments.DetailsFragment;
import com.example.sagaronlineyash.Model.NewProductModel;
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


public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.MyViewHolder> {


    String atr_id="";
    String atr_product_id="";
    String attribute_name="";
    String attribute_value="";
    String attribute_mrp="";
    ArrayList<ProductVariantModel> variantList;
    ProductVariantAdapter productVariantAdapter;



    int status=0;
    private List<NewProductModel> modelList;
    private Context context;
    public int counter;
    public WishlistHandler db_wish;
    float stock ;
    float qty ;
    Session_management sessionManagement ;
    String user_id ;


    DatabaseCartHandler db_cart;
    // Activity activity;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_nmae, product_mrp ,product_discount , product_prize, dialog_txtVar;
        public ImageView image , wish_before ,wish_after,product_discount_img;

        RelativeLayout relativeLayout ,rel_add ,rel_out;
        ElegantNumberButton elegantNumberButton ;
        Button add_Button;
        public RelativeLayout rel_variant;
        private TextView dialog_unit_type,dialog_txtId,txtrate;


        LinearLayout showproduct;
        public MyViewHolder(View view) {
            super(view);
            //db_wish= new DatabaseHandlerWishList( activity );
            product_nmae = (TextView) view.findViewById( R.id.product_name);
            product_mrp = (TextView) view.findViewById( R.id.product_mrp);
            relativeLayout= view.findViewById( R.id.relative_top );
            product_discount=(TextView)view.findViewById( R.id.product_discount);
            product_discount_img=(ImageView) view.findViewById(R.id.product_discount_img);
            product_prize=(TextView)view.findViewById( R.id.product_prize );
            image = (ImageView) view.findViewById( R.id.iv_icon);
            wish_after=(ImageView)view.findViewById( R.id.wish_after );
            wish_before=(ImageView)view.findViewById( R.id.wish_before );
            add_Button=(Button)view.findViewById( R.id.btn_add );
            elegantNumberButton=(ElegantNumberButton) view.findViewById( R.id.product_qty );
            image = (ImageView) view.findViewById( R.id.iv_icon);
            db_wish= new WishlistHandler( context );
            db_cart=new DatabaseCartHandler(context);
            variantList=new ArrayList<>();
            rel_variant=(RelativeLayout)view.findViewById( R.id.rel_variant);
            dialog_unit_type=(TextView)view.findViewById( R.id.unit_type);
            dialog_txtId=(TextView)view.findViewById( R.id.txtId);
            txtrate=(TextView)view.findViewById( R.id.single_varient);
            dialog_txtVar=(TextView)view.findViewById( R.id.txtVar);

            rel_add = view.findViewById( R.id.rel_add );
            rel_out = view.findViewById( R.id.rel_out );





        }
    }

    public NewProductAdapter(List<NewProductModel> modelList, Activity activity) {
        db_cart=new DatabaseCartHandler(activity);
        db_wish=new WishlistHandler(activity);
        this.modelList = modelList;
    }

    @Override
    public NewProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.row_product, parent, false);

        context = parent.getContext();

        return new NewProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewProductAdapter.MyViewHolder holder, final int position) {
        final NewProductModel mList = modelList.get(position);
        final String getid = mList.getProduct_id();
        sessionManagement = new Session_management( context );
        user_id=sessionManagement.getUserDetails().get(KEY_ID);

        if(db_wish.isInWishtable( getid ,user_id))
        {
            holder.wish_after.setVisibility( View.VISIBLE );
            holder.wish_before.setVisibility( View.GONE );
        }
        else
        {
            holder.wish_after.setVisibility( View.GONE);
            holder.wish_before.setVisibility( View.VISIBLE );
        }
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        final String language=preferences.getString("language","");

        String img_array= mList.getProduct_image();
        String img_name = null;
        try {
            JSONArray array=new JSONArray(img_array);
            img_name=array.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(context)
                .load( BaseURL.IMG_PRODUCT_URL + img_name)
                .placeholder( R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        //holder.product_prize.setText(context.getResources().getString(R.string.tv_toolbar_price) + context.getResources().getString(R.string.currency) + mList.getPrice());
        // holder.product_prize.setText( context.getResources().getString(R.string.currency) + mList.getMrp());
        holder.product_mrp.setPaintFlags( holder.product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        stock=Float.parseFloat(mList.getStock());
        holder.elegantNumberButton.setRange( 0, (int) stock );

        if(stock<=0 || mList.getIn_stock().equals("0"))
        {
            holder.rel_out.setVisibility(View.VISIBLE);
        }
        else {
            holder.rel_out.setVisibility( View.GONE );

        }

        final String atr= String.valueOf(mList.getProduct_attribute());
        if(atr.equals("[]"))
        {


            status=1;
            String p= String.valueOf(mList.getPrice());
            String m= String.valueOf(mList.getMrp());
            int pp = Integer.parseInt( p );
            int mm = Integer.parseInt( m );
            holder.product_prize.setText(context.getResources().getString( R.string.currency)+ mList.getPrice());
            if (mm>pp) {

                holder.product_mrp.setText( context.getResources().getString( R.string.currency ) + mList.getMrp() );
                int discount = getDiscount( p, m );
                if(discount<=0)
                {
                    holder.product_discount.setVisibility(View.GONE);
                    holder.product_discount_img.setVisibility(View.GONE);
                }
                else
                {
                    holder.product_discount.setVisibility(View.VISIBLE);
                    holder.product_discount.setText( "" + discount + "% OFF" );
                    holder.product_discount_img.setVisibility(View.VISIBLE);
                }
                //Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();

            }
            else {
                holder.product_mrp.setVisibility( View.GONE );
                holder.product_discount.setVisibility( View.GONE );
                holder.product_discount_img.setVisibility(View.GONE);
            }
            holder.txtrate.setVisibility( View.VISIBLE);
            holder.txtrate.setText(mList.getUnit_value()+" "+mList.getUnit());


        }

        else
        {

            holder.rel_variant.setVisibility( View.VISIBLE);
            status=2;
            JSONArray jsonArr = null;
            try {

                jsonArr = new JSONArray(atr);

                ProductVariantModel model=new ProductVariantModel();
                JSONObject jsonObj = jsonArr.getJSONObject(0);
                atr_id=jsonObj.getString("id");
                atr_product_id=jsonObj.getString("product_id");
                attribute_name=jsonObj.getString("attribute_name");
                attribute_value=jsonObj.getString("attribute_value");
                attribute_mrp=jsonObj.getString("attribute_mrp");



                //     arrayList.add(new AttributeModel(atr_id,product_id,attribute_name,attribute_value));

                //Toast.makeText(getActivity(),"id "+atr_id+"\n p_id "+product_id+"\n atr_name "+attribute_name+"\n atr_value "+attribute_value,Toast.LENGTH_LONG).show();



                String atr_price= String.valueOf(attribute_value);
                String atr_mrp= String.valueOf(attribute_mrp);
                int atr_m = Integer.parseInt( atr_mrp );
                int atr_p = Integer.parseInt( atr_price );
                holder.product_prize.setText("\u20B9"+attribute_value.toString());
                if(atr_m >atr_p) {
                    int atr_dis = getDiscount( atr_price, atr_mrp );

                    if(atr_dis<=0)
                    {
                        holder.product_discount.setVisibility(View.GONE);
                        holder.product_discount_img.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.product_discount.setVisibility(View.VISIBLE);

                        holder.product_discount.setText( "" + atr_dis + "% OFF" );
                        holder.product_discount_img.setVisibility(View.VISIBLE);

                    }
                    holder.product_mrp.setText( "\u20B9" + attribute_mrp.toString() );
                }
                else
                {
                    holder.product_discount.setVisibility( View.GONE );
                    holder.product_mrp.setVisibility( View.GONE );
                    holder.product_discount_img.setVisibility(View.GONE);
                }

                holder.dialog_txtId.setText(atr_id.toString()+"@"+"0");
                //dialog_unit_type.setText("\u20B9"+variantList.get(i).getAttribute_value()+"/"+variantList.get(i).getAttribute_name());
                //dialog_txtId.setText(variantList.get(i).getId()+"@"+i);
                holder.dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                holder.dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                //  holder.txtTotal.setText("\u20B9"+String.valueOf(list_atr_value.get(0).toString()));

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }

        final String product_id= String.valueOf(mList.getProduct_id());
        if(atr.equals("[]"))
        {
            boolean st=db_cart.isInCart(product_id);
            if(st==true)
            {
                holder.add_Button.setVisibility( View.GONE);
                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                holder.elegantNumberButton.setVisibility( View.VISIBLE);

            }
            else
            {
                holder.add_Button.setVisibility( View.VISIBLE );
                holder.elegantNumberButton.setVisibility( View.GONE );
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
                holder.add_Button.setVisibility( View.GONE);
                holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(at_id));
                holder.elegantNumberButton.setVisibility( View.VISIBLE);
            }
            else {
                holder.add_Button.setVisibility( View.VISIBLE );
                holder.elegantNumberButton.setVisibility( View.GONE );

            }
        }



        holder.rel_variant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final NewProductModel mList = modelList.get(position);
                float stck=Float.parseFloat(mList.getStock());
                if (stck <= 0 || mList.getIn_stock().equals("0")) {
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = layoutInflater.inflate(R.layout.dialog_vairant_layout, null);
                    variantList.clear();
                    String atr = String.valueOf(mList.getProduct_attribute());
                    JSONArray jsonArr = null;
                    try {

                        jsonArr = new JSONArray(atr);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            ProductVariantModel model = new ProductVariantModel();
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            String atr_id = jsonObj.getString("id");
                            String atr_product_id = jsonObj.getString("product_id");
                            String attribute_name = jsonObj.getString("attribute_name");
                            String attribute_value = jsonObj.getString("attribute_value");
                            String attribute_mrp = jsonObj.getString("attribute_mrp");


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
                    ListView l1 = (ListView) row.findViewById(R.id.list_view_varaint);
                    productVariantAdapter = new ProductVariantAdapter(context, variantList);
                    //productVariantAdapter.notifyDataSetChanged();
                    l1.setAdapter(productVariantAdapter);


                    builder.setView(row);
                    final AlertDialog ddlg = builder.create();
                    ddlg.show();
                    l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            atr_id = String.valueOf(variantList.get(i).getId());
                            atr_product_id = String.valueOf(variantList.get(i).getProduct_id());
                            attribute_name = String.valueOf(variantList.get(i).getAttribute_name());
                            attribute_value = String.valueOf(variantList.get(i).getAttribute_value());
                            attribute_mrp = String.valueOf(variantList.get(i).getAttribute_mrp());

                            holder.dialog_unit_type.setText("\u20B9" + attribute_value + "/" + attribute_name);
                            //   holder.dialog_txtId.setText(variantList.get(i).getId());
                            holder.dialog_txtId.setText(variantList.get(i).getId() + "@" + i);
                            holder.dialog_txtVar.setText(variantList.get(i).getAttribute_value() + "@" + variantList.get(i).getAttribute_name() + "@" + variantList.get(i).getAttribute_mrp());

                            //    txtPer.setText(String.valueOf(df)+"% off");

                            holder.product_prize.setText("\u20B9" + attribute_value.toString());

                            String pr = String.valueOf(attribute_value);
                            String mr = String.valueOf(attribute_mrp);
                            int ppr = Integer.parseInt(pr);
                            int mmr = Integer.parseInt(mr);
                            if (mmr > ppr) {
                                int atr_dis = getDiscount(pr, mr);
                                if (atr_dis<=0)
                                {
                                    holder.product_discount.setVisibility( View.GONE );
                                    holder.product_discount_img.setVisibility(View.GONE);
                                }
                                else {
                                    holder.product_discount.setText( "" + atr_dis + "% OFF" );
                                    holder.product_mrp.setText( "\u20B9" + attribute_mrp.toString() );
                                    holder.product_discount_img.setVisibility(View.VISIBLE);

                                }
                            } else {
                                holder.product_mrp.setVisibility(View.GONE);
                                holder.product_discount.setVisibility(View.GONE);
                                holder.product_discount_img.setVisibility(View.GONE);
                            }
                            String atr = String.valueOf(modelList.get(position).getProduct_attribute());
                            String product_id = String.valueOf(modelList.get(position).getProduct_id());
                            if (atr.equals("[]")) {
                                boolean st = db_cart.isInCart(product_id);
                                if (st == true) {
                                    holder.add_Button.setVisibility(View.GONE);
                                    holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(product_id));
                                    holder.elegantNumberButton.setVisibility(View.VISIBLE);
                                } else {
                                    holder.add_Button.setVisibility(View.VISIBLE);
                                    holder.elegantNumberButton.setVisibility(View.GONE);
                                }
                            } else {
                                String str_id = holder.dialog_txtId.getText().toString();
                                String[] str = str_id.split("@");
                                String at_id = String.valueOf(str[0]);
                                boolean st = db_cart.isInCart(at_id);
                                if (st == true) {
                                    holder.add_Button.setVisibility(View.GONE);
                                    holder.elegantNumberButton.setNumber(db_cart.getCartItemQty(at_id));
                                    holder.elegantNumberButton.setVisibility(View.VISIBLE);
                                } else {
                                    holder.add_Button.setVisibility(View.VISIBLE);

                                    holder.elegantNumberButton.setVisibility(View.GONE);
                                }
                            }

                            // holder.elegantNumberButton.setNumber("1");


                            ddlg.dismiss();
                        }
                    });

                }
            }
        });

        holder.relativeLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float stock=Float.parseFloat(modelList.get(position).getStock());



                final NewProductModel mList = modelList.get(position);
                Bundle args = new Bundle();
//

                //args.putString("product_id",mList.getProduct_id());
                args.putString("cat_id", modelList.get(position).getCategory_id());
                args.putString("product_id", modelList.get(position).getProduct_id());
                args.putString("product_images", modelList.get(position).getProduct_image());
                args.putString("product_name", modelList.get(position).getProduct_name());
                args.putString("product_description", modelList.get(position).getProduct_description());
                args.putString("stock", modelList.get(position).getStock());
                args.putString("in_stock", modelList.get(position).getIn_stock());
//                args.putString("product_size",modelList.get(position).getSize());
//                args.putString("product_color",modelList.get( position).getColor());
                args.putString("price", modelList.get(position).getPrice());
                args.putString("mrp", modelList.get(position).getMrp());
                args.putString("unit_value", modelList.get(position).getUnit_value());
                args.putString("unit", modelList.get(position).getUnit());
                args.putString("product_attribute", modelList.get(position).getProduct_attribute());
                args.putString("rewards", modelList.get(position).getRewards());
                args.putString("increment", modelList.get(position).getIncreament());
                args.putString("title", modelList.get(position).getTitle());
                // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                DetailsFragment fm = new DetailsFragment();
                fm.setArguments(args);
//                FragmentManager fragmentManager = .beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getFragmentManager().beginTransaction().replace(R.id.fragment_container, fm)
                        .addToBackStack(null)
                        .commit();

            }

        } );

        holder.wish_before.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // holder.wish_after.setVisibility( View.VISIBLE );
                if (sessionManagement.isLoggedIn()) {
                    float stck=Float.parseFloat(modelList.get(position).getStock());
                    if (stck<1 || modelList.get(position).getIn_stock().equals("0"))
                    {
                        Toast.makeText( context,"Out Of Stock",Toast.LENGTH_LONG ).show();
                    }
                    else
                    {
                        holder.wish_after.setVisibility( View.VISIBLE );
                        holder.wish_before.setVisibility( View.INVISIBLE );

                        final NewProductModel mList = modelList.get( position );

                        String tl = "";
                        String title = String.valueOf( mList.getTitle() );
                        if (title.equals( "null" )) {
                            tl = "title";
                        } else {
                            tl = String.valueOf( mList.getTitle() );
                        }

                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put( "product_id", mList.getProduct_id() );
                        mapProduct.put( "product_images", mList.getProduct_image() );
                        mapProduct.put( "cat_id", mList.getCategory_id() );
                        mapProduct.put( "product_name", mList.getProduct_name() );
                        mapProduct.put( "price", mList.getPrice() );
                        mapProduct.put( "product_description", mList.getProduct_description() );
                        mapProduct.put( "rewards", mList.getRewards() );
                        mapProduct.put( "unit_value", mList.getUnit_value() );
                        mapProduct.put( "unit", mList.getUnit() );
                        mapProduct.put( "increment", mList.getIncreament() );
                        mapProduct.put( "stock", mList.getStock() );
                        mapProduct.put( "title", tl );
                        mapProduct.put( "mrp", mList.getMrp() );
                        mapProduct.put("user_id",user_id);
                        mapProduct.put( "product_attribute", mList.getProduct_attribute() );
                        mapProduct.put( "in_stock", mList.getIn_stock() );

                        // Toast.makeText(context,""+mapProduct,Toast.LENGTH_LONG).show();


                        try {

                            boolean tr = db_wish.setwishTable( mapProduct );
                            if (tr == true) {

                                Log.e("user_id",user_id);
                                Log.e("WishTable",db_wish.getWishtableAll(user_id).toString());
                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText( context, "Added to Wishlist", Toast.LENGTH_LONG ).show();


                            } else {
                                Toast.makeText( context, "Something Went Wrong", Toast.LENGTH_LONG ).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else
                {
                    Intent intent = new Intent( context, LoginActivity.class );
                    context.startActivity( intent );
                }


            }
        } );

        holder.wish_after.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.wish_after.setVisibility( View.INVISIBLE );
                holder.wish_before.setVisibility( View.VISIBLE );
                db_wish.removeItemFromWishtable(mList.getProduct_id(),user_id);
                Toast.makeText(context, "removed from Wishlist" , Toast.LENGTH_LONG).show();
                // list.remove(position);
                notifyDataSetChanged();

            }
        } );

        holder.add_Button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float stck=Float.parseFloat(mList.getStock());
                if (stck < 1 || mList.getIn_stock().equals("0")) {
                    Toast.makeText( context, "Out Of Stock", Toast.LENGTH_LONG ).show();
                }
                else {
                    holder.add_Button.setVisibility( View.INVISIBLE );
                    NewProductModel mList = modelList.get( position );
                    String atr = String.valueOf( modelList.get( position ).getProduct_attribute() );
                    if (atr.equals( "[]" )) {
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf( mList.getUnit_value() + " " + mList.getUnit() );
                        mapProduct.put( "cart_id", mList.getProduct_id() );
                        mapProduct.put( "product_id", mList.getProduct_id() );
                        mapProduct.put( "product_image", mList.getProduct_image() );
                        mapProduct.put( "cat_id", mList.getCategory_id() );
                        mapProduct.put( "product_name", mList.getProduct_name() );
                        mapProduct.put( "price", mList.getPrice() );
                        mapProduct.put( "unit_price", mList.getPrice() );
                        mapProduct.put( "stock", mList.getStock() );
                        mapProduct.put( "unit", unt );
                        mapProduct.put( "mrp", mList.getMrp() );
                        mapProduct.put( "type", "p" );
                        try {

                            boolean tr = db_cart.setCart( mapProduct, (float) 1 );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                                //mainActivity.setCartCounter( "" + db_cart.getCartCount() );

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText( context, "Added to Cart", Toast.LENGTH_LONG ).show();
                                int n = db_cart.getCartCount();
                                updateintent();


                            } else if (tr == false) {
                                Toast.makeText( context, "cart updated", Toast.LENGTH_LONG ).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText( context, "" + ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }

                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                    } else {
                        //ProductVariantModel model=variantList.get(position);

                        String str_id = holder.dialog_txtId.getText().toString();
                        String s = holder.dialog_txtVar.getText().toString();
                        String[] st = s.split( "@" );
                        String st0 = String.valueOf( st[0] );
                        String st1 = String.valueOf( st[1] );
                        String st2 = String.valueOf( st[2] );
                        String[] str = str_id.split( "@" );
                        String at_id = String.valueOf( str[0] );
                        int j = Integer.parseInt( String.valueOf( str[1] ) );
                        //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put( "cart_id", at_id );
                        mapProduct.put( "product_id", mList.getProduct_id() );
                        mapProduct.put( "product_image", mList.getProduct_image() );
                        mapProduct.put( "cat_id", mList.getCategory_id() );
                        mapProduct.put( "product_name", mList.getProduct_name() );
                        mapProduct.put( "price", st0 );
                        mapProduct.put( "unit_price", st0 );
                        mapProduct.put( "stock", mList.getStock() );
                        mapProduct.put( "unit", st1 );
                        mapProduct.put( "mrp", st2 );
                        mapProduct.put( "type", "a" );
                        //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                        try {

                            boolean tr = db_cart.setCart( mapProduct, (float) 1 );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                                //mainActivity.setCartCounter( "" + db_cart.getCartCount() );

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText( context, "Added to Cart", Toast.LENGTH_LONG ).show();
                                int n = db_cart.getCartCount();
                                updateintent();


                            } else if (tr == false) {
                                Toast.makeText( context, "cart updated", Toast.LENGTH_LONG ).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText( context, "" + ex.getMessage(), Toast.LENGTH_LONG ).show();
                        }

                    }

                    updateintent();
                    holder.add_Button.setVisibility( View.GONE );
                    holder.elegantNumberButton.setVisibility( View.VISIBLE );
                    holder.elegantNumberButton.setNumber( "1" );


                }
            }
        } );
        holder.elegantNumberButton.setOnValueChangeListener( new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                final NewProductModel mList = modelList.get(position);
                qty = Float.parseFloat( String.valueOf( newValue ) );

                String atr= String.valueOf(modelList.get(position).getProduct_attribute());
                if(newValue<=0)
                {
                    boolean st=checkAttributeStatus(atr);
                    if(st==false)
                    {
                        db_cart.removeItemFromCart(product_id);

                    }
                    else if(st==true)
                    {

                        String str_id = holder.dialog_txtId.getText().toString();
                        String[] str = str_id.split("@");
                        String at_id = String.valueOf(str[0]);
                        db_cart.removeItemFromCart(at_id);
                    }

                    holder.elegantNumberButton.setVisibility( View.GONE);
                    holder.add_Button.setVisibility( View.VISIBLE);
                    updateintent();
                }
//                else if (newValue == stock)
//                {
//
//                    int ss = Integer.parseInt( mList.getStock());
//                    Toast.makeText( context,"Only " +ss+ " in Stock",Toast.LENGTH_LONG  ).show();
//                }
                else
                {

                    //       String atr = String.valueOf(modelList.get(position).getProduct_attribute());
                    if (atr.equals("[]")) {
                        double pr = Double.parseDouble(mList.getPrice());
                        double amt = pr * qty;
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf(mList.getUnit_value() + " " + mList.getUnit());
                        mapProduct.put("cart_id", mList.getProduct_id());
                        mapProduct.put("product_id", mList.getProduct_id());
                        mapProduct.put("product_image", mList.getProduct_image());
                        mapProduct.put("cat_id", mList.getCategory_id());
                        mapProduct.put("product_name", mList.getProduct_name());
                        mapProduct.put("price", String.valueOf(amt));
                        mapProduct.put("unit_price", mList.getPrice());
                        mapProduct.put("stock", mList.getStock());
                        mapProduct.put("unit", unt);
                        mapProduct.put("mrp", mList.getMrp());
                        mapProduct.put("type", "p");
                        try {

                            boolean tr = db_cart.setCart(mapProduct, qty);
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                               // mainActivity.setCartCounter("" + db_cart.getCartCount());

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
                                int n = db_cart.getCartCount();
                                updateintent();


                            } else if (tr == false) {
                                Toast.makeText(context, "cart updated", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
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
                        int k = Integer.parseInt( String.valueOf(str[1]));
                        double pr = Double.parseDouble(st0);
                        double amt = pr * qty;
                        //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put("cart_id", at_id);
                        mapProduct.put("product_id", mList.getProduct_id());
                        mapProduct.put("product_image", mList.getProduct_image());
                        mapProduct.put("cat_id", mList.getCategory_id());
                        mapProduct.put("product_name", mList.getProduct_name());
                        mapProduct.put("price", String.valueOf(amt));
                        mapProduct.put("unit_price", st0);
                        mapProduct.put("stock", mList.getStock());
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
                                Toast.makeText(context, "Added to Cart", Toast.LENGTH_LONG).show();
                                int n = db_cart.getCartCount();
                                updateintent();


                            } else if (tr == false) {
                                Toast.makeText(context, "cart updated", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                            Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }

            }
        } );
        if (language.contains("english")) {
            holder.product_nmae.setText(mList.getProduct_name());
        }
        else {
            holder.product_nmae.setText(mList.getProduct_name());

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
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
    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
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



}

