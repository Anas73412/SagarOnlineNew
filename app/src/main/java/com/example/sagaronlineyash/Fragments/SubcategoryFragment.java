package com.example.sagaronlineyash.Fragments;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sagaronlineyash.Activity.MainActivity;
import com.example.sagaronlineyash.Adapter.Home_adapter;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.Model.CategoryModel;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.ConnectivityReceiver;
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
 * Use the {@link SubcategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubcategoryFragment extends android.app.Fragment {

    Module module;
    private static String TAG = SubcategoryFragment.class.getSimpleName();
    private RecyclerView rv_items;
    private List<CategoryModel> category_modelList = new ArrayList<>();
    //private Shop_Now_adapter adapter;
    Session_management session_management;
    private boolean isSubcat = false;
    String getid;
    String getcat_title;
    LoadingBar loadingBar;
    Home_adapter home_icon_adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubcategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubcategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubcategoryFragment newInstance(String param1, String param2) {
        SubcategoryFragment fragment = new SubcategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);
        loadingBar = new LoadingBar(getActivity());
        module=new Module(getActivity());
        setHasOptionsMenu(true);
        session_management=new Session_management(getActivity());
        String getcat_id = getArguments().getString("cat_id");
        String getcat_name = getArguments().getString("title");

        ((MainActivity) getActivity()).setTitle(getcat_name);


        if (ConnectivityReceiver.isConnected()) {
            //     makeSubGetCategoryRequest();
            makeGetCategoryRequest(getcat_id);

        }

        rv_items = (RecyclerView) view.findViewById(R.id.rv_sub);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        rv_items.setLayoutManager(gridLayoutManager);
        // rv_items.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(-25), true));
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(false);
        rv_items.setItemViewCacheSize(10);
        rv_items.setDrawingCacheEnabled(true);
        rv_items.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                getid=category_modelList.get(position).getId();
                String title=category_modelList.get(position).getTitle();
                Bundle args = new Bundle();
                android.app.Fragment fm = new ProductFragment();
                args.putString("cat_id", getid);
                args.putString( "title" , title);
                args.putString("slab_value",category_modelList.get(position).getSlab_value());
                args.putString("max_slab",category_modelList.get(position).getMax_slab_amt());
                session_management.setCategoryId(getid);
                // args.putString( "" );
                // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                        .addToBackStack(null).commit();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;

    }

    private void makeGetCategoryRequest(final String parent_id) {
        loadingBar.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", parent_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response from category", response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<CategoryModel>>() {}.getType();
                        category_modelList = gson.fromJson(response.getString("data"), listType);
                        if (!category_modelList.isEmpty()) {
                            rv_items.setVisibility(View.VISIBLE);
                            home_icon_adapter=new Home_adapter(category_modelList,"h");
                            rv_items.setAdapter(home_icon_adapter);
                            home_icon_adapter.notifyDataSetChanged();
                            loadingBar.dismiss();

                        } else {

                        }

                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}