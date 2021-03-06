package com.ecom.sagaronline.Activity;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.ecom.sagaronline.Config.BaseURL.GET_ABOUT_URL;

public class AboutUsActivity extends Fragment {
TextView tv_aboutus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_about_us, container, false);
        tv_aboutus=v.findViewById(R.id.TV_AboutUs);
        ((MainActivity) getActivity()).setTitle("About Us");
        getaboutus();
        return v;
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