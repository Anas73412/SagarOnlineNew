package com.ecom.sagaronline.Fragments;

//import android.app.FragmentManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Adapter.Home_adapter;
import com.ecom.sagaronline.Adapter.NewProductAdapter;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.Model.BannerModel;
import com.ecom.sagaronline.Model.CategoryModel;
import com.ecom.sagaronline.Model.NewProductModel;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.DatabaseCartHandler;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.RecyclerTouchListener;
import com.ecom.sagaronline.Utils.Session_management;
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


public class HomeFragment extends Fragment {

    Module module;
    CarouselView carouselView,carousel_banner;
    Session_management session_management ;
    LoadingBar loadingBar;
    NewProductAdapter newProductAdapter , top_selling_adapter;
    List<CategoryModel> categoryList;
    DatabaseCartHandler db_cart;
    List<NewProductModel> newProductList , top_selling_models;
    Home_adapter categoryAdapter;
    RecyclerView rec_category , rec_new_product , rec_top_product;
    String getid="";
    RelativeLayout rel_search;
    ArrayList<BannerModel> bList=new ArrayList<>();
    private boolean isSubcat = false;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        module=new Module(getActivity());
        loadingBar = new LoadingBar(getActivity());
        session_management = new Session_management(getActivity());
        categoryList = new ArrayList<>();
        newProductList = new ArrayList<>();
        carouselView = v.findViewById(R.id.carousel);
        carousel_banner = v.findViewById(R.id.carousel_banner);
        rec_category = v.findViewById(R.id.rec_category);
        rec_new_product = v.findViewById(R.id.rec_new_product);
        rec_top_product = v.findViewById(R.id.rec_best_product);
        rel_search = v.findViewById(R.id.rel_search);
        db_cart=new DatabaseCartHandler(getActivity());
        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Search_fragment()).addToBackStack(null).commit();
            }
        });
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Light_Dialog_Alert);
//                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //((MainActivity) getActivity()).finish();
                            getActivity().finishAffinity();


                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    final AlertDialog dialog=builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        }
                    });
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
        makeGetSliderRequest();
        makeGetCategoryRequest();
        makeGetBannerRequest();
        new_products();
        make_top_selling();
        rec_category.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rec_category, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = categoryList.get(position).getId();
                String title=categoryList.get(position).getTitle();
                String parent=categoryList.get(position).getCount();
                String slab_value=categoryList.get(position).getSlab_value();
                String max_slab=categoryList.get(position).getMax_slab_amt();

                if(parent.equals("0"))
                {
                    Bundle args = new Bundle();
                   Fragment fm = new ProductFragment();
                    args.putString("cat_id", getid);
                    args.putString( "title" ,title );
                    args.putString("slab_value",slab_value);
                    args.putString("max_slab",max_slab);
                    session_management.setCategoryId(getid);
                    // args.putString( "" );
                    // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                    fm.setArguments(args);
                    androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace( R.id.fragment_container, fm)
                            .addToBackStack(null).commit();

                }
                else
                {
                    Bundle args = new Bundle();
              Fragment fm = new SubcategoryFragment();
                    args.putString("cat_id", getid);
                    args.putString( "title" ,title );
                    // args.putString( "" );
                    // Toast.makeText(getActivity(),""+getid,Toast.LENGTH_LONG).show();
                    fm.setArguments(args);
                 androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace( R.id.fragment_container, fm)
                            .addToBackStack(null).commit();

                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
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
                                      Fragment fm = null;
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

    private void makeGetBannerRequest() {
         bList.clear();
        JsonArrayRequest req = new JsonArrayRequest( BaseURL.GET_BANNER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("banners", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                BannerModel bannerModel=new BannerModel();
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                JSONObject bannerObj=jsonObject.getJSONObject("banner");
                                String catStr=jsonObject.getString("category_info");


                                bannerModel.setId(bannerObj.getString("id"));
                                bannerModel.setSlider_title(bannerObj.getString("slider_title"));
                                bannerModel.setSlider_image(bannerObj.getString("slider_image"));
                                bannerModel.setSlider_status(bannerObj.getString("slider_status"));
                                bannerModel.setSub_cat(bannerObj.getString("sub_cat"));
                                if(module.checkNullCondition(catStr)){
                                    bannerModel.setCat_id("0");
                                    bannerModel.setTitle("");
                                    bannerModel.setParent("");
                                    bannerModel.setLeval("");
                                    bannerModel.setSlab_value("");
                                    bannerModel.setMax_slab_amt("");
                                }else{
                                    JSONObject catObj=jsonObject.getJSONObject("category_info");
                                    bannerModel.setCat_id(catObj.getString("id"));
                                    bannerModel.setTitle(catObj.getString("title"));
                                    bannerModel.setParent(catObj.getString("parent"));
                                    bannerModel.setLeval(catObj.getString("leval"));
                                    bannerModel.setSlab_value(catObj.getString("slab_value"));
                                    bannerModel.setMax_slab_amt(catObj.getString("max_slab_amt"));
                                }
                                bList.add(bannerModel);

//                                HashMap<String, String> url_maps = new HashMap<String, String>();
//                                url_maps.put("slider_title", bannerObj.getString("slider_title"));
//                                url_maps.put("sub_cat", bannerObj.getString("sub_cat"));
//                                Log.e("nammasd", "onResponse: "+catStr );
//                                if(!module.checkNullCondition(catStr)) {
//                                    catObj=new JSONObject(catStr);
//                                    url_maps.put("parent", catObj.getString("parent"));
//                                    url_maps.put("leval", catObj.getString("leval"));
//                                    url_maps.put("max_slab",catObj.getString("max_slab_amt"));
//                                    url_maps.put("slab_value",catObj.getString("slab_value"));
//                                    Log.e("home_dat", "onClick: "+catObj.getString("max_slab_amt")+" :: "+catObj.getString("slab_value") );
//                                }else{
//                                    url_maps.put("parent", "");
//                                    url_maps.put("leval", "");
//                                    url_maps.put("max_slab","");
//                                    url_maps.put("slab_value","");

//                                }
//
//                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + bannerObj.getString("slider_image"));
//                                listarray.add(url_maps);

                            }
                            for(int j=0;j<bList.size();j++){
//                                Log.e("bLiasidasd", "onResponse: "+bList.get(j).getCat_id()+" :: "+bList.get(j).getSlab_value() );
                                carousel_banner.setImageListener(new ImageListener() {
                                    @Override
                                    public void setImageForPosition(int position, ImageView imageView) {
                                        Glide.with(getActivity()).load(BaseURL.IMG_SLIDER_URL +bList.get(position).getSlider_image()).into(imageView);
                                    }
                                });
                                carousel_banner.setPageCount(bList.size());



                            }

                                carousel_banner.setImageClickListener(new ImageClickListener() {
                                    @Override
                                    public void onClick(int position) {

                                        Bundle args = new Bundle();
                                        String sub_cat = bList.get(position).getSub_cat();
                                        Fragment fm = null;
                                        args.putString("cat_id", sub_cat);
                                        args.putString("title", bList.get(position).getSlider_title());
                                        args.putString("max_slab", bList.get(position).getMax_slab_amt());
                                        args.putString("slab_value", bList.get(position).getSlab_value());
                                        session_management.setCategoryId(sub_cat);

                                        if(sub_cat.equals("0")){
                                            fm=null;
                                        }
                                        else{
                                            if (bList.get(position).getParent().equals("0")) {
                                                fm = new SubcategoryFragment();
                                            } else {
                                                fm = new ProductFragment();
                                            }
                                        }
                                        if(fm!=null) {
                                            fm.setArguments(args);
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                                                    .addToBackStack(null).commit();
                                        }
                                    }
                                });
//
//                            for (final HashMap<String, String> name : listarray) {
//

//
//                            }
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
                            categoryAdapter = new Home_adapter(categoryList,"h");
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
                            newProductAdapter = new NewProductAdapter(newProductList,getActivity(),"new");
                            rec_new_product.setLayoutManager(new GridLayoutManager(getActivity(),2));
                            rec_new_product.setAdapter(newProductAdapter);
                            newProductAdapter.notifyDataSetChanged();

                        }
                    }
                } catch (JSONException e) {
                    Log.e("Error",e.toString());
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
                            top_selling_adapter = new NewProductAdapter(top_selling_models,getActivity(),"top");
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

    BroadcastReceiver mCart=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type=intent.getStringExtra("type");
            if(type.contentEquals("update")){
                updateData();
            }
        }
    };

    private void updateData() {
        ((MainActivity)getActivity()).setCartCounter(db_cart.getCartCount());
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mCart,new IntentFilter("Cart"));
    }
}