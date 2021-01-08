package com.ecom.sagaronline.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.sagaronline.Config.BaseURL.KEY_ID;
import static com.ecom.sagaronline.Config.BaseURL.KEY_MOBILE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends Fragment {
    LoadingBar loadingBar;
    Session_management session_management;
    EditText et_phone,et_name,et_message;
    Module module;
    Button submit;
    String name,mobile,message;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);
        loadingBar= new LoadingBar(getContext());
        session_management = new Session_management(getActivity());
        et_phone = v.findViewById(R.id.et_phone);
        et_name= v.findViewById(R.id.et_name);
        et_message=v.findViewById(R.id.et_message);

        et_phone.setText(session_management.getUserDetails().get(KEY_MOBILE));

        module=new Module(getContext());
        submit=v.findViewById(R.id.submit);

       // et_phone.setText(session_management.getUserDetails().get(KEY_MOBILE));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                mobile = et_phone.getText().toString();
                message = et_message.getText().toString();
                putSuggestion(name,mobile,message,session_management.getUserDetails().get(KEY_ID));
            }
        });

        return v;


    }
    private void putSuggestion(String username, String number, String message ,String user_id) {

        loadingBar.show();

        String tag_json_obj = "json_suggestion_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("user_name",username);
        params.put("user_phone",number);
        params.put("message",message);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.PUT_SUGGESTION_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("suggestion", response.toString());

                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                       // toastMsg.toastIconSuccess("" + msg);
                        Toast.makeText(getContext(),""+ msg,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));

                    } else {
                        String error = response.getString("error");
                        Toast.makeText(getContext(),""+ error,Toast.LENGTH_SHORT).show();
                        //toastMsg.toastIconError(error);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                error.printStackTrace();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    //toastMsg.toastIconError(""+msg);
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}