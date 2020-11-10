package com.example.sagaronlineyash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.Model.SocityModel;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonArrayRequest;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.sagaronlineyash.Config.BaseURL.ADD_ADDRESS_URL;
import static com.example.sagaronlineyash.Config.BaseURL.GET_SOCITY_URL;

public class NewAddressActivity<Private> extends AppCompatActivity {
    Button btn_add_address;

    EditText et_receiver_name;
    EditText et_receiver_mobile;
    EditText et_house_no;

    TextView tv_home;
    TextView tv_office;
    TextView tv_other;

    String pincodes;
    String user_id;
    String name;
    String titles;
    String types;
    String mobile;
    String address;
    String socity=" ";
    String delivery_charges;

    Module module;

    Spinner pincode;
    RadioButton title;
    RadioGroup rg;

    List<SocityModel> socityList;
    List<String> pincodeList;


    private final String TAG = NewAddressActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        getpincode();

        pincode=findViewById(R.id.et_pincode);
       // pincodeList.setAdapter(myAdapter);
        //pincodes= pincodeList.getSelectedItem().toString();

        pincodeList = new ArrayList<>();
        socityList = new ArrayList<>();
        btn_add_address=findViewById(R.id.btn_add_address);

        et_receiver_name=findViewById(R.id.et_name);
        et_receiver_mobile=findViewById(R.id.et_mobile);
        et_house_no=findViewById(R.id.editTextTextMultiLine);

        tv_home=findViewById(R.id.tv_home);
        tv_office=findViewById(R.id.tv_office);
        tv_other=findViewById(R.id.tv_other);


        rg = (RadioGroup) findViewById(R.id.rg_title);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = rg.getCheckedRadioButtonId();
                 title = (RadioButton) findViewById(checkedId);
                titles=title.getText().toString();
                Log.e(TAG, "onCheckedChanged: "+titles );
//              Log.e("tile", "onCheckedChanged: "+str);
            }
        });


        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("radiiiiiiidisdasdasd", "onClick: "+title.getText() );
                tv_home.setBackground(getResources().getDrawable(R.drawable.active));
                tv_home.setTextColor(getResources().getColor(R.color.white));
                tv_office.setBackground(getResources().getDrawable(R.drawable.inactive));
                tv_office.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_other.setBackground(getResources().getDrawable(R.drawable.inactive));
                tv_other.setTextColor(getResources().getColor(R.color.colorPrimary));
                types="home";
            }
        });


        tv_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_office.setBackground(getResources().getDrawable(R.drawable.active));
                tv_office.setTextColor(getResources().getColor(R.color.white));
                tv_home.setBackground(getResources().getDrawable(R.drawable.inactive));
                tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_other.setBackground(getResources().getDrawable(R.drawable.inactive));
                tv_other.setTextColor(getResources().getColor(R.color.colorPrimary));
                types="office";
            }
        });


        tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_other.setBackground(getResources().getDrawable(R.drawable.active));
                tv_other.setTextColor(getResources().getColor(R.color.white));
                tv_home.setBackground(getResources().getDrawable(R.drawable.inactive));
                tv_home.setTextColor(getResources().getColor(R.color.colorPrimary));
                tv_office.setBackground(getResources().getDrawable(R.drawable.inactive));
                tv_office.setTextColor(getResources().getColor(R.color.colorPrimary));
                types="other";

            }
        });


        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // titles = title.getText().toString();
               // name=et_receiver_name.getText().toString();
                //mobile=et_receiver_mobile.getText().toString();
//                pincodes=pincode.getSelectedItem().toString();
               // address=et_house_no.getText().toString();
                checkDataEntered();

                addnewaddress(titles,name,mobile,pincodes,address,types);


            }
        });

    }

    private void getpincode() {
        HashMap<String, String> para = new HashMap<>();
        String tag_json_req="get_pincode_request";
        para.put("socity_id", "3");
        para.put("delivery_charge",delivery_charges);
        para.put("pincode", pincodes);

        Log.e(TAG, "user_id-  " + para.toString());

        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET, GET_SOCITY_URL, para, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.e("Response",response.toString());
                if (response.length()!=0) {
                    try {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<SocityModel>>() {
                        }.getType();
                        socityList = gson.fromJson(String.valueOf(response),listType);

                        Log.e("SocityList",socityList.toString());
                        for (int i = 0; i <= response.length(); i++) {
                            Log.e("Pincode 1",socityList.get(i).getPincode().toString());
                            pincodeList.add(socityList.get(i).getPincode());

                            ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(NewAddressActivity.this, android.R.layout.simple_list_item_1,pincodeList);
                            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            pincode.setAdapter(myAdapter);

                        }



                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
               // Log.e("response", "onResponse: "+ response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest);
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public  void checkDataEntered()
    {
        if (isEmpty(et_receiver_name)) {
            et_receiver_name.setError("Enter valid name!");
        }

        if (isEmpty(et_receiver_mobile)) {
            et_receiver_mobile.setError("Enter valid number!");
        }


        if (isEmpty(et_house_no)) {
            et_house_no.setError("Enter valid address!");
        }

        if(pincodes.equalsIgnoreCase("Choose Pincode")){
            TextView errorText = (TextView)pincode.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Please Choose Pincode");
        }

    }

    private void addnewaddress( String titles, String name, String mobile, String pincodes, String address, String types) {
        HashMap<String, String> params = new HashMap<>();
        String tag_json_req = "add_address_request";

        params.put("user_id", "1");
        params.put("receiver_name",name);
        params.put("receiver_mobile",mobile);
        params.put("pincode", pincodes);
        params.put("socity_id", "3");
        params.put("house_no", address);
        params.put("title",titles);
        params.put("type",types);
        Log.e(TAG, "user_id-  " + params.toString());

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, ADD_ADDRESS_URL, params , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean res = response.getBoolean("responce");
                    Log.e("response", "onResponse: " + response.toString());
                    if(res){

                        Log.e("response", "onResponse: " + response.toString());
                       /** new Module(getActivity()).showToast("Address added");
                        ((MainActivity)getActivity()).onBackPressed();*/


                    }
                    else{
                        JSONObject obj = new JSONObject("error");
                        String str = obj.getString("error");

                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Log.e("response", "onResponse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }


}