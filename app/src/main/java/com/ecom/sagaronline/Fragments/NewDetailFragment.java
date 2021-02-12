package com.ecom.sagaronline.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.ecom.sagaronline.Activity.LoginActivity;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Adapter.ColorAdapter;
import com.ecom.sagaronline.Adapter.ProductVariantAdapter;
import com.ecom.sagaronline.Adapter.RelatedProductAdapter;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.Fragments.CartFragment;
import com.ecom.sagaronline.Fragments.DeliveryFragment;
import com.ecom.sagaronline.Fragments.EmptyCartFragment;
import com.ecom.sagaronline.Fragments.SubcategoryFragment;
import com.ecom.sagaronline.Model.ColorModel;
import com.ecom.sagaronline.Model.NewProductModel;
import com.ecom.sagaronline.Model.ProductVariantModel;
import com.ecom.sagaronline.Model.RelatedProductModel;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.BuyNowHandler;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.DatabaseCartHandler;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.Session_management;
import com.ecom.sagaronline.Utils.WishlistHandler;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.ecom.sagaronline.Config.BaseURL.GET_PRODUCT_DETAIL_URL;
import static com.ecom.sagaronline.Config.BaseURL.IMG_PRODUCT_URL;
import static com.ecom.sagaronline.Config.BaseURL.KEY_ID;

