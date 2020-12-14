package com.example.sagaronlineyash.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sagaronlineyash.Activity.MainActivity;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.Model.Socity_model;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.ConnectivityReceiver;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonArrayRequest;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;
import com.example.sagaronlineyash.Utils.LoadingBar;
import com.example.sagaronlineyash.Utils.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Add_delivery_address_fragment extends Fragment implements View.OnClickListener {


    private static String TAG = Add_delivery_address_fragment.class.getSimpleName();
    private List<Socity_model> socity_modelList = new ArrayList<>();
    Module module;
    List<String> list;
    private EditText et_phone, et_name, et_address;
    private AutoCompleteTextView et_pin;
    private RelativeLayout btn_update;
    private TextView tv_phone, tv_name, tv_pin, tv_address, tv_socity, btn_socity ,tv_submit;
    private String getsocity_id = "";

    private Session_management sessionManagement;

    private boolean isEdit = false;

    private String getlocation_id;
    private String[] pincodes;
   LoadingBar loadingBar ;
    public Add_delivery_address_fragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);
        list=new ArrayList<>();
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.add));
        loadingBar = new LoadingBar(getActivity());
        sessionManagement = new Session_management(getActivity());
        module =new Module(getActivity());
        et_phone = (EditText) view.findViewById(R.id.et_add_adres_phone);
        et_name = (EditText) view.findViewById(R.id.et_add_adres_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_add_adres_phone);
        tv_name = (TextView) view.findViewById(R.id.tv_add_adres_name);
        tv_pin = (TextView) view.findViewById(R.id.tv_add_adres_pin);
        et_pin = (AutoCompleteTextView) view.findViewById(R.id.et_add_adres_pin);
        et_address = (EditText) view.findViewById(R.id.et_add_adres_home);
        tv_address =(TextView)view.findViewById( R.id.tv_add );
        tv_submit = view.findViewById( R.id.add_updt );
        //tv_socity = (TextView) view.findViewById(R.id.tv_add_adres_socity);
        btn_update = (RelativeLayout) view.findViewById(R.id.btn_add_adres_edit);
        //  btn_socity = (TextView) view.findViewById(R.id.btn_add_adres_socity);


        getSocities();

        et_pin.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                et_pin.showDropDown();
                return false;
            }
        } );

        et_pin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getsocity_id=socity_modelList.get(i).getSocity_id();

                // Toast.makeText(getActivity(),"ccc"+socity_modelList.get(i).getPincode(),Toast.LENGTH_SHORT).show();
            }
        });

        String pin = et_pin.getText().toString();


//        for(int i =0 ;i<=pincodes.length ;i++)
//        {
//            String pins = pincodes[i];
//            if(!(pins.equals( pin )))
//            {
//                et_pin.requestFocus();
//                et_pin.setError( "We dont deliver at this address" );
//            }
//        }


        Bundle args = getArguments();

        if (args != null) {
            isEdit = true;

            getlocation_id = getArguments().getString("location_id");
            String get_name = getArguments().getString("name");
            String get_phone = getArguments().getString("mobile");
            String get_pin = getArguments().getString("pincode");
            String get_socity_id = getArguments().getString("socity_id");
            String get_socity_name = getArguments().getString("socity_name");
            String get_add = getArguments().getString("house");

            if (TextUtils.isEmpty(get_name) && get_name == null) {

            }
            else {


                //  Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();

                et_name.setText(get_name);
                et_phone.setText(get_phone);
                et_pin.setText(get_pin);
                et_address.setText(get_add);
                //  btn_socity.setText(get_socity_name);

                // sessionManagement.updateSocity(get_socity_name, get_socity_id);
            }
        }
        else
        {
            isEdit = false;
        }



       /* if (!TextUtils.isEmpty(getsocity_name)) {

            btn_socity.setText(getsocity_name);
            sessionManagement.updateSocity(getsocity_name, getsocity_id);
        }*/

        btn_update.setOnClickListener(this);
        // btn_socity.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            module.preventMultipleClick(btn_update);
            attemptEditProfile();
        } /*else if (id == R.id.btn_add_adres_socity) {

            /*String getpincode = et_pin.getText().toString();

            if (!TextUtils.isEmpty(getpincode)) {*/

