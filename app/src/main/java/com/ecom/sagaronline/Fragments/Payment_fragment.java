package com.ecom.sagaronline.Fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.Config.SharedPref;
import com.ecom.sagaronline.NetworkConnector.NetworkConnection;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.BuyNowHandler;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.DatabaseCartHandler;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import static com.android.volley.VolleyLog.TAG;


public class Payment_fragment extends Fragment {
    //RelativeLayout confirm;
    Button confirm;
    Module module;
    private DatabaseCartHandler db_cart;
    private BuyNowHandler db_buy_now;
    private Session_management sessionManagement;
    TextView payble_ammount, my_wallet_ammount, used_wallet_ammount, used_coupon_ammount, order_ammount;
    private String getlocation_id = "";
    private String getstore_id = "";

    private double wamt=0;
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private Double rewards;
    RadioButton rb_Store, rb_Cod, rb_card, rb_Netbanking, rb_paytm;
    CheckBox checkBox_Wallet;
    CheckBox checkBox_coupon;
    EditText et_Coupon;
    String getvalue;
    String text;
    String deli_charges="";
   LoadingBar loadingBar;
    String Used_Wallet_amount , Wallet_amount;
    String total_amount;
    String order_total_amount;
    RadioGroup radioGroup;
    String Prefrence_TotalAmmount;
    String getwallet;
    String getcharge ;
    String type;
    boolean buynow=false;
    LinearLayout Promo_code_layout, Coupon_and_wallet;
    RelativeLayout Apply_Coupon_Code, Relative_used_wallet, Relative_used_coupon;

    public Payment_fragment() {

    }

    public static Payment_fragment newInstance(String param1, String param2) {
       Payment_fragment fragment = new Payment_fragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar = new LoadingBar(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_payment_method, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.payment));



        Prefrence_TotalAmmount = SharedPref.getString(getActivity(), BaseURL.TOTAL_AMOUNT);
        loadingBar = new LoadingBar(getActivity());
        module=new Module(getActivity());

        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                getvalue = radioButton.getText().toString();
            }
        });


//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/bold.ttf" );
        checkBox_Wallet = (CheckBox) view.findViewById(R.id.use_wallet);
        //     checkBox_Wallet.setTypeface(font);
        rb_Store = (RadioButton) view.findViewById(R.id.use_store_pickup);
//        rb_Store.setTypeface(font);
        rb_Cod = (RadioButton) view.findViewById(R.id.use_COD);
        //   rb_Cod.setTypeface(font);
        rb_card = (RadioButton) view.findViewById(R.id.use_card);
        //    rb_card.setTypeface(font);
        rb_Netbanking = (RadioButton) view.findViewById(R.id.use_netbanking);
        //   rb_Netbanking.setTypeface(font);
        rb_paytm = (RadioButton) view.findViewById(R.id.use_wallet_ammount);
        //   rb_paytm.setTypeface(font);
        checkBox_coupon = (CheckBox) view.findViewById(R.id.use_coupon);
        //   checkBox_coupon.setTypeface(font);
        et_Coupon = (EditText) view.findViewById(R.id.et_coupon_code);
        Promo_code_layout = (LinearLayout) view.findViewById(R.id.prommocode_layout);
        Apply_Coupon_Code = (RelativeLayout) view.findViewById(R.id.apply_coupoun_code);

      /*  Apply_Coupon_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coupon_code();

            }
        });*/

        sessionManagement = new Session_management(getActivity());


        Coupon_and_wallet = (LinearLayout) view.findViewById(R.id.coupon_and_wallet);
        Relative_used_wallet = (RelativeLayout) view.findViewById(R.id.relative_used_wallet);
        Relative_used_coupon = (RelativeLayout) view.findViewById(R.id.relative_used_coupon);

        //Show  Wallet
        getwallet = SharedPref.getString(getActivity(), BaseURL.KEY_WALLET_Ammount);
        my_wallet_ammount = (TextView) view.findViewById(R.id.user_wallet);
        // my_wallet_ammount.setText(getwallet+getActivity().getString(R.string.currency));
        db_cart = new DatabaseCartHandler(getActivity());
        db_buy_now =new BuyNowHandler(getActivity());
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
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener()

        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment fm = new HomeFragment();
                 androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });


        total_amount = getArguments().getString("total");
        order_total_amount = getArguments().getString("total");
        getdate = getArguments().getString("getdate");
        gettime = getArguments().getString("gettime");
        getlocation_id = getArguments().getString("getlocationid");
        deli_charges = getArguments().getString("deli_charges");
        getstore_id = getArguments().getString("getstoreid");
        getcharge = getArguments().getString( "deli_charges" );
        payble_ammount = (TextView) view.findViewById(R.id.payable_ammount);
        order_ammount = (TextView) view.findViewById(R.id.order_ammount);
        //  used_wallet_ammount = (TextView) view.findViewById(R.id.used_wallet_ammount);
        // used_coupon_ammount = (TextView) view.findViewById(R.id.used_coupon_ammount);
        payble_ammount.setText(getActivity().getString(R.string.currency)+total_amount);
        order_ammount.setText(getActivity().getString(R.string.currency)+order_total_amount);

        //    Toast.makeText( getActivity(),"charge" +getcharge,Toast.LENGTH_LONG).show();