public class NewDetailFragment extends Fragment {
    TextView txtTotal,tv_details_product_price,tv_details_product_mrp,tv_details_product_off,tv_details_product_description,tv_descriptionTitle,tv_details_product_name,tv_variant;
    Button btn_add,btn_buy_now,btn_checkout;
    TextView dialog_unit_type,dialog_txtId,dialog_txtVar;
    NumberPicker product_qty;
    ArrayList<NewProductModel> list;
    ArrayList<ProductVariantModel> vlist;
    private RelatedProductAdapter adapter_product;
    private List<RelatedProductModel> product_modelList = new ArrayList<>();
    RelativeLayout rel_out,lin_img,rel_variant;
    ImageView wish_before,wish_after;
    CarouselView img_slider;
    WishlistHandler db_wish ;
    public static ArrayList<String> image_list;
    ArrayList<ProductVariantModel> variantList;
    NewProductModel newProductModel;
    RecyclerView top_selling_recycler;
    String atr_price,atr_mrp;
    String user_id;
    Module module;
    RelativeLayout rv_weight;
    ProductVariantAdapter productVariantAdapter;
    private ElegantNumberButton numberButton;
    Session_management session_management;
    RecyclerView  recyclerViewColor , recyclerViewSize;
    DatabaseCartHandler db_carts;
    BuyNowHandler db_buy_now;
    ArrayList<ColorModel> color_list;
    public static ArrayList<String>sub_image_list;
    ColorAdapter colorAdapter;
    String product_id="";
    String in_stock="",stock="",stock_value="";
    String atr_id="";
    String atr_product_id="";
    String attribute_name="";
    String attribute_value="";
    String attribute_mrp="";
    String attribute_color = "";
    String attribute_size="";
    String status="";
    LoadingBar loadingBar;


    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        db_wish=new WishlistHandler( getActivity() );
        module = new Module(getContext());
        loadingBar = new LoadingBar(getContext());
        rv_weight= view.findViewById(R.id.weight);
        session_management = new Session_management(getContext());
        user_id=session_management.getUserDetails().get(KEY_ID);
        db_carts=new DatabaseCartHandler(getActivity());
        db_buy_now =new BuyNowHandler(getActivity());
        tv_details_product_price=view.findViewById(R.id.details_product_price);
        tv_details_product_mrp=view.findViewById(R.id.details_product_mrp);
        tv_details_product_mrp.setPaintFlags(tv_details_product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv_details_product_off=view.findViewById(R.id.details_product_per);
        tv_details_product_description=view.findViewById(R.id.details_product_description);
        tv_descriptionTitle=view.findViewById(R.id.descriptionTitle);
        tv_details_product_name=view.findViewById(R.id.details_product_name);
        recyclerViewColor = view.findViewById(R.id.rec_color);
        recyclerViewSize = view.findViewById(R.id.rec_size);
        wish_before=view.findViewById(R.id.wish_before);
        numberButton=view.findViewById(R.id.product_qty);
        txtTotal=(TextView)view.findViewById(R.id.product_total);
        wish_after=view.findViewById(R.id.wish_after);
        rel_variant=(RelativeLayout)view.findViewById(R.id.rel_variant);
        rel_out= view.findViewById( R.id.rel_out );
        lin_img = view.findViewById(R.id.relative_layout_img);
        img_slider = view.findViewById(R.id.img_slider);
        tv_variant = view.findViewById(R.id.tv_variant);
        btn_checkout = view.findViewById(R.id.btn_f_Add_to_cart);
        btn_add=view.findViewById(R.id.btn_add);
        btn_buy_now=view.findViewById(R.id.btn_buy_now);
       // product_qty=(ElegantNumberButton)view.findViewById(R.id.product_qty);
        top_selling_recycler = view.findViewById(R.id.top_selling_recycler);
        dialog_unit_type=(TextView)view.findViewById(R.id.unit_type);
        dialog_txtId=(TextView)view.findViewById(R.id.txtId);
        dialog_txtVar=(TextView)view.findViewById(R.id.txtVar);
        numberButton.setBackgroundColor(getContext().getResources().getColor(R.color.white));

        Bundle bundle=getArguments();
        product_id=bundle.getString("product_id");
        productDetail(product_id);
        if(db_wish.isInWishtable( product_id ,user_id ))
        {
            wish_after.setVisibility( View.VISIBLE );
            wish_before.setVisibility( View.GONE );
        }
        else
        {
            wish_after.setVisibility( View.GONE );
            wish_before.setVisibility( View.VISIBLE );
        }


        vlist = new ArrayList<>();
        variantList=new ArrayList<>();
        color_list=new ArrayList<>();
        sub_image_list = new ArrayList<>();
        image_list=new ArrayList<String>();
        list=new ArrayList<>();

        db_buy_now.clearCart();
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        top_selling_recycler.setLayoutManager(new GridLayoutManager(getActivity(),2));

        rel_variant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row=layoutInflater.inflate(R.layout.dialog_vairant_layout,null);
                variantList.clear();
                String atr=String.valueOf(list.get(0).getProduct_attribute());
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
                        String attribute_color = jsonObj.getString("attribute_color");
                        model.setAttribute_color(attribute_color);
                        model.setId(atr_id);
                        model.setProduct_id(atr_product_id);
                        model.setAttribute_value(attribute_value);
                        model.setAttribute_name(attribute_name);
                        model.setAttribute_mrp(attribute_mrp);
                        variantList.add(model);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListView l1=(ListView)row.findViewById(R.id.list_view_varaint);
                productVariantAdapter=new ProductVariantAdapter(getActivity(),variantList);
                //productVariantAdapter.notifyDataSetChanged();
                l1.setAdapter(productVariantAdapter);


                builder.setView(row);
                final AlertDialog ddlg=builder.create();
                ddlg.show();
                l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        atr_id=String.valueOf(vlist.get(0).getId());
                        atr_product_id=String.valueOf(vlist.get(0).getProduct_id());
                        attribute_name=String.valueOf(vlist.get(0).getAttribute_name());
                        attribute_value=String.valueOf(vlist.get(0).getAttribute_value());
                        attribute_mrp=String.valueOf(vlist.get(0).getAttribute_mrp());
                        attribute_color=String.valueOf(vlist.get(0).getAttribute_color());
                        JSONArray colorarray= null;
                        try {
                            getValues(attribute_color);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setColor();
                        tv_variant.setText("\u20B9"+vlist.get(0).getAttribute_value()+"/"+vlist.get(0).getAttribute_name());
                    //    dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                        dialog_txtId.setText(vlist.get(0).getId()+"@"+i);
                        dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                        //    txtPer.setText(String.valueOf(df)+"% off");

                       // tv_details_product_price.setText("\u20B9"+attribute_value.toString());
                        tv_details_product_price.setText("\u20B9"+vlist.get(0).getAttribute_value().toString());
                       // tv_details_product_mrp.setText("\u20B9"+attribute_mrp.toString());
                        tv_details_product_mrp.setText("\u20B9"+vlist.get(0).getAttribute_mrp().toString());
                        String pr=String.valueOf(attribute_value);
                        String mr=String.valueOf(attribute_mrp);
                        int atr_dis=getDiscount(pr,mr);
                        tv_details_product_off.setText(""+atr_dis+"%"+" OFF");
                        String atr=String.valueOf(list.get(0).getProduct_attribute());
                        if(atr.equals("[]"))
                        {
                            boolean st=db_carts.isInCart(product_id);
                            if(st==true)
                            {
                               // numberButton.setValue(Integer.parseInt(db_carts.getCartItemQty(product_id)));
                                numberButton.setNumber(String.valueOf(Integer.parseInt(db_carts.getCartItemQty(product_id))));

                            }
                        }
                        else
                        {
                            String str_id=dialog_txtId.getText().toString();
                            String[] str=str_id.split("@");
                            String at_id=String.valueOf(str[0]);
                            boolean st=db_carts.isInCart(at_id);
                            if(st==true)
                            {
                               // numberButton.setValue(Integer.parseInt(db_carts.getCartItemQty(at_id)));
                                numberButton.setNumber(String.valueOf(Integer.parseInt(db_carts.getCartItemQty(at_id))));
                            }
                            else
                            {
//                                btn_add.setVisibility(View.VISIBLE);
//
//                                numberButton.setVisibility(View.GONE);
                            }
                        }


                        txtTotal.setText("\u20B9"+String.valueOf(db_carts.getTotalAmount()));
                        ddlg.dismiss();
                    }
                });

            }
        });


        wish_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wish_after.setVisibility( View.VISIBLE );