//                Bundle args = new Bundle();
//                binplus.Jabico.Fragment fm = new Socity_fragment();
//                //args.putString("pincode", getpincode);
//                fm.setArguments(args);
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                        .addToBackStack(null).commit();
            /*} else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            }*/


    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.tv_reg_pincode));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_address.setText("Address");
        //  tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_pin.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_address.setTextColor(getResources().getColor(R.color.dark_gray));
        // tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpin = et_pin.getText().toString();
        String getadd = et_address.getText().toString();
        // String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);
        String getsocity = getsocity_id;
        boolean cancel = false;
        View focusView = null;

        //list = Arrays.asList(pincodes);
        if (TextUtils.isEmpty(getphone)) {
            et_phone.setError("Enter Mobile number");
            focusView = et_phone;
            cancel = true;
        }
//        else if (!isPhoneValid(getphone)) {
//            tv_phone.setText(getResources().getString(R.string.phone_too_short));
//            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
//            focusView = et_phone;
//            cancel = true;
//        }
        else if(!(getphone.length()==10))
        {
            et_phone.setError("Invalid mobile number");
            focusView = et_phone;
            cancel = true;
        }
        else if (getphone.charAt(0)<6)
        { et_phone.setError("Invalid mobile number");
            focusView = et_phone;
            cancel = true;

        }


        if (TextUtils.isEmpty(getname)) {
            et_name.setError("Enter name");
            focusView = et_name;
            cancel = true;
        }


        if (TextUtils.isEmpty(getpin)) {
            et_pin.setError("Select any one pincode");
            focusView = et_pin;
            cancel = true;
        }

        if (TextUtils.isEmpty(getadd)) {
            et_address.setError("Enter address");
            focusView = et_address;
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                if(!list.contains(et_pin.getText().toString()))
                {
                    Toast.makeText(getActivity(),"We don't delivered at this pincode", Toast.LENGTH_LONG).show();
                    et_pin.setError("delivery not available here ");
                    et_pin.requestFocus();
                }
                else
                {
                    String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                    // check internet connection
                    if (ConnectivityReceiver.isConnected()) {
                        if (isEdit) {
                            makeEditAddressRequest(getlocation_id, getpin,getSocietyId(getpin).getSocity_id(), getadd, getname, getphone);
                        } else {
                            makeAddAddressRequest(user_id, getpin, getsocity_id,getadd, getname, getphone);
                        }
                    }
                }

            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddAddressRequest(String user_id, String pincode, String socity_id,
                                       String address, String receiver_name, String receiver_mobile) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pincode", pincode);
        params.put("socity_id", socity_id);
        params.put("house_no", address);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);




        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        ((MainActivity) getActivity()).onBackPressed();

                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Something wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeEditAddressRequest(String location_id, String pincode, String socity_id,
                                        String add, String receiver_name, String receiver_mobile) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("location_id", location_id);
        params.put("pincode", pincode);
        params.put("socity_id", socity_id);
        params.put("house_no", add);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);
//     Toast.makeText(getActivity(),"loc- "+location_id+"\n pin- "+pincode+"\n soc- "+socity_id+"\n add- "+add
//             +"\n name- "+receiver_name+"\n mob- "+receiver_mobile+"\n list_soc- "+getSocietyId(pincode).getSocity_id(),Toast.LENGTH_SHORT).show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.EDIT_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    loadingBar.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("data");
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                        ((MainActivity) getActivity()).onBackPressed();

                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void getSocities()
    {
        loadingBar.show();
        String json_tag="json_socity_tag";
        HashMap<String, String> map=new HashMap<>();

        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.POST, BaseURL.GET_SOCITY_URL, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    loadingBar.dismiss();
                    Log.d("pincode",response.toString());

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Socity_model>>() {
                    }.getType();

                    socity_modelList = gson.fromJson(response.toString(), listType);
                    pincodes=new String[socity_modelList.size()];
                    for(int i=0;i<socity_modelList.size();i++)
                    {
                        pincodes[i]=socity_modelList.get(i).getPincode();
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,pincodes);
                    // et_pin.setThreshold( 1 );
                    et_pin.setAdapter( arrayAdapter );
                    list= Arrays.asList(pincodes);
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
                    Toast.makeText(getActivity(),""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg, Toast.LENGTH_LONG).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_tag);
    }

    public Socity_model getSocietyId(String pin)
    {
        Socity_model sc_model=new Socity_model();
        for(int i=0; i<socity_modelList.size();i++)
        {
            Socity_model socity_model=socity_modelList.get(i);
            if(socity_model.getPincode().equals(pin))
            {
                sc_model=socity_model;
                break;
            }
        }
        return sc_model;
    }
}
