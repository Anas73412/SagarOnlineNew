package com.ecom.sagaronline.Fragments;

import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Activity.NewAddressActivity;
import com.ecom.sagaronline.Adapter.AddressAdapter;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Model.AddressModel;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ecom.sagaronline.Config.BaseURL.GET_ADDRESS_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment {

    CardView cv_add_btn;
    Button btn_next;

    TextView user_name;
    TextView user_phone;
    TextView user_address;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddressFragment() {
        // Required empty public constructor
    }

    RecyclerView rec_add_Address;
    List<AddressModel> itemList;

    AddressAdapter adapter;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_address, container, false);
        ((MainActivity) getActivity()).setTitle("Select delivery Address");
        cv_add_btn=v.findViewById(R.id.add);

        rec_add_Address = v.findViewById(R.id.recycler_address);
        rec_add_Address.setHasFixedSize(true);
        rec_add_Address.setLayoutManager(new LinearLayoutManager(getActivity()));
//--------------------------------------------------------------------
        itemList=new ArrayList<>();
        user_name = v.findViewById(R.id.tv_addname);
        user_phone = v.findViewById(R.id.tv_phone);
        user_address = v.findViewById(R.id.tv_address);

        getaddress();
        //initData();

     //   rec_add_Address.setAdapter(new AddressAdapter(initData()));

        cv_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NewAddressActivity.class);
                startActivity(i);

            }
        });

        btn_next=v.findViewById(R.id.next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressModel addressModel = itemList.get(adapter.getSelectedPosition());
                String location_id = addressModel.getLocation_id();
                String user_id = addressModel.getUser_id();
                String pincode = addressModel.getPincode();
                String socity_id = addressModel.getSocity_id();
                String house_no = addressModel.getHouse_no();
                String receiver_name = addressModel.getReceiver_name();
                String receiver_moblie = addressModel.getReceiver_moblie();
                String store_id = addressModel.getStore_id();
                String title = addressModel.getTitle();
                String address_type = addressModel.getAddress_type();
                String socity_name = addressModel.getSocity_name();
                String delivery_charge = addressModel.getDelivery_charge();

                Bundle bundle = new Bundle();
                bundle.putString("location_id", location_id);
                bundle.putString("user_id",user_id);
                bundle.putString("pincode",pincode);
                bundle.putString("socity_id",socity_id);
                bundle.putString("house_no",house_no);
                bundle.putString("receiver_name",receiver_name);
                bundle.putString("receiver_moblie",receiver_moblie);
                bundle.putString("store_id",store_id);
                bundle.putString("title",title);
                bundle.putString("address_type",address_type);
                bundle.putString("socity_name",socity_name);
                bundle.putString("delivery_charge",delivery_charge);
                Log.e("aadress fragment",bundle.toString());

            }
        });

        return v;
    }

    private void getaddress()
    {
        HashMap<String, String> params = new HashMap<>();
        String tag_json_req = "Contact_us_request";

        params.put("user_id", "46");

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, GET_ADDRESS_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Log.e("response_dat", "onResponse: "+response.toString() );
                    boolean res=response.getBoolean("responce");
                    if(res)
                    {
                        JSONArray arr=response.getJSONArray("data");
                        for(int i=0;i<arr.length();i++){

                            JSONObject obj=arr.getJSONObject(i);
                            AddressModel model=new AddressModel();
                            model.setLocation_id(obj.getString("location_id"));
                            model.setUser_id(obj.getString("user_id"));
                            model.setPincode(obj.getString("pincode"));
                            model.setSocity_id(obj.getString("socity_id"));
                            model.setHouse_no(obj.getString("house_no"));
                            model.setReceiver_name(obj.getString("receiver_name"));
                            model.setReceiver_moblie(obj.getString("receiver_mobile"));
                            model.setStore_id(obj.getString("store_id"));
                            model.setTitle(obj.getString("title"));
                            model.setAddress_type(obj.getString("address_type"));
                            model.setSocity_name(obj.getString("socity_name"));
                            model.setDelivery_charge(obj.getString("delivery_charge"));
                            model.setChecked(false);
                            itemList.add(model);
                            adapter = new AddressAdapter(getActivity(),itemList);
                        }
                        rec_add_Address.setAdapter(adapter);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Log.e("response", "onResponse: "+ response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }

   /** private List<AddressModel> initData() {
        itemList=new ArrayList<>();
        itemList.add(new AddressModel("1","1","6576687","11","2","Isha","9784679457","76","miss","hone","nagara","50",true));
        itemList.add(new AddressModel("1","1","6576687","11","2","anil","9784679457","76","mr","office","nagara","50",true));

        return itemList;
    }*/

}