//                wish_before.setVisibility( View.INVISIBLE );

                if (session_management.isLoggedIn()) {
                    int stck = Integer.parseInt(stock);

                    if (stck < 1 || in_stock.equals("0")) {
                    wish_after.setVisibility(View.GONE);
                        Toast.makeText( getActivity(), "Out Of Stock", Toast.LENGTH_LONG ).show();
                    }
                    else {
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
                    }
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
//                Toast.makeText(getActivity(), "removed from Wishlist" +db_wish.getWishtableCount(user_id), Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "removed from Wishlist" , Toast.LENGTH_LONG).show();
            }
        });
//       btn_add.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               btn_add.setText("Update Cart");
//               Toast.makeText(getContext(),"Cart Updated!",Toast.LENGTH_SHORT).show();
//           }
//       });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int stck = Integer.parseInt(stock);
                if (stck < 1 || in_stock.equals("0")) {
                    Toast.makeText( getActivity(), "Out Of Stock", Toast.LENGTH_LONG ).show();
                } else {
                    //list.clear();
                   // vlist.clear();
                    String tot1 = txtTotal.getText().toString().trim();

                    String tot_amount = tot1.substring( 1, tot1.length() );
                    //float qty = numberButton.getValue() ;
                    float qty = Float.parseFloat(numberButton.getNumber());


                    String atr = String.valueOf( list.get(0).getProduct_attribute() );
                    if (atr.equals( "[]" )) {

                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf( list.get(0).getUnit_value() + " " + list.get(0).getUnit() );
                        mapProduct.put( "cart_id", product_id );
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "product_image", list.get(0).getProduct_image() );
                        mapProduct.put( "cat_id", list.get(0).getCategory_id() );
                        mapProduct.put( "product_name", list.get(0).getProduct_name() );
                        mapProduct.put( "price", list.get(0).getPrice() );
                        mapProduct.put( "unit_price", list.get(0).getPrice());
                        mapProduct.put( "color",list.get(0).getColor());
                        mapProduct.put( "stock", list.get(0).getStock());
                        mapProduct.put( "unit", unt );
                        mapProduct.put( "mrp", list.get(0).getMrp());
                        mapProduct.put( "type", "p" );
                        try {
                            Log.e(TAG, "onClick: "+mapProduct.toString() );
                            Log.e("qty", String.valueOf(qty));
                            boolean tr = db_carts.setCart( mapProduct, qty );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                                //mainActivity.setCartCounter( "" + db_carts.getCartCount() );

                                //   context.setCartCounter("" + holder.db_carts.getCartCount());
                                Toast.makeText( getActivity(), "Added to Cart", Toast.LENGTH_LONG ).show();
                                int n = db_carts.getCartCount();
//                                updateintent();
                                updateData();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_carts.getTotalAmount() ) );

                            } else if (tr == false) {
                                Toast.makeText( getActivity(), "cart updated", Toast.LENGTH_LONG ).show();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_carts.getTotalAmount() ) );
                            }
                            module.showToast(""+db_carts.getCartCount());

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                    } else {
                        //ProductVariantModel model=variantList.get(position);

                        String str_id = dialog_txtId.getText().toString();
                      //  dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                      //  String s = dialog_txtVar.getText().toString();
                       // String[] st = s.split( "@" );
                        String st0 = String.valueOf(vlist.get(0).getAttribute_value());
                        String st1 = String.valueOf( vlist.get(0).getAttribute_name() );
                        String st2 = String.valueOf( vlist.get(0).getAttribute_mrp() );
                       // String[] str = str_id.split( "@" );
                        String at_id = String.valueOf( vlist.get(0).getId() );
                        //int j = Integer.parseInt( String.valueOf( str[1] ) );
                        //       Toast.makeText(context,""+str[0].toString()+"\n"+str[1].toString(),Toast.LENGTH_LONG).show();
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put( "cart_id", at_id );
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "product_image", list.get(0).getProduct_image() );
                        mapProduct.put( "cat_id", list.get(0).getCategory_id() );
                        mapProduct.put( "color",list.get(0).getColor());
                        mapProduct.put( "product_name", list.get(0).getProduct_name());
                        mapProduct.put( "price", st0 );
                        mapProduct.put( "unit_price", st0 );
                        mapProduct.put( "stock", list.get(0).getStock() );
                        mapProduct.put( "unit", st1 );
                        mapProduct.put( "mrp", st2 );
                        mapProduct.put( "type", "a" );
                        //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                        try {

                            boolean tr = db_carts.setCart( mapProduct, qty );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                                // mainActivity.setCartCounter( "" + db_cart.getCartCount() );

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText( getActivity(), "Added to Cart", Toast.LENGTH_LONG ).show();
                                int n = db_carts.getCartCount();
