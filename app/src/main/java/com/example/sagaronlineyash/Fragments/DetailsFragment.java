package com.example.sagaronlineyash.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.example.sagaronlineyash.Activity.LoginActivity;
import com.example.sagaronlineyash.Activity.MainActivity;
import com.example.sagaronlineyash.Adapter.ColorAdapter;
import com.example.sagaronlineyash.Adapter.ProductVariantAdapter;
import com.example.sagaronlineyash.Adapter.RelatedProductAdapter;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.Model.ColorModel;
import com.example.sagaronlineyash.Model.ProductVariantModel;
import com.example.sagaronlineyash.Model.RelatedProductModel;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.ConnectivityReceiver;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;
import com.example.sagaronlineyash.Utils.DatabaseCartHandler;
import com.example.sagaronlineyash.Utils.LoadingBar;
import com.example.sagaronlineyash.Utils.RecyclerTouchListener;
import com.example.sagaronlineyash.Utils.Session_management;
import com.example.sagaronlineyash.Utils.WishlistHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.synnapps.carouselview.CarouselView;
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

import static com.example.sagaronlineyash.Config.BaseURL.IMG_PRODUCT_URL;
import static com.example.sagaronlineyash.Config.BaseURL.KEY_ID;

public class DetailsFragment extends android.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String atr_id="";
    String atr_product_id="";
    String attribute_name="";
    String attribute_value="";
    String attribute_mrp="";
    String attribute_color = "";
    String attribute_size="";
    Context context;
    Button btn_add;
    Module module;
    String mVerificationId;
    ArrayList<ProductVariantModel> variantList;
    ProductVariantAdapter productVariantAdapter;
    ColorAdapter colorAdapter;
    //SizeAdapter sizeAdapter;
    String user_id;
    Session_management sessionManagement;

    private static String TAG = DetailsFragment.class.getSimpleName();
    private RecyclerView rv_cat;
    int index;
    double tot_amt=0;
    ColorModel colorModel;
    //SizeModel sizeModel;
    LoadingBar loadingBar;
    double tot=0;

    RelativeLayout rel_variant,lin_img;
    private List<RelatedProductModel> product_modelList = new ArrayList<>();
    private RelatedProductAdapter adapter_product;
    Activity activity;
    Button btn_add_to_cart;
    DatabaseCartHandler db_cart;
    WishlistHandler db_wish ;
    //TextView txtColor,txtSize;
    TextView txtPer;
    Button btn_buy_now;
    int status=0;
    CarouselView img_slider ;
    private TextView dialog_unit_type,dialog_txtId,dialog_txtVar;
    Dialog dialog;

    public static ProgressBar progressBar,pgb,pbg1;
    RelativeLayout relativeLayout_spinner,relativeLayout_size,relativeLayout_color;
    //Produccts_images_adapter imagesAdapter;
    String cat_id,product_id,product_images,details_product_name,details_product_desc,details_product_inStock,details_product_attribute;
    String details_product_price,details_product_mrp,details_product_unit_value,details_product_unit,details_product_rewards,details_product_increament,details_product_title;
    String details_stock;

    public static ImageView btn,img2;
    private TextView txtrate,txtTotal,txtBack;
    ImageView wish_before ,wish_after ;
    RelativeLayout rel_out ;
    ListView listView,listView1;
    String colorSelected="";
    String sizeSelected = "";
    public static ArrayList<String> image_list,sub_image_list;
    ArrayList<ColorModel> color_list;
    //ArrayList<SizeModel> size_list;

    private TextView txtName,txtPrice,txtMrp;
    ReadMoreTextView txtDesc;
    RecyclerView  recyclerViewColor , recyclerViewSize;
    CardView cardView;
    //ImageAdapter pagerAdapter ;


    private NumberPicker numberButton;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_details, container, false);

        sessionManagement = new Session_management(getActivity());

        user_id=sessionManagement.getUserDetails().get(KEY_ID);

        sessionManagement.cleardatetime();
        loadingBar = new LoadingBar(getActivity());

        rv_cat = (RecyclerView) view.findViewById(R.id.top_selling_recycler);
        // gifImageView=(ImageView) view.findViewById(R.id.gifImageView);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv_cat.setLayoutManager(new GridLayoutManager(getActivity(),2));
        module=new Module(getActivity());
        db_cart=new DatabaseCartHandler(getActivity());
        db_wish=new WishlistHandler( getActivity() );
        Bundle bundle=getArguments();
        variantList=new ArrayList<>();

        sub_image_list = new ArrayList<>();
        cat_id=bundle.getString("cat_id");
        product_id=bundle.getString("product_id");
        product_images=bundle.getString("product_images");
        details_product_name=bundle.getString("product_name");
        details_product_desc=bundle.getString("product_description");
        //     details_product_color=bundle.getString("product_color");
        details_product_inStock=bundle.getString("in_stock");
        details_stock=bundle.getString("stock");
        details_product_attribute=bundle.getString("product_attribute");
        Log.e("Attribute",details_product_attribute);
        details_product_price=bundle.getString("price");
        details_product_mrp=bundle.getString("mrp");
        details_product_unit_value=bundle.getString("unit_value");
        details_product_unit=bundle.getString("unit");
        details_product_rewards=bundle.getString("rewards");
        details_product_increament=bundle.getString("increment");
        details_product_title=bundle.getString("title");

        ((MainActivity) getActivity()).setTitle(details_product_name);
        btn_add=(Button)view.findViewById(R.id.btn_add);
        btn_buy_now = view.findViewById(R.id.btn_buy_now);
        dialog_unit_type=(TextView)view.findViewById(R.id.unit_type);
        dialog_txtId=(TextView)view.findViewById(R.id.txtId);
        dialog_txtVar=(TextView)view.findViewById(R.id.txtVar);
        btn_add_to_cart=(Button)view.findViewById(R.id.btn_f_Add_to_cart);
        txtPer=(TextView)view.findViewById(R.id.details_product_per);
        rel_variant=(RelativeLayout)view.findViewById(R.id.rel_variant);
        //recyclerView=view.findViewById(R.id.recylerView);
        wish_before = view.findViewById( R.id.wish_before );
        wish_after = view.findViewById( R.id.wish_after );
        rel_out= view.findViewById( R.id.rel_out );
        lin_img = view.findViewById(R.id.relative_layout_img);
        img_slider = view.findViewById(R.id.img_slider);
        image_list=new ArrayList<String>();
        color_list=new ArrayList<>();
        txtName=(TextView)view.findViewById(R.id.details_product_name);
        txtDesc=(ReadMoreTextView)view.findViewById(R.id.details_product_description);
        txtPrice=(TextView)view.findViewById(R.id.details_product_price);
        txtMrp=(TextView)view.findViewById(R.id.details_product_mrp);
        txtMrp.setPaintFlags(txtMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtrate=(TextView)view.findViewById(R.id.product_rate);
        txtTotal=(TextView)view.findViewById(R.id.product_total);
        numberButton=view.findViewById(R.id.product_qty);
        recyclerViewColor = view.findViewById(R.id.rec_color);
        recyclerViewSize = view.findViewById(R.id.rec_size);
        txtDesc.setText(details_product_desc);
        float mrp=Float.parseFloat(details_product_mrp);
        final float price=Float.parseFloat(details_product_price);
        float per=((mrp-price)/mrp)*100;
        int df=Math.round(per);

        txtName.setText(details_product_name);


        final float stock = Float.parseFloat( details_stock );

        numberButton.setMin(1);
        numberButton.setMax((int) (stock+1));
        if (stock<1 || details_product_inStock.equals("0"))
        {
            rel_out.setVisibility( View.VISIBLE );
        }
        else
        {
            rel_out.setVisibility( View.GONE );
        }

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


        String atr=String.valueOf(details_product_attribute);
        if(atr.equals("[]"))
        {
            if(df<=0)
            {
                txtPer.setVisibility(View.GONE);
            }
            else
            {
                txtPer.setVisibility(View.VISIBLE);

                txtPer.setText(String.valueOf(df)+"% "+" off");

            }

            status=1;
            txtPrice.setText("\u20B9"+details_product_price);
            txtMrp.setText("\u20B9"+details_product_mrp);

            txtrate.setVisibility(View.VISIBLE);
            //  Toast.makeText(getActivity(),""+atr,Toast.LENGTH_LONG).show();
            txtrate.setText(details_product_unit_value+details_product_unit);
            txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
        }

        else
        {

            rel_variant.setVisibility(View.VISIBLE);
            status=2;
            JSONArray jsonArr = null;
            try {

                jsonArr = new JSONArray(atr);

                ProductVariantModel model=new ProductVariantModel();
                JSONObject jsonObj = jsonArr.getJSONObject(0);
                atr_id=jsonObj.getString("id");
                product_id=jsonObj.getString("product_id");
                attribute_name=jsonObj.getString("attribute_name");
                attribute_value=jsonObj.getString("attribute_value");
                attribute_mrp=jsonObj.getString("attribute_mrp");
                attribute_color=jsonObj.getString("attribute_color");
                getValues(attribute_color);
                setColor();
                String atr_price=String.valueOf(attribute_value);
                String atr_mrp=String.valueOf(attribute_mrp);
                int atr_dis=getDiscount(atr_price,atr_mrp);
                txtPrice.setText("\u20B9"+attribute_value.toString());
                txtMrp.setText("\u20B9"+attribute_mrp.toString());
                dialog_txtId.setText(String.valueOf(atr_id.toString()+"@"+"0"));
                dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                //  holder.txtTotal.setText("\u20B9"+String.valueOf(list_atr_value.get(0).toString()));
                if(atr_dis<=0)
                {
                    txtPer.setVisibility(View.GONE);
                }
                else
                {
                    txtPer.setVisibility(View.VISIBLE);
                    txtPer.setText(""+atr_dis+"%"+" OFF");
                }

            } catch (JSONException e) {
                Log.e("Error1",e.toString());
            }
        }


        rel_variant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row=layoutInflater.inflate(R.layout.dialog_vairant_layout,null);
                variantList.clear();
                String atr=String.valueOf(details_product_attribute);
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

                        atr_id=String.valueOf(variantList.get(i).getId());
                        atr_product_id=String.valueOf(variantList.get(i).getProduct_id());
                        attribute_name=String.valueOf(variantList.get(i).getAttribute_name());
                        attribute_value=String.valueOf(variantList.get(i).getAttribute_value());
                        attribute_mrp=String.valueOf(variantList.get(i).getAttribute_mrp());
                        attribute_color=String.valueOf(variantList.get(i).getAttribute_color());
                        JSONArray colorarray= null;
                        try {
                            getValues(attribute_color);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setColor();
                        dialog_unit_type.setText("\u20B9"+attribute_value+"/"+attribute_name);
                        dialog_txtId.setText(variantList.get(i).getId()+"@"+i);
                        dialog_txtVar.setText(attribute_value+"@"+attribute_name+"@"+attribute_mrp);
                        //    txtPer.setText(String.valueOf(df)+"% off");

                        txtPrice.setText("\u20B9"+attribute_value.toString());
                        txtMrp.setText("\u20B9"+attribute_mrp.toString());
                        String pr=String.valueOf(attribute_value);
                        String mr=String.valueOf(attribute_mrp);
                        int atr_dis=getDiscount(pr,mr);
                        txtPer.setText(""+atr_dis+"%"+" OFF");
                        String atr=String.valueOf(details_product_attribute);
                        if(atr.equals("[]"))
                        {
                            boolean st=db_cart.isInCart(product_id);
                            if(st==true)
                            {
                                numberButton.setValue(Integer.parseInt(db_cart.getCartItemQty(product_id)));
                            }
                        }
                        else
                        {
                            String str_id=dialog_txtId.getText().toString();
                            String[] str=str_id.split("@");
                            String at_id=String.valueOf(str[0]);
                            boolean st=db_cart.isInCart(at_id);
                            if(st==true)
                            {
                                numberButton.setValue(Integer.parseInt(db_cart.getCartItemQty(at_id)));
                            }
                            else
                            {
//                                btn_add.setVisibility(View.VISIBLE);
//
//                                numberButton.setVisibility(View.GONE);
                            }
                        }


                        txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));
                        ddlg.dismiss();
                    }
                });

            }
        });
        wish_before.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    if (stock < 1 || details_product_inStock.equals("0")) {
                        Toast.makeText( getActivity(), "Out Of Stock", Toast.LENGTH_LONG ).show();
                    } else {
                        wish_after.setVisibility( View.VISIBLE );
                        wish_before.setVisibility( View.INVISIBLE );
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        mapProduct.put( "product_id",product_id );
                        mapProduct.put( "product_images",product_images );
                        mapProduct.put( "cat_id", cat_id );
                        mapProduct.put( "product_name",details_product_name );
                        mapProduct.put( "price",details_product_price );
                        mapProduct.put( "product_description", details_product_desc );
                        mapProduct.put( "rewards",details_product_rewards);
                        mapProduct.put("user_id",user_id.toString());
                        mapProduct.put( "unit_value", details_product_unit_value );
                        mapProduct.put( "unit", details_product_unit );
                        mapProduct.put( "increment",details_product_increament );
                        mapProduct.put( "stock",details_stock );
                        mapProduct.put( "title", details_product_title );
                        mapProduct.put( "mrp", details_product_mrp );
                        mapProduct.put( "product_attribute",details_product_attribute );
                        mapProduct.put( "in_stock", details_product_inStock );
                        try {

                            boolean tr = db_wish.setwishTable( mapProduct );
                            if (tr == true) {

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText( getActivity(), "Added to Wishlist", Toast.LENGTH_LONG ).show();


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
        } );
        wish_after.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wish_after.setVisibility( View.INVISIBLE );
                wish_before.setVisibility( View.VISIBLE );
                db_wish.removeItemFromWishtable(product_id,user_id);
                Toast.makeText(getActivity(), "removed from Wishlist" +db_wish.getWishtableCount(user_id), Toast.LENGTH_LONG).show();
                // list.remove(position);
                // notifyDataSetChanged();
            }
        } );

