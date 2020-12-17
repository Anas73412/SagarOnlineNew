package com.ecom.sagaronline.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.Session_management;

public class SplashActivity extends AppCompatActivity {

    Session_management session_management;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

       session_management=new Session_management(SplashActivity.this);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session_management.isLoggedIn())
                {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }
                finish();
            }
        },2500);
    }
}