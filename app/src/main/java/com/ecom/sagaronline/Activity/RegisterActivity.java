package com.ecom.sagaronline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.LoadingBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import static com.ecom.sagaronline.Config.BaseURL.URL_GEN_OTP;
import static com.ecom.sagaronline.Config.BaseURL.URL_REGISTER_OTP;

public class RegisterActivity extends AppCompatActivity {

    EditText et_reg_number;
    Button btn_verify_number;
    LoadingBar loadingBar ;
    String type="";
    Module module;
    Context context;
    private String msg_status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        module=new Module(RegisterActivity.this);
        Intent intent = getIntent();
        type=intent.getStringExtra("type");
        et_reg_number=(EditText)findViewById(R.id.etMobile);
        btn_verify_number=findViewById(R.id.submit);
        loadingBar=new LoadingBar(RegisterActivity.this);
        getMessageStatus();
        btn_verify_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String phone=et_reg_number.getText().toString().trim();

                if(phone.isEmpty())
                {
                    et_reg_number.setError("enter mobile number");
                    et_reg_number.requestFocus();
                }
                else if(!isPhoneValid(phone))
                {
                    et_reg_number.setError("invalid mobile number");
                    et_reg_number.requestFocus();
                }
                else
                {
                    if(ConnectivityReceiver.isConnected())
                    {

                        String otp=getRandomKey(4);
                        if(type.equals("r"))
                        {
                            getVerificationCode(phone,otp);
                        }
                        else if(type.equals("f"))
                        {
                            getForgotVerificationCode(phone,otp);
                        }

                    }

                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void getForgotVerificationCode(final String phone, final String otp) {

        loadingBar.show();

        String json_tag="json_verifiaction_tag";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("mobile",phone);
        params.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, URL_GEN_OTP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    //    Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();
                    boolean responce=response.getBoolean("responce");
                    if(responce)
                    {
                        loadingBar.dismiss();
                        Intent intent=new Intent(getApplicationContext(), SmsVerificationActivity.class);
                        intent.putExtra("mobile",phone);
                        intent.putExtra("otp",otp);
                        intent.putExtra("type",type);
                        intent.putExtra("msg_status",msg_status);
                        startActivity(intent);
                        finish();
//
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(),"Mobile number not register",Toast.LENGTH_LONG).show();

                    }
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
                    Log.e("TRY ERROR ", ex.toString());
                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(getActivity(),""+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);



    }


    private boolean isPhoneValid(String phoneno) {

        return phoneno.length() > 9;
    }
    public static String getRandomKey(int i)
    {
        final String characters="0123456789";
        StringBuilder stringBuilder=new StringBuilder();
        while (i>0)
        {
            Random ran=new Random();
            stringBuilder.append(characters.charAt(ran.nextInt(characters.length())));
            i--;
        }
        return stringBuilder.toString();
    }


    private void getVerificationCode(final String phone, final String otp) {
        loadingBar.show();

        String json_tag="json_verifiaction_tag";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("mobile",phone);
        params.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, URL_REGISTER_OTP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    //  Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();
                    boolean responce=response.getBoolean("responce");
                    if(responce)
                    {
                        loadingBar.dismiss();
                        Intent intent=new Intent(getApplicationContext(), SmsVerificationActivity.class);
                        intent.putExtra("mobile",phone);
                        intent.putExtra("otp",otp);
                        intent.putExtra("type",type);
                        intent.putExtra("msg_status",msg_status);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        loadingBar.dismiss();

                        Toast.makeText(getApplicationContext(),""+response.getString("error").toString(),Toast.LENGTH_LONG).show();

                    }
                }
                catch (Exception ex)
                {
                    loadingBar.dismiss();
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);




    }

    public void getMessageStatus()
    {
        loadingBar.show();
        String json_tag="json_app_tag";
        HashMap<String,String> map=new HashMap<>();

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.GET_VERSION_URL, map, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d("app_setting",response.toString());
                loadingBar.dismiss();
                try
                {
                    boolean sts=response.getBoolean("responce");

                    if(sts)
                    {
                        JSONObject object=response.getJSONObject("data");
                        msg_status=object.getString("msg_status");


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),""+response.getString("error"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request,json_tag);
    }
}