//       if (details_product_color.isEmpty())
//       {
//           txtColor.setVisibility( View.GONE );
//       }
//       if (details_product_color.equals( "null" ))
//       {
//           txtColor.setVisibility( View.GONE );
//       }
//       if(details_product_size.isEmpty())
//       {
//           txtSize.setVisibility( View.GONE );
//       }
//       if(details_product_size.equals( "null" ))
//       {
//           txtSize.setVisibility( View.GONE );
//       }

//if (txtSize.getVisibility()== View.VISIBLE) {
//    txtSize.setOnClickListener( new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            dialog = new Dialog( getActivity() );
//            dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
//            dialog.setContentView( R.layout.dialog_product_size_layout );
//
//            listView = (ListView) dialog.findViewById( R.id.list_view_size );
//            pgb = (ProgressBar) dialog.findViewById( R.id.pgb );
//            //final List lis=makeGetProductColorRequest(cat_id,product_id,listView1,pbg1,dialog);
//            final List lis = makeGetProductRequest( cat_id, product_id, listView, pgb, dialog );
//
//
//            dialog.setCanceledOnTouchOutside( false );
//            dialog.show();
//
//            listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    txtSize.setText( lis.get( i ).toString() );
//                    dialog.dismiss();
//
//
//                }
//            } );
//
//        }
//    } );
//}


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stock < 1 || details_product_inStock.equals("0")) {
                    Toast.makeText( getActivity(), "Out Of Stock", Toast.LENGTH_LONG ).show();
                } else {

                    String tot1 = txtTotal.getText().toString().trim();

                    String tot_amount = tot1.substring( 1, tot1.length() );
                    float qty = numberButton.getValue() ;

                    String atr = String.valueOf( details_product_attribute );
                    if (atr.equals( "[]" )) {
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf( details_product_unit_value + " " + details_product_unit );
                        mapProduct.put( "cart_id", product_id );
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "product_image", product_images );
                        mapProduct.put( "cat_id", cat_id );
                        mapProduct.put( "product_name", details_product_name );
                        mapProduct.put( "price", details_product_price );
                        mapProduct.put( "unit_price", details_product_price );
                        mapProduct.put( "color",colorSelected);
                        mapProduct.put( "stock", details_stock );
                        mapProduct.put( "unit", unt );
                        mapProduct.put( "mrp", details_product_mrp );
                        mapProduct.put( "type", "p" );
                        try {

                            boolean tr = db_cart.setCart( mapProduct, qty );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                                //mainActivity.setCartCounter( "" + db_cart.getCartCount() );

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText( getActivity(), "Added to Cart", Toast.LENGTH_LONG ).show();
                                int n = db_cart.getCartCount();
                                updateintent();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );

                            } else if (tr == false) {
                                Toast.makeText( getActivity(), "cart updated", Toast.LENGTH_LONG ).show();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //Toast.makeText(context,"1\n"+status+"\n"+modelList.get(position).getProduct_attribute(),Toast.LENGTH_LONG).show();
                    } else {
                        //ProductVariantModel model=variantList.get(position);

                        String str_id = dialog_txtId.getText().toString();
                        String s = dialog_txtVar.getText().toString();
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
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "product_image", product_images );
                        mapProduct.put( "cat_id", cat_id );
                        mapProduct.put( "color",colorSelected);
                        mapProduct.put( "product_name", details_product_name );
                        mapProduct.put( "price", st0 );
                        mapProduct.put( "unit_price", st0 );
                        mapProduct.put( "stock", details_stock );
                        mapProduct.put( "unit", st1 );
                        mapProduct.put( "mrp", st2 );
                        mapProduct.put( "type", "a" );
                        //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                        try {

                            boolean tr = db_cart.setCart( mapProduct, qty );
                            if (tr == true) {
                                MainActivity mainActivity = new MainActivity();
                               // mainActivity.setCartCounter( "" + db_cart.getCartCount() );

                                //   context.setCartCounter("" + holder.db_cart.getCartCount());
                                Toast.makeText( getActivity(), "Added to Cart", Toast.LENGTH_LONG ).show();
                                int n = db_cart.getCartCount();
                                updateintent();

                                txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );
                            } else if (tr == false) {
                                Toast.makeText( getActivity(), "cart updated", Toast.LENGTH_LONG ).show();
                                txtTotal.setText( "\u20B9" + String.valueOf( db_cart.getTotalAmount() ) );
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
        recyclerViewColor.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewColor, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getSelectedColor(position);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stock < 1 || details_product_inStock.equals("0")) {
                    Toast.makeText( getActivity(), "Out Of Stock", Toast.LENGTH_LONG ).show();
                } else {

                    db_cart.clearSingleCart();
                    String tot1 = txtTotal.getText().toString().trim();

                    String tot_amount = tot1.substring( 1, tot1.length() );
                    float qty = numberButton.getValue() ;

                    String atr = String.valueOf( details_product_attribute );
                    if (atr.equals( "[]" )) {
                        HashMap<String, String> mapProduct = new HashMap<String, String>();
                        String unt = String.valueOf( details_product_unit_value + " " + details_product_unit );
                        mapProduct.put( "cart_id", product_id );
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "product_image", product_images );
                        mapProduct.put( "cat_id", cat_id );
                        mapProduct.put( "color",colorSelected);
                        mapProduct.put( "product_name", details_product_name );
                        mapProduct.put( "price", details_product_price );
                        mapProduct.put( "unit_price", details_product_price );
                        mapProduct.put( "stock", details_stock );
                        mapProduct.put( "unit", unt );
                        mapProduct.put( "mrp", details_product_mrp );
                        mapProduct.put( "type", "p" );
                        try {

                            boolean tr = db_cart.setSingleCart( mapProduct, qty );
                            Log.e("tr",String.valueOf(tr));
                            if (tr == true) {

                            } else if (tr == false) {

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(getActivity(), "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String str_id = dialog_txtId.getText().toString();
                        String s = dialog_txtVar.getText().toString();
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
                        mapProduct.put( "product_id", product_id );
                        mapProduct.put( "color",colorSelected);
                        mapProduct.put( "product_image", product_images );
                        mapProduct.put( "cat_id", cat_id );
                        mapProduct.put( "product_name", details_product_name );
                        mapProduct.put( "price", st0 );
                        mapProduct.put( "unit_price", st0 );
                        mapProduct.put( "stock", details_stock );
                        mapProduct.put( "unit", st1 );
                        mapProduct.put( "mrp", st2 );
                        mapProduct.put( "type", "a" );
                        //  Toast.makeText(context,""+attributeList.get(j).getId()+"\n"+mapProduct,Toast.LENGTH_LONG).show();
                        try {

                            Log.e("DATA",db_cart.getSingleCartAll().toString());
                            boolean tr = db_cart.setSingleCart( mapProduct, qty );
                            Log.e("DATA",db_cart.getSingleCartAll().toString());
                            Log.e("tr",String.valueOf(tr));
                            if (tr == true) {

                            } else if (tr == false) {

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(activity, "" + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                    updateData();
                    if (ConnectivityReceiver.isConnected()) {
                        makeGetLimiteRequest();
                    }


                }
            }
        });




        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.Fragment fm=new CartFragment();


                if (ConnectivityReceiver.isConnected()) {
//                    Intent intent=new Intent(getActivity(), OtpActivity.class);
//                    startActivity(intent);
                    if(db_cart.getCartCount()>0) {

                        makeGetLimiteRequest();
                    }
                    else
                    {
                        fm = new EmptyCartFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
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

    /*private void setSize() {
        LinearLayoutManager HorizontalLayout;
        sizeAdapter = new SizeAdapter(size_list);
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerViewSize.setLayoutManager(HorizontalLayout);
        recyclerViewSize.setAdapter(sizeAdapter);
    }*/

   /* private void getSelectedSize(int position) {
        size_list.get(position).setSelected(true);
        sizeSelected = size_list.get(position).getSize();
        for(int i=0;i<size_list.size();i++)
        {
            if(i==position)
                continue;
            size_list.get(i).setSelected(false);
            sizeAdapter.notifyDataSetChanged();
        }
    }*/

    private void getSelectedColor(int position) {
        color_list.get(position).setSelected(true);
        colorSelected = color_list.get(position).getColor();
        for(int i=0;i<color_list.size();i++)
        {
            if(i==position)
                continue;
            color_list.get(i).setSelected(false);
            colorAdapter.notifyDataSetChanged();
        }
        product_images = sub_image_list.get(position);
        getImages();
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

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        getActivity().sendBroadcast(updates);
    }
    @Override
    public void onStart() {
        super.onStart();


        txtTotal.setText("\u20B9"+String.valueOf(db_cart.getTotalAmount()));

        String atr=String.valueOf(details_product_attribute);
        makeRelatedProductRequest(cat_id);
        getImages();
    }

    private void getImages()
    {
        try
        {
            image_list.clear();
            JSONArray array=new JSONArray(product_images);
            //Toast.makeText(this,""+product_images,Toast.LENGTH_LONG).show();
            if(product_images.equals(null))
            {
                Toast.makeText(getActivity(),"There is no image for this product",Toast.LENGTH_LONG).show();
            }
            else
            {
                for(int i=0; i<=array.length()-1;i++)
                {
                    HashMap<String, String> img_map= new HashMap<>();

                    image_list.add(array.get(i).toString());

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


            }

        }
        catch (Exception ex)
        {
            // Toast.makeText(Product_Frag_details.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
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
                Log.d("rett" +
                        "", response.toString());

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
                            model.setProduct_description("product_description");
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
                        ///         Toast.makeText(getActivity(),""+response.getString("data"),Toast.LENGTH_LONG).show();
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<RelatedProductModel>>() {
//                        }.getType();
//                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        loadingBar.dismiss();
                        adapter_product = new RelatedProductAdapter( getActivity(),product_modelList,product_id);

                        rv_cat.setAdapter(adapter_product);
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

    public int getDiscount(String price, String mrp)
    {
        double mrp_d=Double.parseDouble(mrp);
        double price_d=Double.parseDouble(price);
        double per=((mrp_d-price_d)/mrp_d)*100;
        double df=Math.round(per);
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


    private void makeGetLimiteRequest() {

        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_LIMITE_SETTING_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        Double total_amount = Double.parseDouble(db_cart.getTotalAmount());


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
                                if (sessionManagement.isLoggedIn()) {
                                    Bundle args = new Bundle();
                                    args.putString("buy_now","Y");
                                    android.app.Fragment fm = new DeliveryFragment();
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
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }


    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }


    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    public void updateData()
    {
        //((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());
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
    //

}