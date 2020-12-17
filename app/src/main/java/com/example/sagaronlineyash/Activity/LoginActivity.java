package com.example.sagaronlineyash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.ConnectivityReceiver;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;
import com.example.sagaronlineyash.Utils.LoadingBar;
import com.example.sagaronlineyash.Utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    LoadingBar loadingBar;
    TextView btn_register,btn_forgot;
    Module module;
    EditText et_password , et_email;
    Button btn_continue;
    Session_management sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        sessionManagement = new Session_management(LoginActivity.this);
        module=new Module(LoginActivity.this);
        loadingBar = new LoadingBar(LoginActivity.this);
        et_password = (EditText) findViewById(R.id.etPassword);
        et_email = (EditText) findViewById(R.id.etMobile);
        btn_continue = findViewById(R.id.login);
        btn_register = findViewById(R.id.register);
        btn_forgot = (TextView) findViewById(R.id.forgotPass);

        btn_forgot.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.login) {
            attemptLogin();
        } else if (id == R.id.register) {
            Intent startRegister = new Intent(LoginActivity.this,RegisterActivity.class);
            startRegister.putExtra("type","r");
            startActivity(startRegister);
        } else if (id == R.id.forgotPass) {
            Intent startRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startRegister.putExtra("type","f");
            startActivity(startRegister);
//
        }

    }

    private void attemptLogin() {

        String getpassword = et_password.getText().toString();
        String getemail = et_email.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(getpassword))
        {
            et_password.setError("Enter Password");
            et_password.requestFocus();
            focusView = et_password;
            cancel = true;
        }
        else if (getpassword.length()<6)
        {
            et_password.setError("Password is too short");
            et_password.requestFocus();
            focusView = et_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail))
        {

            et_email.setError("Enter Mobile");
            et_email.requestFocus();
            focusView = et_email;
            cancel = true;
        }
        else if (!isPhoneValid(getemail)) {
            et_email.setError("Invalid Mobile");
            et_email.requestFocus();
            focusView = et_email;
            cancel = true;
        }

        if (cancel) {
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {
                makeLoginRequest(getemail, getpassword);
            }
        }

    }



    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    private boolean isPhoneValid(String phoneno) {

        return phoneno.length() == 10;
    }
    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeLoginRequest(String email, final String password) {

        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", email);
        params.put("password", password);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.LOGIN_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("LOGIN", response.toString());

                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        JSONObject obj = response.getJSONObject("data");
                        String user_id = obj.getString("user_id");
                        String user_fullname = obj.getString("user_fullname");
                        String user_email = obj.getString("user_email");
                        String user_phone = obj.getString("user_phone");
                        String user_image = obj.getString("user_image");
                        String wallet_ammount = obj.getString("wallet");
                        String reward_points = obj.getString("rewards");
                        sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, user_image, wallet_ammount, reward_points, "", "", "", "", password);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                        btn_continue.setEnabled(false);

                    } else {
                        String error = response.getString("error");
                        btn_continue.setEnabled(true);

                        Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e) {
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
                    Toast.makeText(LoginActivity.this,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    protected void onStart() {
        super.onStart();



    }
}