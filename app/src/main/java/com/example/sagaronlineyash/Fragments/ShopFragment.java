package com.example.sagaronlineyash.Fragments;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sagaronlineyash.Adapter.Home_adapter;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.Model.CategoryModel;
import com.example.sagaronlineyash.Model.NewProductModel;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;
import com.example.sagaronlineyash.Utils.LoadingBar;
import com.example.sagaronlineyash.Utils.RecyclerTouchListener;
import com.example.sagaronlineyash.Utils.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends android.app.Fragment {

    List<CategoryModel> categoryList;
    Home_adapter categoryAdapter;
    Module module;
    RecyclerView rec_category;
    private boolean isSubcat = false;
    LoadingBar loadingBar;
    String getid="";
    Session_management session_management;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        categoryList = new ArrayList<>();
        rec_category = view.findViewById(R.id.rv_cat);
        session_management= new Session_management(getActivity());
        module=new Module(getActivity());
        loadingBar = new LoadingBar(getActivity());
        makeGetCategoryRequest();
        rec_category.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rec_category, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("RV_HEADER","RV_HEADER");
                getid = categoryList.get(position).getId();
                String title=categoryList.get(position).getTitle();
                String parent=categoryList.get(position).getCount();
                String slab_value=categoryList.get(position).getSlab_value();
                String max_slab=categoryList.get(position).getMax_slab_amt();

                if(parent.equals("0"))
                {
                    Bundle args = new Bundle();
                    android.app.Fragment fm = new ProductFragment();
                    args.putString("cat_id", getid);
                    args.putString( "title" ,title );
                    args.putString("slab_value",slab_value);
                    args.putString("max_slab",max_slab);
                    session_management.setCategoryId(getid);
                    // args.putString( "" );
                    // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace( R.id.fragment_container, fm)
                            .addToBackStack(null).commit();

                }
                else
                {
                    Bundle args = new Bundle();
                    android.app.Fragment fm = new SubcategoryFragment();
                    args.putString("cat_id", getid);
                    args.putString( "title" ,title );
                    // args.putString( "" );
                    // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                    fm.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace( R.id.fragment_container, fm)
                            .addToBackStack(null).commit();

                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }
    private void makeGetCategoryRequest() {
        loadingBar.show();
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<CategoryModel>>() {
                            }.getType();
                            categoryList = gson.fromJson(response.getString("data"), listType);
                            categoryAdapter = new Home_adapter(categoryList,"s");
                            rec_category.setLayoutManager(new GridLayoutManager(getActivity(),1));
                            rec_category.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        }
                        loadingBar.dismiss();
                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}