package com.ecom.sagaronline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.LocaleHelper;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.LoadingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterDetailsActivity extends AppCompatActivity {

    Module module;
    private static String TAG = RegisterActivity.class.getSimpleName();
    CardView btn_register;
    String number="";
    String verfiy_number;
    private EditText et_phone, et_name, et_password,et_reg__con_password;
    LoadingBar loadingBar ;
    @Override
    protected void attachBaseContext(Context newBase) {



        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_details);
        loadingBar = new LoadingBar(this);
        number=getIntent().getStringExtra("mobile");
        et_phone = (EditText) findViewById(R.id.etMobile);
        et_name = (EditText) findViewById(R.id.etName);
        et_password = (EditText) findViewById(R.id.etPassword);
        et_reg__con_password = (EditText) findViewById(R.id.etCnfPassword);
        module=new Module(RegisterDetailsActivity.this);
        btn_register =findViewById(R.id.Register);
        et_phone.setText(number);
        et_phone.setEnabled(false);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptRegister();
            }
        });
    }
    private void attemptRegister() {


        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpassword = et_password.getText().toString();
        String getconpass=et_reg__con_password.getText().toString();
        String f = String.valueOf( getphone.charAt( 0 ) );
        String plus = "[+]" ;
        //  int first = Integer.parseInt( f );

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {

            focusView = et_phone;
            cancel = true;
        }

        else if(!(getphone.length()==10))
        {
            et_phone.setText("Invalid mobile number");

            focusView = et_phone;
            cancel = true;
        }
        else if (getphone.charAt(0)<6)
        {
//        { tv_phone.setText("Invalid mobile number");
            et_phone.setError( "Invalid mobile number" );
            focusView = et_phone;
            cancel = true;

        }


        if (TextUtils.isEmpty(getname)) {
            et_name.setError( "name is required" );
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpassword)) {
            et_password.setError( "Password is Required" );
            focusView = et_password;
            cancel = true;
        } else if (getpassword.length()<6) {
            et_password.setError( "Password should be of min 6 characters" );

            focusView = et_password;
            cancel = true;
        }
        if (TextUtils.isEmpty(getconpass)) {
            et_reg__con_password.setError( "Confirm Password is Required" );
            focusView = et_reg__con_password;
            cancel = true;
        } else if (getconpass.length()<6) {
            et_reg__con_password.setError("Password is too short");
            focusView = et_reg__con_password;
            cancel = true;
        }


        if (cancel==true) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        }
        else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                if(getconpass.equals(getpassword))
                {
                    makeRegisterRequest(getname, getphone, getpassword);
                }
                else
                {
                    Toast.makeText(RegisterDetailsActivity.this,"Password must be matched",Toast.LENGTH_LONG).show();
                }

            }
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        if (phoneno.length()==10)
        {
            return phoneno.length() == 10;
        }
        return phoneno.length() == 10;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeRegisterRequest(String name, String mobile,
                                     String password) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile",mobile);
        params.put("user_email", "");
        params.put("password",password);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(RegisterDetailsActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(RegisterDetailsActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        btn_register.setEnabled(false);

                    } else {
                        String error = response.getString("error");


                        btn_register.setEnabled(true);

                        if(error.equals("The Mobile Number field must contain a unique value."))
                        {
                            Toast.makeText(RegisterDetailsActivity.this, "Mobile number already exist.", Toast.LENGTH_SHORT).show();
                        }
                        else if(error.equals("The User Email field must contain a unique value."))
                        {
                            Toast.makeText(RegisterDetailsActivity.this, "Email address already exist.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(RegisterDetailsActivity.this, "Mobile number already exist.\nEmail address already exist.", Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
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
                    Toast.makeText(RegisterDetailsActivity.this,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}