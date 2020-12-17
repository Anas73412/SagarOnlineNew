package com.ecom.sagaronline.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.LocaleHelper;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.LoadingBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    Module module;
    private static String TAG = ForgotActivity.class.getSimpleName();
    LoadingBar loadingBar ;
    Button btn_continue;
    private EditText et_con_pass,et_new_pass;
    private TextView tv_email;
    String lan;
    String num="";
    SharedPreferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title

        setContentView(R.layout.activity_forgot);
        num=getIntent().getStringExtra("mobile");
        et_new_pass=(EditText)findViewById(R.id.et_new_pass);
        et_con_pass=(EditText)findViewById(R.id.et_con_pass);
        loadingBar=new LoadingBar(ForgotActivity.this);

        module=new Module(ForgotActivity.this);

        btn_continue =  findViewById(R.id.btnContinue);
//
        btn_continue.setOnClickListener(this);
//        preferences = getSharedPreferences("lan", MODE_PRIVATE);
//        lan=preferences.getString("language","");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnContinue) {
            String n_pass=et_new_pass.getText().toString();
            String c_pass=et_con_pass.getText().toString();
            requestPassword(num,n_pass,c_pass);
            // attemptForgot();
        }
    }

    private void requestPassword(String num,String n_pass, String c_pass) {

        if(n_pass.isEmpty())
        {
            et_new_pass.setError("enter password");
            et_new_pass.requestFocus();
        }
        else if(c_pass.isEmpty())
        {
            et_con_pass.setError("enter confim password");
            et_con_pass.requestFocus();
        }
        else if(n_pass.length()<6)
        {
            et_new_pass.setError("password too short");
            et_new_pass.requestFocus();
        }
        else if(c_pass.length()<6)
        {
            et_con_pass.setError("password too short");
            et_con_pass.requestFocus();
        }
        else
        {
            if(n_pass.equals(c_pass))
            {
                getForgotRequest(num,n_pass);
            }
            else
            {
                Toast.makeText(ForgotActivity.this,"Password must be matched",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void getForgotRequest(String num,String n_pass) {

        loadingBar.show();
        String json_tag="json_forgot_tag";
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",num);
        map.put("password",n_pass);


        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.FORGOT_URL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    boolean responce=response.getBoolean("responce");
                    String msg=response.getString("error");
                    if(responce)
                    {
                        Toast.makeText(ForgotActivity.this,""+ msg,Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(ForgotActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ForgotActivity.this,""+ msg,Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(ForgotActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(ForgotActivity.this,""+ ex.getMessage(),Toast.LENGTH_LONG).show();
                }
                //  Toast.makeText(ForgotActivity.this,""+response.toString(),Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(ForgotActivity.this,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }


    private void makeForgotRequest(String email) {

        // Tag used to cancel the request
        String tag_json_obj = "json_forgot_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.FORGOT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    String error = response.getString("error");
                    String error_arb=response.getString("error_arb");
                    if (status) {
                        Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ForgotActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        if (lan.contains("english")) {
                            Toast.makeText(ForgotActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                        }
                       /* else {
                            Toast.makeText(ForgotActivity.this, "" + error_arb, Toast.LENGTH_SHORT).show();

                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(ForgotActivity.this,""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
