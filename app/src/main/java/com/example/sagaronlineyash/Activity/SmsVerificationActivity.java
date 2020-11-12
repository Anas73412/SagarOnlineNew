package com.example.sagaronlineyash.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.SmsReceiver;
import com.example.sagaronlineyash.Utils.ConnectivityReceiver;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;
import com.example.sagaronlineyash.Utils.LoadingBar;
import com.example.sagaronlineyash.Utils.Session_management;
import com.example.sagaronlineyash.Utils.SmsListener;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.sagaronlineyash.Config.BaseURL.URL_VERIFY_OTP;
import static com.example.sagaronlineyash.Config.BaseURL.URL_VERIFY_REGISTER_OTP;

public class SmsVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    private final int REQUEST_ID_MULTIPLE_PERMISSIONS=1;
    CardView btn_msg;
    EditText et_otp;
    String type="";
    String Otp="";
    String number="",name="",wStatus="";
    LoadingBar loadingBar;
    String msg_status="";
    Module module;
    CountDownTimer countDownTimer;
    Session_management session_management;
    public static final String OTP_REGEX = "[0-9]{3,6}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_verification);
        btn_msg=findViewById(R.id.submit);
        et_otp=(EditText) findViewById(R.id.etMobile);
        loadingBar=new LoadingBar(SmsVerificationActivity.this);
        module=new Module(SmsVerificationActivity.this);
        type=getIntent().getStringExtra("type");
        number=getIntent().getStringExtra("mobile");
        Otp=getIntent().getStringExtra("otp");
        msg_status=getIntent().getStringExtra("msg_status");

        name=getIntent().getStringExtra("name");

        if(msg_status.equals("0"))
        {
            countDownTimer=new CountDownTimer(5000,1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    et_otp.setText(Otp);
                }
            };
            countDownTimer.start();

//            module.showToast("Your One Time Password is : "+Otp);
        }
        else
        {
            startSmsUserConsent();
        }


        session_management=new Session_management(SmsVerificationActivity.this);
        checkAndRequestPermissions();

        btn_msg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        if(id == R.id.submit)
        {
            if(ConnectivityReceiver.isConnected())
            {
                verification();

            }

        }

    }

    public void getSmsOtp()
    {
        try
        {


            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {

                    //From the received text string you may do string operations to get the required OTP
                    //It depends on your SMS format
                    Log.e("Message",messageText);
                    // Toast.makeText(SmsVerificationActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();

                    // If your OTP is six digits number, you may use the below code

                    Pattern pattern = Pattern.compile(OTP_REGEX);
                    Matcher matcher = pattern.matcher(messageText);
                    String otp="";
                    while (matcher.find())
                    {
                        otp = matcher.group();
                    }

                    if(!(otp.isEmpty() || otp.equals("")))
                    {
                        et_otp.setText(otp);

                        if(ConnectivityReceiver.isConnected())
                        {
                            verification();
                        }
                    }

                    //           Toast.makeText(SmsVerificationActivity.this,"OTP: "+ otp ,Toast.LENGTH_LONG).show();

                }
            });
        }
        catch (Exception ex)
        {
            // Toast.makeText(SmsVerificationActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void verification() {
        String otp=et_otp.getText().toString();
        if(otp.isEmpty())
        {
            et_otp.setError("Enter OTP");
            et_otp.requestFocus();
        }
        else if(otp.length()<4)
        {
            et_otp.setError("OTP is too short");
            et_otp.requestFocus();
        }
        else {
            if(type.equals("f"))
            {
                verifyMobileWithOtp(number,otp);

            }
            else if(type.equals("r"))
            {
                verifyRegisterMobileWithOtp(number,otp);

            }

        }
    }

    private void verifyRegisterMobileWithOtp(final String number, String otp) {
        loadingBar.show();
        String json_tag="json_verification";
        HashMap<String, String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("otp",otp);



        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST,URL_VERIFY_REGISTER_OTP, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    loadingBar.dismiss();
                    Log.d("verify",response.toString());
                    boolean status=response.getBoolean("responce");
                    if(status)
                    {

                        Intent intent = new Intent( SmsVerificationActivity.this,RegisterDetailsActivity.class );
                        intent.putExtra( "mobile", number );
                        startActivity( intent );
                        finish();



                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(SmsVerificationActivity.this,""+response.getString("error").toString(), Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(SmsVerificationActivity.this,""+ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showToast(error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }


    private void verifyMobileWithOtp(final String number, String otp) {
        loadingBar.show();
        String json_tag="json_verification";
        HashMap<String, String> map=new HashMap<>();
        map.put("mobile",number);
        map.put("otp",otp);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST,URL_VERIFY_OTP, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    loadingBar.dismiss();
                    Log.d("verify",response.toString());
                    boolean status=response.getBoolean("responce");
                    if(status)
                    {
                        String data=response.getString("data");

                        Intent intent = new Intent( SmsVerificationActivity.this, ForgotActivity.class );
                        intent.putExtra( "mobile", number );
                        startActivity( intent );
                        finish();

                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(SmsVerificationActivity.this,""+response.getString("error").toString(), Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(SmsVerificationActivity.this,""+ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.showToast(error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }

    private boolean checkAndRequestPermissions()
    {
        int sms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (sms != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

}
