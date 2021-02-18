package com.ecom.sagaronline.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.LoadingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import static com.ecom.sagaronline.Config.BaseURL.GET_TERMS_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermsFragment extends Fragment {

    TextView tv_term;
    LoadingBar loadingBar ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TermsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TermsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TermsFragment newInstance(String param1, String param2) {
        TermsFragment fragment = new TermsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setTitle("Terms & Condition");
        View v = inflater.inflate(R.layout.fragment_terms, container, false);
        loadingBar = new LoadingBar(getContext());
        tv_term=v.findViewById(R.id.TV_Terms);

        getTermsAndCondition();
        return v;

    }

    private void getTermsAndCondition() {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.GET,GET_TERMS_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    boolean resp=response.getBoolean("responce");
                    if(resp){
                        JSONArray dataArr=response.getJSONArray("data");
                        JSONObject obj=dataArr.getJSONObject(0);
                        String str=obj.getString("pg_descri");
                        tv_term.setText(Html.fromHtml(str));
                    }
                    loadingBar.dismiss();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                Log.e("response", "onResponse: "+ response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                new Module(getContext()).VolleyErrorMessage(error);

            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }
}