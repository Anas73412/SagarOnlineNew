package com.example.sagaronlineyash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.sagaronlineyash.Config.BaseURL.GET_ABOUT_URL;

public class AboutUsActivity extends AppCompatActivity {
TextView tv_aboutus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        tv_aboutus=findViewById(R.id.TV_AboutUs);

        getaboutus();
    }

    private void getaboutus() {
        HashMap<String,String> params=new HashMap<>();
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.GET,GET_ABOUT_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    boolean resp=response.getBoolean("responce");
                    if(resp){
                        JSONArray dataArr=response.getJSONArray("data");
                        JSONObject obj=dataArr.getJSONObject(0);
                        String str=obj.getString("pg_descri");
                        tv_aboutus.setText(Html.fromHtml(str));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                Log.e("response", "onResponse: "+ response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }
}