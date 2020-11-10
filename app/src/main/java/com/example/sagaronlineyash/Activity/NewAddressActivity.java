package com.example.sagaronlineyash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;

import org.json.JSONObject;

import java.util.HashMap;

import static com.example.sagaronlineyash.Config.BaseURL.ADD_ADDRESS_URL;

public class NewAddressActivity extends AppCompatActivity {
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
    String type;
    String mobile;
    String address;
    String socity1;

    Module module;

    Spinner pincode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        pincode=findViewById(R.id.et_pincode);
        ArrayAdapter<String> myAdapter =new ArrayAdapter<String>(NewAddressActivity.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.pincode));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pincode.setAdapter(myAdapter);
        pincodes= pincode.getSelectedItem().toString();

        btn_add_address=findViewById(R.id.btn_add_address);

        et_receiver_name=findViewById(R.id.et_name);
        et_receiver_mobile=findViewById(R.id.et_mobile);
        et_house_no=findViewById(R.id.editTextTextMultiLine);

        tv_home=findViewById(R.id.tv_home);
        tv_office=findViewById(R.id.tv_office);
        tv_other=findViewById(R.id.tv_other);


        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_title);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton title = (RadioButton) findViewById(checkedId);
                titles=title.getText().toString();
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


            }
        });

        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
                //  titles = title.getText().toString();
                name=et_receiver_name.getText().toString();
                mobile=et_receiver_mobile.getText().toString();
                pincodes=pincode.getSelectedItem().toString();
                address=et_house_no.getText().toString();
           //     addnewaddress(titles,name,mobile,pincodes,address);

            }
        });

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

  /**  private void addnewaddress( String titles, String name, String mobile, String pincodes, String address) {
        HashMap<String, String> params = new HashMap<>();
        String tag_json_req = "add_address_request";

        params.put("user_id", "1");
        params.put("pincode", pincodes);
        params.put("socity", "1");
        params.put("house_no", address);
        params.put("receiver_name",name);
        params.put("receiver_mobile",mobile);
        params.put("title",titles);
        params.put("type","");

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, ADD_ADDRESS_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean res = response.getBoolean("responce");
                    Log.e("response", "onResponse: " + response.toString());
                    if(res){
                       String str=response.getString("error");
                        Toast toast=Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    else{
                        String error = response.getString("error");
                        Toast toast=Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT);
                        toast.show();

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

                error.printStackTrace();


                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast toast=Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }*/

}