//                                if (!db_carts.equals("")||db_carts!=null)
//                                {
//                                    btn_add.setText("Update Cart");
//                                }

//                                updateintent();
updateData();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_carts.getTotalAmount() ) );
                            } else if (tr == false) {
                                Toast.makeText( getActivity(), "cart updated", Toast.LENGTH_LONG ).show();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_carts.getTotalAmount() ) );
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //Toast.makeText(activity, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                    updateData();
                    btn_add.setText("Update Cart");
//                    btn_add.setVisibility( View.GONE );
//                    numberButton.setNumber( "1" );
//                    numberButton.setVisibility( View.VISIBLE );

                }
            }
        });

        btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // db_buy_now.clearSingleCart();
//                db_buy_now.clearCart();

                       int stck = Integer.parseInt(stock);
                if (stck < 1 || in_stock.equals("0")) {
                    Toast.makeText( getActivity(), "Out Of Stock", Toast.LENGTH_LONG ).show();
                } else {
//                    db_buy_now.clearSingleCart();
                    String tot1 = txtTotal.getText().toString().trim();

                    String tot_amount = tot1.substring( 1, tot1.length() );

                   // float qty = numberButton.getValue() ;
                    float qty = Float.parseFloat(numberButton.getNumber());


                    String atr = String.valueOf( list.get(0).getProduct_attribute() );
                    if (atr.equals( "[]" )) {

                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf( list.get(0).getUnit_value() + " " + list.get(0).getUnit() );
                        mapProduct.put( "cart_id", product_id );
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "product_image", list.get(0).getProduct_image() );
                        mapProduct.put( "cat_id", list.get(0).getCategory_id() );
                        mapProduct.put( "product_name", list.get(0).getProduct_name() );
                        mapProduct.put( "price", list.get(0).getPrice() );
                        mapProduct.put( "unit_price", list.get(0).getPrice());
                        mapProduct.put( "color",list.get(0).getColor());
                        mapProduct.put( "stock", list.get(0).getStock());
                        mapProduct.put( "unit", unt );
                        mapProduct.put( "mrp", list.get(0).getMrp());
                        mapProduct.put( "type", "p" );
                        try {
                            Log.e(TAG, "onClick: "+mapProduct.toString() );
                            Log.e("qty", String.valueOf(qty));
                            boolean tr = db_buy_now.setCart( mapProduct, qty );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                                //mainActivity.setCartCounter( "" + db_carts.getCartCount() );

                                //   context.setCartCounter("" + holder.db_carts.getCartCount());

//                                Toast.makeText(getActivity(),"list size "+list.size(),Toast.LENGTH_SHORT).show();
                                Toast.makeText( getActivity(), "Added to Cart", Toast.LENGTH_SHORT ).show();
                                //adapter_product.notifyDataSetChanged();
                                int n = db_buy_now.getCartCount();
//                                updateintentBuy();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_buy_now.getTotalAmount() ) );

                                Bundle args = new Bundle();
                                Fragment fm = new CartFragment();
                                args.putString("type", "buy_now");
                                fm.setArguments(args);
                                androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace( R.id.fragment_container, fm)
                                        .addToBackStack(null).commit();

//                                Intent intent =new Intent(getContext(),CartFragment.class);
//                                Bundle bundle=getArguments();
//                                if(bundle!=null){
//                                   String type=bundle.getString("type");
//                                }
//                                startActivity(intent);

                            } else if (tr == false) {
                                Toast.makeText( getActivity(), "cart updated", Toast.LENGTH_LONG ).show();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_buy_now.getTotalAmount() ) );
                            }
                            module.showToast(""+db_buy_now.getCartCount());

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                    } else {


                        String str_id = dialog_txtId.getText().toString();
                        String st0 = String.valueOf(vlist.get(0).getAttribute_value());
                        String st1 = String.valueOf( vlist.get(0).getAttribute_name() );
                        String st2 = String.valueOf( vlist.get(0).getAttribute_mrp() );
                        String at_id = String.valueOf( vlist.get(0).getId() );

                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put( "cart_id", at_id );
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "product_image", list.get(0).getProduct_image() );
                        mapProduct.put( "cat_id", list.get(0).getCategory_id() );
                        mapProduct.put( "color",list.get(0).getColor());
                        mapProduct.put( "product_name", list.get(0).getProduct_name());
                        mapProduct.put( "price", st0 );
                        mapProduct.put( "unit_price", st0 );
                        mapProduct.put( "stock", list.get(0).getStock() );
                        mapProduct.put( "unit", st1 );
                        mapProduct.put( "mrp", st2 );
                        mapProduct.put( "type", "a" );
                        Log.e("item_in_cart",mapProduct.toString());
                        Log.e("item_in_qty", String.valueOf(qty));
                          //Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                        try {

                            boolean tr = db_buy_now.setCart( mapProduct, qty );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();

                                Toast.makeText(getActivity(), "added " +db_buy_now.getCartCount(), Toast.LENGTH_LONG).show();

                                //Toast.makeText(getActivity(),"list size "+list.size(),Toast.LENGTH_SHORT).show();
                                Toast.makeText( getActivity(), "Added to Cart", Toast.LENGTH_LONG ).show();
                                Log.e(TAG, "onClick: "+db_buy_now.getCartAll().get(0).toString() );
                                int n = db_buy_now.getCartCount();

