package com.ecom.sagaronline.Fragments;

import android.app.DatePickerDialog;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;

//import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.Session_management;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import static com.ecom.sagaronline.Config.BaseURL.EDIT_PROFILE_URL;
import static com.ecom.sagaronline.Config.BaseURL.KEY_EMAIL;
import static com.ecom.sagaronline.Config.BaseURL.KEY_HOUSE;
import static com.ecom.sagaronline.Config.BaseURL.KEY_ID;
import static com.ecom.sagaronline.Config.BaseURL.KEY_IMAGE;
import static com.ecom.sagaronline.Config.BaseURL.KEY_MOBILE;
import static com.ecom.sagaronline.Config.BaseURL.KEY_NAME;
import static com.ecom.sagaronline.Config.BaseURL.KEY_PINCODE;
import static com.ecom.sagaronline.Config.BaseURL.KEY_REWARDS_POINTS;
import static com.ecom.sagaronline.Config.BaseURL.KEY_SOCITY_ID;
import static com.ecom.sagaronline.Config.BaseURL.KEY_WALLET_Ammount;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    Session_management session_management;
    EditText et_email;
    EditText et_name , et_number, et_addess;
    Button update;
    TextView txtusername,txtuseremail,et_dob;

    ProgressBar progressBar;
    String name;
    String mobile;
    String pincode;
    String socity_id;
    String image;
    String wallet;
    String rewards;
    String house;
    String email="";

    TextView tv_name;
   // ImageView iv_edit;

    private final String TAG = ProfileFragment.class.getSimpleName();
DatePickerDialog.OnDateSetListener setListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View v= inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainActivity) getActivity()).setTitle("My Profile");
       // String type = getArguments().getString("type");
      //  String id = getArguments().getString("id");
      //  String username = getArguments().getString("name");
       // String usernumber = getArguments().getString("number");

        et_name = v.findViewById(R.id.name);
        et_number = v.findViewById(R.id.number);
        et_addess = v.findViewById(R.id.address);
        update = v.findViewById(R.id.update);
       // progressBar = v.findViewById(R.id.progressBar);
        et_dob=v.findViewById(R.id.date_of_birth);
        et_email=v.findViewById(R.id.email_update);
        txtusername=v.findViewById(R.id.username);
        txtuseremail=v.findViewById(R.id.useremail);
        Session_management session_management= new Session_management(getActivity());
         pincode=session_management.getUserDetails().get(KEY_PINCODE);
         socity_id=session_management.getUserDetails().get(KEY_SOCITY_ID);
         image=session_management.getUserDetails().get(KEY_IMAGE);
         wallet=session_management.getUserDetails().get(KEY_WALLET_Ammount);
         rewards=session_management.getUserDetails().get(KEY_REWARDS_POINTS);
         house=session_management.getUserDetails().get(KEY_HOUSE);

        et_number.setText(session_management.getUserDetails().get(KEY_MOBILE));
        txtusername.setText(session_management.getUserDetails().get(KEY_NAME));
        et_name.setText(session_management.getUserDetails().get(KEY_NAME));
        et_email.setText(session_management.getUserDetails().get(KEY_EMAIL));

        if(session_management.getUserDetails().get(KEY_EMAIL).equalsIgnoreCase("null") || session_management.getUserDetails().get(KEY_EMAIL).isEmpty()){
            txtuseremail.setText(" ");
        }else {

            txtuseremail.setText(session_management.getUserDetails().get(KEY_EMAIL));
        }
      //  txtuseremail.setText(session_management.getUserDetails().get(KEY_EMAIL));

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog =new DatePickerDialog(getActivity(),android.R.style.Theme_Material_Light_DarkActionBar,setListener,year,month,day);
                datePickerDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        setListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                et_dob.setText(date);
            }
        };

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();

               String username=et_name.getText().toString();
                String usernumber=et_number.getText().toString();
                 email = et_email.getText().toString();
                 String add = et_addess.getText().toString();
                 String birth = et_dob.getText().toString();

                 if (username.isEmpty()||usernumber.isEmpty()||email.isEmpty()||add.isEmpty()||birth.isEmpty()){
                     Toast.makeText(getActivity(),"Fill all the fields",Toast.LENGTH_SHORT).show();
                 }

             //  session_management.updateUserName(username);

                   params.put("user_id", session_management.getUserDetails().get(KEY_ID));
                   params.put("user_fullname",username);
//                   params.put("user_fullname",name);
                   params.put("user_mobile",usernumber);
                   params.put("user_email",email);
                   Log.e(TAG, "user_id-  " + params.toString());

//                if (TextUtils.isEmpty(username))
//                {
//                    et_name.setError("Enter Name!");
//                    et_name.requestFocus();
//                }
//                else
//                {
//                    progressBar.setVisibility(View.VISIBLE);
//                    session_management.updateUserName(username);
//                    params.put("user_id", id);
//                    params.put("user_fullname",username);
//                    params.put("user_mobile",usernumber);


                    CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, EDIT_PROFILE_URL, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Log.e("response_dat", "onResponse: "+response.toString() );
                                boolean res=response.getBoolean("responce");
                                if(res)
                                {
                                    Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_LONG).show();
//                                    progressBar.setVisibility(View.GONE);
                                    session_management.updateData(username,usernumber,pincode,socity_id, image,wallet,rewards,house,email);
                                    updatename();
                                    txtusername.setText(username);
                                    txtuseremail.setText(email);

//                                    HomeFragment addressFragment = new HomeFragment();
//                                    ((MainActivity)getActivity()).loadFragment(addressFragment,null);

                                }

                                else {
                                    JSONObject obj = new JSONObject("error");
                                    String str = obj.getString("error");
                                }

                            }
                            catch (Exception ex)
                            {
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

        //    }

        });

        return v;
    }

private void updatename() {
    ((MainActivity)getActivity()).updatename();
}
}