//
//        CheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked) {
//                   // Use_Wallet_Ammont();
//
//                    //getWalletAmount("18");
//
//                   // Coupon_and_wallet.setVisibility(View.VISIBLE);
//                   // Relative_used_wallet.setVisibility(View.VISIBLE);
//                  /*  if (rb_card.isChecked() || rb_Netbanking.isChecked() || rb_paytm.isChecked()) {
//                        rb_card.setChecked(false);
//                        rb_Netbanking.setChecked(false);
//                        rb_paytm.setChecked(false);
//                    }
//                } else {
//                    if (payble_ammount != null) {
//                        rb_Cod.setText(getResources().getString(R.string.cash));
//                        rb_card.setClickable(true);
//                        rb_card.setTextColor(getResources().getColor(R.color.dark_black));
//                        rb_Netbanking.setClickable(true);
//                        rb_Netbanking.setTextColor(getResources().getColor(R.color.dark_black));
//                        rb_paytm.setClickable(true);
//                        rb_paytm.setTextColor(getResources().getColor(R.color.dark_black));
//
//                        checkBox_coupon.setClickable(true);
//                        checkBox_coupon.setTextColor(getResources().getColor(R.color.dark_black));
//                    }*/
//                    final String Ammount = SharedPref.getString(getActivity(), BaseURL.TOTAL_AMOUNT);
//                    final String WAmmount = SharedPref.getString(getActivity(), BaseURL.KEY_WALLET_Ammount);
//                    my_wallet_ammount.setText(WAmmount+getActivity().getResources().getString(R.string.currency));
//                    payble_ammount.setText(Ammount+getResources().getString(R.string.currency));
////                    used_wallet_ammount.setText("");
//                    Relative_used_wallet.setVisibility(View.GONE);
//                    if (checkBox_coupon.isChecked()) {
//                        final String ammount = SharedPref.getString(getActivity(), BaseURL.COUPON_TOTAL_AMOUNT);
//                        payble_ammount.setText(ammount+getResources().getString(R.string.currency));
//                    }
//                }
//            }
//        });
       /* checkBox_coupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Promo_code_layout.setVisibility(View.VISIBLE);
                    Coupon_and_wallet.setVisibility(View.VISIBLE);
                    Relative_used_coupon.setVisibility(View.VISIBLE);
                    if (rb_Store.isChecked() || rb_Cod.isChecked() || rb_card.isChecked() || rb_Netbanking.isChecked() || rb_paytm.isChecked()) {
                        rb_Store.setChecked(false);
                        rb_Cod.setChecked(false);
                        rb_card.setChecked(false);
                        rb_Netbanking.setChecked(false);
                        rb_paytm.setChecked(false);
                    }
                } else {
                    et_Coupon.setText("");
                    Relative_used_coupon.setVisibility(View.GONE);
                    Promo_code_layout.setVisibility(View.GONE);
                }
            }
        });
*/

        //confirm = (RelativeLayout) view.findViewById(R.id.confirm_order);
        confirm = (Button) view.findViewById(R.id.confirm_order);
        confirm.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {

                    // confirm.setEnabled(false);
                    //  Toast.makeText(getActivity(),"p"+total_amount+"\n o"+order_total_amount,Toast.LENGTH_LONG).show();
//                    if (checkBox_Wallet.isChecked()){
//                        getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//
//                        Usewalletfororder(getuser_id,Used_Wallet_amount);
//                        checked();
//
//                    }
//                    else {
//                        checked();
//
//                    }
//

                    checked();

                } else {
                    confirm.setEnabled(true);

                    ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        // Toast.makeText(getActivity(),""+getuser_id,Toast.LENGTH_LONG).show();
        getWalletAmount(getuser_id);


    }

    private void getWalletAmount(String user_id)
    {
        String json_wallet_tag="json_wallet_tag";
        HashMap<String, String> params=new HashMap<String, String>();
        params.put("user_id",user_id);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST,BaseURL.WALLET_AMOUNT_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    //Toast.makeText(getActivity(),"Something went wrong"+response,Toast.LENGTH_LONG).show();
                    String status=response.getString("status");
                    if(status.equals("success"))
                    {
                        wamt= Double.parseDouble(response.getString("data"));
                    }
                    else if(status.equals("failed"))
                    {
                        wamt= Double.parseDouble(response.getString("data"));
                    }
                    my_wallet_ammount.setText(getActivity().getString(R.string.currency)+" "+wamt);
                }
                catch (Exception ex)
                {
                    // Toast.makeText(getActivity(),"Something went wrong"+ex.getMessage(),Toast.LENGTH_LONG).show();
                }

                // Toast.makeText(getActivity(),"Response :"+response,Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_wallet_tag);
    }

    private void attemptOrder() {
        ArrayList<HashMap<String, String>> items = new ArrayList<>();
        if (buynow){
            items = db_buy_now.getCartAll();
            Log.e("hey", String.valueOf(items));
        }else {
            items = db_cart.getCartAll();
            Log.e("hello", String.valueOf(items));
        }
        //rewards = Double.parseDouble(db_cart.getColumnRewards());
        rewards = Double.parseDouble("0");
        if (items.size() > 0) {
            JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                // String unt=
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("attribute_id", map.get("cart_id"));
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("product_name", map.get("product_name"));
                    jObjP.put("qty", map.get("qty"));
                    jObjP.put("unit_value", map.get("unit_price"));
                    jObjP.put("unit", map.get("unit"));
                    jObjP.put("price", map.get("price"));
//                    jObjP.put("rewards", "0");
                    passArray.put(jObjP);
                    Log.e("passArray",jObjP.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

            if (ConnectivityReceiver.isConnected()) {



                Date date=new Date();

                SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
                String g=dateFormat.format(date);
                SimpleDateFormat dateFormat1=new SimpleDateFormat("hh:mm a");
                String t=dateFormat1.format(date);

                //    gettime=t+" - "+t.toString();
                //   Toast.makeText(getActivity(),"Time"+t,Toast.LENGTH_LONG).show();
                getdate=g;

//                gettime="03:00 PM - 03:30 PM";

                String ctime=new SimpleDateFormat("hh:mm a").format(new Date());
                gettime=ctime+" - "+ctime;
                // getdate="2019-7-23";
//                Log.e(TAG, "from:" +"03:00 PM - 03:30 PM" + "\ndate:" + "2019-7-23" +
//                        "\n" + "\nuser_id:" + getuser_id + "\n l" + getlocation_id + getstore_id + "\ndata:" + passArray.toString());
//Toast.makeText(getActivity(), "from:" + gettime + "\ndate:" + getdate +
                //      "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:" + passArray.toString(),Toast.LENGTH_LONG).show();

                //    Toast.makeText(getActivity(),""+deli_charges,Toast.LENGTH_SHORT).show();
                Log.e("sdcfv",gettime+"  "+getlocation_id);
               // makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id, passArray);

                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, passArray);
//                makeOrderRequest(getdate, gettime, getuser_id, getlocation_id, passArray);
            }
        }
    }

    private void makeOrderRequest(String date, String gettime, String userid, String
            location, JSONArray passArray){
        HashMap<String, String> params = new HashMap<String, String>();
        String cDate=new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        params.put("date", cDate);
        params.put("time", gettime);
        params.put("user_id", userid);
        params.put("location", location);
        params.put( "delivery_charges",getcharge );
        params.put("store_id", "0");
        params.put("total_ammount",total_amount);
        params.put("delivery_charges",deli_charges);
        params.put("payment_method", getvalue);
        params.put("data", String.valueOf(passArray));
        Log.e(TAG, "makeOrderRequest: "+params.toString() );
            module.postRequest(BaseURL.ADD_ORDER_URL, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "onResponse: "+response.toString() );
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
    }

    private void makeAddOrderRequest(String date, String gettime, String userid, String
            location, JSONArray passArray) {

        loadingBar.show();
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", getdate);
        params.put("time", gettime);
        params.put("user_id", userid);
        params.put("location", location);
        params.put( "delivery_charges",getcharge );
        params.put("store_id", "0");
        params.put("total_ammount",total_amount);
        params.put("delivery_charges",deli_charges);
        params.put("payment_method", getvalue);
        params.put("data", String.valueOf(passArray));
        Log.e("PaymentFragementpara",params.toString());
        // Toast.makeText(getActivity(),""+passArray,Toast.LENGTH_LONG).show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("odd", response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        // JSONObject object = response.getJSONObject("data");
//                        ((MainActivity)getActivity()).setCount("0");
                        String msg=response.getString("data");
                        if (buynow){
                            db_buy_now.clearCart();

                        }else {
                            db_cart.clearCart();
                            updateintent();
//                            ((MainActivity)getActivity()).setCount("0");
                        }

                        loadingBar.dismiss();
                        ((MainActivity)getActivity()).setCount("0");
                        Bundle args = new Bundle();
                      Fragment fm = new Thanks_fragment();
                        args.putString("msg", msg);
                        // args.putString("msgarb",msg_arb);
                        fm.setArguments(args);
                      FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                                .addToBackStack(null).commit();

                        //      Toast.makeText(getActivity(),"success",Toast.LENGTH_LONG).show();
                    }
                    else

                    {
                        loadingBar.dismiss();
                        Toast.makeText(getActivity(),"Something went wrong", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    loadingBar.dismiss();
                    Toast.makeText(getActivity(),""+e.getMessage() , Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                error.printStackTrace();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg, Toast.LENGTH_LONG).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void Usewalletfororder(String userid, String Wallet) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("wallet_amount", Wallet);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Wallet_CHECKOUT, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Toast.makeText(getActivity(),""+response, Toast.LENGTH_LONG).show();
                    String status = response.getString("responce");

                } catch (Exception e) {
                    e.printStackTrace();
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void Use_Wallet_Ammont() {
        final String Wallet_Ammount = SharedPref.getString(getActivity(), BaseURL.KEY_WALLET_Ammount);
        final String Coupon_Ammount = SharedPref.getString(getActivity(), BaseURL.COUPON_TOTAL_AMOUNT);
        final String Ammount = SharedPref.getString(getActivity(), BaseURL.TOTAL_AMOUNT);
        if (NetworkConnection.connectionChecking(getActivity())) {
            RequestQueue rq = Volley.newRequestQueue(getActivity());
            StringRequest postReq = new StringRequest(Request.Method.POST, BaseURL.BASE_URL+"index.php/api/wallet_on_checkout",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray Jarray = object.getJSONArray("final_amount");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json_data = Jarray.getJSONObject(i);
                                    Wallet_amount = json_data.getString("wallet");
//                                     Used_Wallet_amount = json_data.getString("used_wallet");
                                    total_amount = json_data.getString("total");


                                  /*  if (total_amount.equals(Wallet_amount)) {
                                        rb_Cod.setText("Home Delivery");
                                        getvalue = rb_Cod.getText().toString();
                                        rb_card.setClickable(false);
                                        rb_card.setTextColor(getResources().getColor(R.color.gray));
                                        rb_Netbanking.setClickable(false);
                                        rb_Netbanking.setTextColor(getResources().getColor(R.color.gray));
                                        rb_paytm.setClickable(false);
                                        rb_paytm.setTextColor(getResources().getColor(R.color.gray));
                                        checkBox_coupon.setClickable(false);
                                        checkBox_coupon.setTextColor(getResources().getColor(R.color.gray));
                                    } else {
                                        if (total_amount != null) {
                                            rb_Cod.setText("Cash On Delivery");
                                            rb_card.setClickable(true);
                                            rb_card.setTextColor(getResources().getColor(R.color.dark_black));
                                            rb_Netbanking.setClickable(true);
                                            rb_Netbanking.setTextColor(getResources().getColor(R.color.dark_black));
                                            rb_paytm.setClickable(true);
                                            rb_paytm.setTextColor(getResources().getColor(R.color.dark_black));
                                            checkBox_coupon.setClickable(true);
                                            checkBox_coupon.setTextColor(getResources().getColor(R.color.dark_black));
                                        }
                                    }*/
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    // used_wallet_ammount.setText("(" + getResources().getString(R.string.currency) + Used_Wallet_amount + ")");
                                    SharedPref.putString(getActivity(), BaseURL.WALLET_TOTAL_AMOUNT, total_amount);
                                    my_wallet_ammount.setText(Wallet_amount+getResources().getString(R.string.currency));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    if (checkBox_Wallet.isChecked()){
                        params.put("wallet_amount", Wallet_Ammount);
                    }else {
                        params.put("total_amount", Ammount);

                    }

                   /* if (checkBox_coupon.isChecked()) {
                        params.put("total_amount", Coupon_Ammount);
                    } else {
                        params.put("total_amount", Ammount);

                    }*/
                    return params;
                }
            };
            rq.add(postReq);
        } else {
            Intent intent = new Intent(getActivity(), NetworkError.class);
            startActivity(intent);
        }
    }

   /* private void Coupon_code() {
        final String Ammount = SharedPref.getString(getActivity(), BaseURL.TOTAL_AMOUNT);
        final String Wallet_Ammount = SharedPref.getString(getActivity(), BaseURL.WALLET_TOTAL_AMOUNT);
        final String Coupon_code = et_Coupon.getText().toString();
        if (NetworkConnection.connectionChecking(getActivity())) {
            RequestQueue rq = Volley.newRequestQueue(getActivity());
            StringRequest postReq = new StringRequest(Request.Method.POST, BaseURL.COUPON_CODE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                total_amount = obj.getString("Total_amount");
                                String Used_coupon_amount = obj.getString("coupon_value");
                                if (obj.optString("responce").equals("true")) {
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    SharedPref.putString(getActivity(), BaseURL.COUPON_TOTAL_AMOUNT, total_amount);
                                    Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                    used_coupon_ammount.setText("(" + getActivity().getResources().getString(R.string.currency) + Used_coupon_amount + ")");
                                    Promo_code_layout.setVisibility(View.GONE);

                                } else {
                                    Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                    et_Coupon.setText("");
                                    used_coupon_ammount.setText("");
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    Promo_code_layout.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("coupon_code", Coupon_code);
                    if (checkBox_Wallet.isChecked()) {
                        params.put("payable_amount", Wallet_Ammount);
                    } else {
                        params.put("payable_amount", Ammount);
                    }
                    return params;
                }
            };
            rq.add(postReq);
        } else {
            Toast.makeText(getActivity(), "Somthing Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }*/


    private void checked() {
        if (checkBox_Wallet.isChecked()) {

            // Toast.makeText(getActivity(),"checkBox_Wallet",Toast.LENGTH_LONG).show();
            double t= Double.parseDouble(total_amount);

            if(wamt>0)
            {
                rb_Cod.setClickable( false );
                rb_Cod.setVisibility( View.INVISIBLE );
                Usewalletfororder(getuser_id, String.valueOf(t));

                String amt = String.valueOf( t );



                attemptOrder();

            }

            else
            {
                Toast.makeText(getActivity(),"You don't have enough wallet amount.\n Please select another option", Toast.LENGTH_LONG).show();
            }



        }
        else if (rb_Store.isChecked()) {
            // Toast.makeText(getActivity(),"rb_Store",Toast.LENGTH_LONG).show();
            attemptOrder();
        }
        else if (rb_Cod.isChecked()) {

            //Toast.makeText(getActivity(),"rb_Cod",Toast.LENGTH_LONG).show();
            attemptOrder();
        }
        else {
            Toast.makeText(getActivity(), "Please Select Payment Method", Toast.LENGTH_SHORT).show();
        }
       /* if (rb_card.isChecked()) {
            Intent myIntent = new Intent(getActivity(), PaymentGatWay.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent.putExtra("total", total_amount);
            } else {
                myIntent.putExtra("total", Prefrence_TotalAmmount);
                myIntent.putExtra("getdate", getdate);
                myIntent.putExtra("gettime", gettime);
                myIntent.putExtra("getlocationid", getlocation_id);
                myIntent.putExtra("getstoreid", getstore_id);
                myIntent.putExtra("getpaymentmethod", getvalue);
            }
            getActivity().startActivity(myIntent);
        }*/
       /* if (rb_Netbanking.isChecked()) {
            Intent myIntent1 = new Intent(getActivity(), PaymentGatWay.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent1.putExtra("total", total_amount);

            } else {
                myIntent1.putExtra("total", Prefrence_TotalAmmount);
                myIntent1.putExtra("getdate", getdate);
                myIntent1.putExtra("gettime", gettime);
                myIntent1.putExtra("getlocationid", getlocation_id);
                myIntent1.putExtra("getstoreid", getstore_id);
                myIntent1.putExtra("getpaymentmethod", getvalue);
            }
            getActivity().startActivity(myIntent1);
        }*/
              /* if (rb_paytm.isChecked()) {
            Intent myIntent1 = new Intent(getActivity(), Paytm.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent1.putExtra("total", total_amount);

            } else {
                myIntent1.putExtra("total", Prefrence_TotalAmmount);
                myIntent1.putExtra("getdate", getdate);
                myIntent1.putExtra("gettime", gettime);
                myIntent1.putExtra("getlocationid", getlocation_id);
                myIntent1.putExtra("getstoreid", getstore_id);
                myIntent1.putExtra("getpaymentmethod", getvalue);
            }
            getActivity().startActivity(myIntent1);

        }*/
       /* if (checkBox_coupon.isChecked()) {
            if (rb_Store.isChecked() || rb_Cod.isChecked()) {
                attemptOrder();
            } else {
                Toast.makeText(getActivity(), "Select Store Or Cod", Toast.LENGTH_SHORT).show();
            }


        }*/



    }
    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        getActivity().sendBroadcast(updates);

    }


}
