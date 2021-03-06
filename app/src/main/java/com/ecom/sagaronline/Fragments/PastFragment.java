package com.ecom.sagaronline.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Adapter.OrderAdapter;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.Model.My_order_model;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.CustomVolleyJsonArrayRequest;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.RecyclerTouchListener;
import com.ecom.sagaronline.Utils.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PastFragment extends Fragment {
    private static String TAG =PastFragment.class.getSimpleName();

    private RecyclerView rv_myorder;
    RelativeLayout rel_no ;
    Module module;
    private List<My_order_model> my_order_modelList = new ArrayList<>();
    TabHost tHost;
    LoadingBar loadingBar ;

    public PastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v =inflater.inflate(R.layout.fragment_past, container, false);
      initView(v);
      return v ;
    }
    void initView(View view)
    {

        module=new Module(getActivity());
        // handle the touch event if true
//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // check user can press back button or not
//                if (event.getAction() == android.view.KeyEvent.ACTION_UP && keyCode == android.view.KeyEvent.KEYCODE_BACK) {
//
////                    Fragment fm = new Home_fragment();
////                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
////                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
////                            .addToBackStack(null).commit();
//                    return true;
//                }
//                return false;
//            }
//        });
        loadingBar = new LoadingBar(getActivity());
        rv_myorder = (RecyclerView) view.findViewById(R.id.rv_myorder);
        rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));
        rel_no = view.findViewById( R.id.rel_no );

        Session_management sessionManagement = new Session_management(getActivity());
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        // check internet connection
        if (ConnectivityReceiver.isConnected())

        {
            makeGetOrderRequest(user_id);
        } else

        {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        // recyclerview item click listener
//        rv_myorder.addOnItemTouchListener(new
//                RecyclerTouchListener(getActivity(), rv_myorder, new RecyclerTouchListener.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(View view, int position) {
//                Bundle args = new Bundle();
//                String sale_id = my_order_modelList.get(position).getSale_id();
//                String date = my_order_modelList.get(position).getOn_date();
//                //String time = my_order_modelList.get(position).getDelivery_time_from() + "-" + my_order_modelList.get(position).getDelivery_time_to();
//                String time = my_order_modelList.get(position).getDelivery_time_from();
//                String total = my_order_modelList.get(position).getTotal_amount();
//                String status = my_order_modelList.get(position).getStatus();
//                String deli_charge = my_order_modelList.get(position).getDelivery_charge();
//                Fragment fm = new OrderDetailsFragment();
//
//                args.putString("sale_id", sale_id);
//                args.putString("date", date);
//                args.putString("time", time);
//                args.putString("total", total);
//                args.putString("status", status);
//                args.putString("deli_charge", deli_charge);
//                fm.setArguments(args);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fm).addToBackStack(null).commit();
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));


    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetOrderRequest(String userid) {
        loadingBar.show();
        String tag_json_obj = "json_socity_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.GET_DELIVERD_ORDER_URL, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());


                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_model>>() {
                }.getType();

                my_order_modelList = gson.fromJson(response.toString(), listType);
                OrderAdapter myPendingOrderAdapter = new OrderAdapter(my_order_modelList);
                rv_myorder.setAdapter(myPendingOrderAdapter);
                myPendingOrderAdapter.notifyDataSetChanged();

                if(response.length()<=0)
                {
                    rel_no.setVisibility(View.VISIBLE);
                    rv_myorder.setVisibility(View.GONE);
                }

                if (my_order_modelList.isEmpty()) {
                    rel_no.setVisibility(View.VISIBLE);
                    rv_myorder.setVisibility(View.GONE);
                    // Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }

                if (my_order_modelList.isEmpty()) {
                    rel_no.setVisibility(View.VISIBLE);
                    rv_myorder.setVisibility(View.GONE);
                    //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                Module module=new Module(getActivity());
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_SHORT).show();
                }
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}