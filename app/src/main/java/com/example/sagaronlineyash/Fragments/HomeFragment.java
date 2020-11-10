package com.example.sagaronlineyash.Fragments;

//import android.app.FragmentManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.sagaronlineyash.Adapter.Home_adapter;
import com.example.sagaronlineyash.Adapter.NewProductAdapter;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.Model.CategoryModel;
import com.example.sagaronlineyash.Model.NewProductModel;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;
import com.example.sagaronlineyash.Utils.LoadingBar;
import com.example.sagaronlineyash.Utils.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    Module module;
    CarouselView carouselView;
    Session_management session_management ;
    LoadingBar loadingBar;
    NewProductAdapter newProductAdapter , top_selling_adapter;
    List<CategoryModel> categoryList;
    List<NewProductModel> newProductList , top_selling_models;
    Home_adapter categoryAdapter;
    RecyclerView rec_category , rec_new_product , rec_top_product;
    private boolean isSubcat = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        module=new Module(getActivity());
        loadingBar = new LoadingBar(getActivity());
        session_management = new Session_management(getActivity());
        categoryList = new ArrayList<>();
        newProductList = new ArrayList<>();
        carouselView = v.findViewById(R.id.carousel);
        rec_category = v.findViewById(R.id.rec_category);
        rec_new_product = v.findViewById(R.id.rec_new_product);
        rec_top_product = v.findViewById(R.id.rec_best_product);
        makeGetSliderRequest();
        makeGetCategoryRequest();
        new_products();
        make_top_selling();
        return v;
    }
    private void makeGetSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest( BaseURL.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("sliders", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("parent", jsonObject.getString("parent"));
                                url_maps.put("leval", jsonObject.getString("leval"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {

                                carouselView.setImageListener(new ImageListener() {
                                    @Override
                                    public void setImageForPosition(int position, ImageView imageView) {
                                        Glide.with(getActivity()).load(listarray.get(position).get("slider_image")).into(imageView);
                                    }
                                });
                                carouselView.setPageCount(listarray.size());

                                carouselView.setImageClickListener(new ImageClickListener() {
                                    @Override
                                    public void onClick(int position) {
                                        Bundle args = new Bundle();
                                        String sub_cat = listarray.get(position).get("sub_cat");
                                        androidx.fragment.app.Fragment fm = null;
                                        args.putString("cat_id", sub_cat);
                                        args.putString("title",name.get("slider_title"));
                                        session_management.setCategoryId(sub_cat);
                                        if(name.get("parent").equals("0"))
                                        {
                                            fm=new SubcategoryFragment();
                                        }
                                        else {
                                            //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                            fm = new ProductFragment();
                                        }
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(req);

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
                            categoryAdapter = new Home_adapter(categoryList);
                            LinearLayoutManager HorizontalLayout
                                    = new LinearLayoutManager(
                                    getActivity(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false);
                            rec_category.setLayoutManager(HorizontalLayout);
                            rec_category.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
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

    private void new_products() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_NEW_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("sels", response.toString());
                //Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<NewProductModel>>() {
                            }.getType();
                            newProductList = gson.fromJson(response.getString("new_product"), listType);
                            newProductAdapter = new NewProductAdapter(newProductList,getActivity());
                            rec_new_product.setLayoutManager(new GridLayoutManager(getActivity(),2));
                            rec_new_product.setAdapter(newProductAdapter);
                            newProductAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("Error0",e.toString());
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void make_top_selling() {

        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("sels", response.toString());
                //Toast.makeText(getActivity(),""+response.toString(),Toast.LENGTH_LONG).show();

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            loadingBar.dismiss();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<NewProductModel>>() {
                            }.getType();
                            top_selling_models = gson.fromJson(response.getString("top_selling_product"), listType);
                            top_selling_adapter = new NewProductAdapter(top_selling_models,getActivity());
                            rec_top_product.setLayoutManager(new GridLayoutManager(getActivity(),2));
                            rec_top_product.setAdapter(top_selling_adapter);
                            top_selling_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
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