//                                updateintentBuy();

                                txtTotal.setText( "\u20B9" + String.valueOf( db_buy_now.getTotalAmount() ) );

                                Bundle args = new Bundle();
                                Fragment fm = new CartFragment();
                                args.putString("type", "buy_now");
                                fm.setArguments(args);
                                androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace( R.id.fragment_container, fm)
                                        .addToBackStack(null).commit();

                            } else if (tr == false) {
                                Toast.makeText( getActivity(), "cart updated", Toast.LENGTH_LONG ).show();

                            }
                            txtTotal.setText( "\u20B9" + String.valueOf( db_buy_now.getTotalAmount() ) );
                            module.showToast("asdfghjk"+db_buy_now.getCartCount() );

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //Toast.makeText(activity, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                    updateData();
                   // btn_add.setText("Update Cart");

                }
            }
        });
//

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm=new CartFragment();


                if (ConnectivityReceiver.isConnected()) {
//                    Intent intent=new Intent(getActivity(), OtpActivity.class);
//                    startActivity(intent);
                    if(db_carts.getCartCount()>0) {

                        makeGetLimiteRequest();
                    }
                    else
                    {
                        fm = new EmptyCartFragment();
                        androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                                .addToBackStack(null).commit();
                        //Toast.makeText(getActivity(),"Cart is Empty",Toast.LENGTH_LONG ).show();

                    }
                }
                else {
                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
//                    FragmentManager fragmentManager=getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel,fm)
//                            .addToBackStack(null)
//                            .commit();



                //}
            }
        });

        return view;
    }

    private void makeGetLimiteRequest() {
        loadingBar.show();

        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_LIMITE_SETTING_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        loadingBar.dismiss();
                        Double total_amount = Double.parseDouble(db_carts.getTotalAmount());


                        try {
                            // Parsing json array response
                            // loop through each json object

                            boolean issmall = false;
                            boolean isbig = false;

                            // arraylist list variable for store data;
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);
                                int value;

                                if (jsonObject.getString("id").equals("1")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount < value) {
                                        issmall = true;
                                        Toast.makeText(getActivity(), "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (jsonObject.getString("id").equals("2")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount > value) {
                                        isbig = true;
                                        Toast.makeText(getActivity(), "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            if (!issmall && !isbig) {
                                if (session_management.isLoggedIn()) {
                                    Bundle args = new Bundle();
                                   // args.putString("buy_now","Y");
                                    args.putString("type","buy_now");
                                    Fragment fm = new DeliveryFragment();
                                    fm.setArguments(args);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                                            .addToBackStack(null).commit();
                                } else {
                                    //Toast.makeText(getActivity(), "Please login or regiter.\ncontinue", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(i);
                                }
                            }

                        } catch (JSONException e) {
                            loadingBar.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    loadingBar.dismiss();
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    private void makeRelatedProductRequest(String cat_id) {
        loadingBar.show();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("rett" + "", response.toString());
                loadingBar.dismiss();

                try {

                    Boolean status = response.getBoolean("responce");

                    if (status) {
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0 ; i< array.length();i++)
                        {
                            JSONObject obj = array.getJSONObject(i);
                            RelatedProductModel model = new RelatedProductModel();
                            model.setProduct_id(obj.getString("product_id"));
                            model.setProduct_name(obj.getString("product_name"));
                            model.setProduct_name_arb(obj.getString("product_name_hindi"));
                            model.setProduct_description_arb(obj.getString("product_description_arb"));
                            model.setCategory_id(obj.getString("category_id"));
                            model.setProduct_description(obj.getString("product_description"));
                            model.setProduct_attribute(obj.getString("product_attribute"));
                            model.setStatus(obj.getString("status"));
                            model.setIn_stock(obj.getString("in_stock"));
                            model.setStock(obj.getString("stock"));
                            model.setUnit_value(obj.getString("unit_value"));
                            model.setUnit(obj.getString("unit"));
                            model.setMrp(obj.getString("mrp"));
                            model.setPrice(obj.getString("price"));
                            model.setProduct_image(obj.getString("product_image"));
                            model.setIncreament(obj.getString("increament"));
                            model.setRewards(obj.getString("rewards"));
                            model.setTitle(obj.getString("title"));

                            if (model.getProduct_id().equals(product_id))
                            {

                            }
                            else
                            {
                                product_modelList.add(model);

                            }
                        }

                        loadingBar.dismiss();
                        adapter_product = new RelatedProductAdapter( getActivity(),product_modelList,product_id);

                        top_selling_recycler.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {

                                loadingBar.dismiss();
                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
                    //   e.printStackTrace();
                    String ex=e.getMessage();




                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void productDetail(String productId)
    {
        loadingBar.show();
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("pid",productId);

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
                            numberButton.setRange(1,Integer.parseInt(stock));

                            tv_details_product_name.setText(list.get(0).getProduct_name());
                            tv_details_product_description.setText(list.get(0).getProduct_description());
                            //tv_details_product_price.setText("\u20B9"+list.get(0).getPrice());
                            //tv_details_product_price.setText("\u20B9"+vlist.get(0).getAttribute_value());
                            //tv_details_product_mrp.setText("\u20B9"+list.get(0).getMrp());
                           // tv_details_product_mrp.setText("\u20B9"+vlist.get(0).getAttribute_mrp());


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

                            int stck = Integer.parseInt(stock);
                            if (stck < 1 || in_stock.equals("0"))
                            {
                                rel_out.setVisibility( View.VISIBLE );
                            }
                            else
                            {
                                rel_out.setVisibility( View.GONE );
                            }

                           // numberButton.setMin(1);
                            numberButton.setRange(1,Integer.parseInt(stock+1));
                            //numberButton.setMax(Integer.parseInt(stock+1));

                            String string = object.getString("product_attribute");
                            JSONArray arr = new JSONArray(string);

                            for (int k=0;k<=arr.length()-1;k++)
                            {
                                JSONObject obj =arr.getJSONObject(k);
                                ProductVariantModel vModel= new ProductVariantModel();
                                vModel.setAttribute_value(obj.getString("attribute_value"));
                                vModel.setAttribute_name(obj.getString("attribute_name"));
                                vModel.setAttribute_mrp(obj.getString("attribute_mrp"));
                                vModel.setAttribute_color(obj.getString("attribute_color"));
                                vModel.setStock_value(obj.getString("stock_value"));
                                vModel.setStatus(obj.getString("status"));
                                vModel.setAttribute_size(obj.getString("attribute_size"));
                                vModel.setId(obj.getString("id"));
                                vlist.add(vModel);

                                atr_id = obj.getString("id");
                                attribute_value=obj.getString("attribute_value");
                                attribute_name=obj.getString("attribute_name");
                                attribute_mrp=obj.getString("attribute_mrp");
                                attribute_color=obj.getString("attribute_color");

                                stock_value=obj.getString("stock_value");
                                status=obj.getString("status");
                                attribute_size=obj.getString("attribute_size");

                               //----------------------------------

                            }
                            String atr=String.valueOf(list.get(i).getProduct_attribute());
                             if (atr.equals("[]"))
                            {
                                    tv_variant.setText("\u20B9"+list.get(0).getPrice()+"/"+list.get(0).getUnit_value()+" "+list.get(0).getUnit());
                                tv_details_product_price.setText("\u20B9"+list.get(0).getPrice());
                                tv_details_product_mrp.setText("\u20B9"+list.get(0).getMrp());
                                //dialog_unit_type.setText("\u20B9"+list.get(0).getPrice()+"/"+list.get(0).getUnit_value()+" "+list.get(0).getUnit());
                            }else {
                                tv_variant.setText("\u20B9" + vlist.get(0).getAttribute_value() + "/" + vlist.get(0).getAttribute_name());

                                 tv_details_product_price.setText("\u20B9"+vlist.get(0).getAttribute_value());
                                 tv_details_product_mrp.setText("\u20B9"+vlist.get(0).getAttribute_mrp());
                                 //dialog_unit_type.setText("\u20B9" + vlist.get(0).getAttribute_value() + "/" + vlist.get(0).getAttribute_name());
                             }

                             atr = String.valueOf( list.get(0).getProduct_attribute() );
                            Log.e("attri",list.get(0).getStock());

                            if (atr.equals( "[]" )) {
                                boolean st=db_carts.isInCart(product_id);
                                if (st == true) {
                                    btn_add.setText("Update Cart");
                                    numberButton.setNumber(db_carts.getCartItemQty(product_id));

                                } else {
                                    btn_add.setText("Add");

                                }
                            }
                            else {
                                Boolean str = db_carts.isInCart(vlist.get(0).getId());
                                if (str == true) {
                                    btn_add.setText("Update Cart");
                                    numberButton.setNumber(db_carts.getCartItemQty(vlist.get(0).getId()));
                                } else {
                                    btn_add.setText("Add");

                                }
                            }

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
//                                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 500);
//                                    imageView.setLayoutParams(layoutParams);
                                    Glide.with(getActivity())
                                            .load(IMG_PRODUCT_URL + image_list.get(position))
                                            .thumbnail(0.9f)
                                            .crossFade()
                                            .into(imageView);
                                }
                            });
                            img_slider.setPageCount(image_list.size());
                            Log.e("image", String.valueOf(image_list.size()));
                            img_slider.setImageClickListener(new ImageClickListener() {
                                @Override
                                public void onClick(int position) {
                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("images",image_list);
                                    Fragment fm = new ImagesViewFragment();
                                    fm.setArguments(bundle);
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                                            .addToBackStack(null).commit();



                                }
                            });
                            loadingBar.dismiss();

//                        }

//                            String msg = response.getString("message");
//                            Toast.makeText(getContext(),"slx,km "+msg,Toast.LENGTH_SHORT).show();
                            makeRelatedProductRequest(list.get(0).getCategory_id());
                        }
                        Log.e("listSize_detail", String.valueOf(list.size()));

                    }

                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace();
                }

                loadingBar.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showToast(module.VolleyErrorMessage(error));
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
    private void getValues(String str) throws JSONException {
        JSONObject json=new JSONObject(str);
        color_list.clear();
        sub_image_list.clear();
        Iterator<String> iterator=json.keys();
        while (iterator.hasNext()){
            String objKey=iterator.next();
            color_list.add(new ColorModel(objKey,false));
            String value=json
                    .getString(objKey);
            sub_image_list.add(value);
            Log.e("DAta",objKey+" : "+value);
        }


    }
    private void setColor() {
        LinearLayoutManager HorizontalLayout;
        Log.e("Colors",color_list.toString());
        colorAdapter = new ColorAdapter(color_list);
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerViewColor.setLayoutManager(HorizontalLayout);
        recyclerViewColor.setAdapter(colorAdapter);

    }
//    private void updateintent() {
//        Intent updates = new Intent("Grocery_cart");
//        updates.putExtra("type", "update");
//        getActivity().sendBroadcast(updates);
//    }
//
//    private void updateintentBuy() {
//        Intent updates = new Intent("Grocery_cart");
//        updates.putExtra("type", "buy_now");
//        getActivity().sendBroadcast(updates);
//    }

    BroadcastReceiver mCart=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type=intent.getStringExtra("type");
            if(type.contentEquals("update")){
                updateData();
            }
        }
    };

    private void updateData() {
        ((MainActivity)getActivity()).setCartCounter(db_carts.getCartCount());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mCart,new IntentFilter("Cart"));
    }


}