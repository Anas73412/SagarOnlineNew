package com.ecom.sagaronline.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.Model.GetCongifDataModel;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.OnGetConfigData;
import com.ecom.sagaronline.Utils.Session_management;

public class SplashActivity extends AppCompatActivity {

    Session_management session_management;
    Module module;
    int version=0 ,ver_code=0;
    String msg_status;
    int status;
    int vers;
    AnimatorSet set ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        module = new Module(SplashActivity.this);
        session_management = new Session_management(SplashActivity.this);
        set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rotate);
        set.setTarget(findViewById(R.id.logo));
        set.start();
        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ver_code = pkgInfo.versionCode;

        module.getCongifData(new OnGetConfigData() {
            @Override
            public void onGetConfigData(GetCongifDataModel model) {

                version = Integer.parseInt(model.getApp_version().toString());
                msg_status = model.getMsg_status();
                status = Integer.parseInt(msg_status);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("version",ver_code+"----"+version);
                        if (ver_code == version) {

                            go_next();
                            finish();
                        } else {
                            Toast.makeText(SplashActivity.this, "update your app", Toast.LENGTH_SHORT).show();

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashActivity.this);
                            builder.setCancelable(false);
                            builder.setMessage("The new version of app is available please update to get access.");
                            builder.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    // String url = app_link;
                                    String url = "https://play.google.com";
                                    Intent in = new Intent(Intent.ACTION_VIEW);
                                    in.setData(Uri.parse(url));
                                    startActivity(in);
                                    finish();
                                    //Toast.makeText(getActivity(),"updating",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
//                                    go_next();
                                    //finish();
                                     finishAffinity();
                                }
                            });
                            android.app.AlertDialog dialogs = builder.create();
                            dialogs.show();

                        }
                    }
                }, 2500);

                Log.e("njx" + vers, String.valueOf(+ver_code));

            }
        });


    }

    public void go_next(){

        if (session_management.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        else
            {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

    }
}