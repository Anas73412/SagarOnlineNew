package com.ecom.sagaronline.Fragments;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ecom.sagaronline.Activity.LoginActivity;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Adapter.CartAdapter;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.BuyNowHandler;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.DatabaseCartHandler;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.Session_management;
import com.google.gson.internal.$Gson$Types;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ecom.sagaronline.Config.BaseURL.KEY_ID;


public class CartFragment extends Fragment implements View.OnClickListener{

    String user_id="";
    private static String TAG = CartFragment.class.getSimpleName();
    Module module;
    public static RecyclerView rv_cart;
    public static TextView tv_clear, tv_total, tv_item;
    public static  Button btn_checkout;
    public static LinearLayout linear_empty ,linear_cart ;

    //  private DatabaseHandler db;
    private DatabaseCartHandler db_cart;
    private static BuyNowHandler db_buy_now;
    private Session_management sessionManagement;
    public static TextView tvDiscount , tvDelivary ,tvSubTotal ,tvMrp ;
    String deli_charge ;
    LoadingBar loadingBar ;
    ScrollView cart_scroll ;
    String database="";
    String type;
    CardView card_cart;
    public static RelativeLayout rel_out ;
    boolean buynow=false;
   // boolean cart =false;
    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar = new LoadingBar(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ((MainActivity) getActivity()).setTitle("My Cart");

        module = new Module(getActivity());
        loadingBar = new LoadingBar(getActivity());
        sessionManagement = new Session_management(getActivity());
        sessionManagement.cleardatetime();

        user_id = sessionManagement.getUserDetails().get(KEY_ID);
        card_cart= view.findViewById(R.id.card_cart);
        cart_scroll = view.findViewById( R.id.cart_scroll );
        rel_out = view.findViewById( R.id.rel_no );
        tv_clear = (TextView) view.findViewById(R.id.tv_cart_clear);
        // tv_total = (TextView) view.findViewById(R.id.tv_cart_total);
        tv_item = (TextView) view.findViewById(R.id.tv_cart_item);
        tvDiscount=(TextView)view.findViewById( R.id.totaldisc );
        tvMrp =(TextView)view.findViewById( R.id.totalmrp);
        tvDelivary=(TextView)view.findViewById( R.id.delivery_charge );
        tvSubTotal =(TextView)view.findViewById( R.id.subtotal ) ;
        btn_checkout =view.findViewById(R.id.btn_cart_checkout);
        rv_cart = (RecyclerView) view.findViewById(R.id.rv_cart);
        //rv_cart.setNestedScrollingEnabled(false);
        linear_cart = (LinearLayout)view.findViewById(R.id.lin_cart);
        linear_empty =view.findViewById(R.id.linear_empty);

        rv_cart.setLayoutManager(new GridLayoutManager(getActivity(),1));

        //db = new DatabaseHandler(getActivity());
        db_cart=new DatabaseCartHandler(getActivity());
        db_buy_now= new BuyNowHandler(getActivity());
//    deli_charge = getArguments().getString("deli_charges");
        // int deli_charges = Integer.parseInt( deli_charge);

        Bundle bundle = getArguments();
        if (bundle!=null){
            type = bundle.getString("type");
        }

        if (!module.checkNullCondition(type))
        {
            buynow=true;
        }
        else {
             buynow=false;
        }

        int items =0;
        if(buynow){
           items= db_buy_now.getCartCount();
           card_cart.setVisibility(View.GONE);
        }else{
            items= db_cart.getCartCount();
        }

        if (items== 0)
        {
            linear_empty.setVisibility(View.VISIBLE);
            rv_cart.setVisibility(View.GONE);
            linear_cart.setVisibility(View.GONE);
            btn_checkout.setVisibility(View.GONE);

        }
        // int deli_charges = 10*items;
        String mrp= getTotMRp();
        String price="";
        if(buynow){
            price = String.valueOf(db_buy_now.getTotalAmount());
            card_cart.setVisibility(View.GONE);
        }else{
            price = String.valueOf(db_cart.getTotalAmount());
        }


        tvMrp.setText(getResources().getString(R.string.currency)+mrp);
        double m= Double.parseDouble(mrp);
        double p= Double.parseDouble(price);
        double d=m-p;
        double db = m-d ;
        tvDiscount.setText("-"+getResources().getString(R.string.currency)+ String.valueOf(d));
        // double db = (m-d)+deli_charges ;
        // tvDelivary.setText(getResources().getString(R.string.currency)+deli_charges);
        tvSubTotal.setText(getResources().getString(R.string.currency)+db);



        ArrayList<HashMap<String, String>> map=new ArrayList<>();
        Log.e(TAG, "onCreateView: "+db_buy_now.getCartCount()+" - "+buynow );
        if (buynow)
        {
           map = db_buy_now.getCartAll();
            card_cart.setVisibility(View.GONE);
        }
        else {
           map = db_cart.getCartAll();
        }
//        final HashMap<String, String> map1 = map.get(0);
        Log.d("cart all ",""+map.get(0).toString());

        if (buynow)
        {
            CartAdapter adapter = new CartAdapter( map,getActivity(),true);
            rv_cart.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }else {
            CartAdapter adapter = new CartAdapter( map,getActivity());

            rv_cart.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }





        updateData();


        tv_clear.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_cart_clear) {
            // showdialog
            // Toast.makeText(getActivity(),""+db_cart.getCartCount(),Toast.LENGTH_LONG).show();
            showClearDialog();
        } else if (id == R.id.btn_cart_checkout) {

            if (ConnectivityReceiver.isConnected()) {

                if(buynow){
                    if (db_buy_now.getCartCount()>0)
                    {
                        makeGetLimiteRequest();
                    }
                    else
                    {
                        Toast.makeText( getActivity(),"Cart is Empty", Toast.LENGTH_LONG ).show();
                    }

                }else{
                    if (db_cart.getCartCount()>0)
                    {
                        makeGetLimiteRequest();
                    }
                    else
                    {
                        Toast.makeText( getActivity(),"Cart is Empty", Toast.LENGTH_LONG ).show();
                    }
                }


            }
            else {
                ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
            }
            module.preventMultipleClick(btn_checkout);
        }
    }

    // update UI
    private void updateData() {
        // tv_total.setText(getActivity().getString(R.string.currency)+ db_cart.getTotalAmount());
        if(buynow){
            tv_item.setText("" + db_buy_now.getCartCount());
            ((MainActivity) getActivity()).setCartCounter(db_buy_now.getCartCount());
            card_cart.setVisibility(View.GONE);
        }else{
            tv_item.setText("" + db_cart.getCartCount());
            ((MainActivity) getActivity()).setCartCounter(db_cart.getCartCount());
        }
    }

    private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Are You Sure to Clear Cart?");
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
                if(buynow){
                    db_buy_now.clearCart();
                    card_cart.setVisibility(View.GONE);
                }else{
                    db_cart.clearCart();
                    card_cart.setVisibility(View.GONE);
                }

                ArrayList<HashMap<String, String>> map;
                if(buynow){
                    map = db_buy_now.getCartAll();
                    card_cart.setVisibility(View.GONE);
                }else{
                    map = db_cart.getCartAll();
                }
                CartAdapter adapter = new CartAdapter( map,getActivity());
                rv_cart.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                updateData();
                linear_cart.setVisibility( View.GONE );
                btn_checkout.setVisibility( View.GONE );
                linear_empty.setVisibility( View.VISIBLE );
                dialogInterface.dismiss();



            }
        });

        alertDialog.show();
    }

    private void makeGetLimiteRequest() {

        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_LIMITE_SETTING_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        Double total_amount =0.0;
                        if(buynow){
                            total_amount = Double.parseDouble(db_buy_now.getTotalAmount());
                            card_cart.setVisibility(View.GONE);
                        }else{
                            total_amount = Double.parseDouble(db_cart.getTotalAmount());

                        }
                    Log.e("db_cart_total", String.valueOf(db_cart.getTotalAmount()));

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
                                   Fragment fm = new DeliveryFragment();
                                   if (buynow){
                                       args.putString("type","buy_now");
                                   }else {
                                       //args.putString("type","cart");
                                   }
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
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg, Toast.LENGTH_LONG).show();
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
        getActivity().registerReceiver(mCart, new IntentFilter("Cart"));
    }
    public String getTotMRp()
    {
        ArrayList<HashMap<String, String>> list;

        if(buynow){
            list = db_buy_now.getCartAll();
            card_cart.setVisibility(View.GONE);
        }else{
            list = db_cart.getCartAll();
        }

        float sum=0;
        for(int i=0;i<list.size();i++)
        {
            final HashMap<String, String> map = list.get(i);

            float q= Float.parseFloat(map.get("qty"));
            float m= Float.parseFloat(map.get("mrp"));

            sum=sum+(q*m);
            //   Toast.makeText(getActivity(),""+q+"\n"+m,Toast.LENGTH_LONG).show();

        }
        if(sum!=0)
        {
            return String.valueOf(sum);
        }
        else
            return "0";
        //Toast.makeText(getActivity(),""+sum,Toast.LENGTH_LONG).show();
    }
    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };



}
