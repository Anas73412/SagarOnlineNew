package com.ecom.sagaronline.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Activity.NoInternetConnection;
import com.ecom.sagaronline.Adapter.NewProductAdapter;
import com.ecom.sagaronline.Adapter.SortAdapter;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.Model.CategoryModel;
import com.ecom.sagaronline.Model.GetCongifDataModel;
import com.ecom.sagaronline.Model.NewProductModel;
import com.ecom.sagaronline.Model.SliderSubcatModel;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.DatabaseCartHandler;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.OnGetConfigData;
import com.ecom.sagaronline.Utils.Session_management;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements View.OnClickListener {

    private static String TAG = ProductFragment.class.getSimpleName();
    private RecyclerView rv_cat;
    LoadingBar loadingBar;
    Module module;
    ImageView no_product;
    Session_management session_management;
    FloatingActionButton float_filter;
    private TabLayout tab_cat ;
    String max_slab,slab_value;
    private LinearLayout tab_filter;
    private ImageView tab_grid , tab_sort ;
    private List<CategoryModel> category_modelList = new ArrayList<>();
    private List<SliderSubcatModel> slider_subcat_models = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();
    private int price_list[]={} ;
    private List<NewProductModel> product_modelList = new ArrayList<>();
    private List<NewProductModel> product_grid_modelList = new ArrayList<>();
    private List<NewProductModel> price_product_list = new ArrayList<>();

    //  private Product_adapter adapter_product;
    private NewProductAdapter gridAdapter ;
    private SortAdapter sortAdapter ;
    //private SliderLayout  banner_slider;
    String language;
    LoadingBar loadingBar1;
    String getcat_id ;
    String  min, max;
    int min_value=0 , max_value =0 ,preMin = -1 ,preMax =-1;
    SharedPreferences preferences;
    DatabaseCartHandler db_cart;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
        View view =  inflater.inflate(R.layout.fragment_product, container, false);


        loadingBar = new LoadingBar(getActivity());
        module=new Module(getActivity());

        float_filter = view.findViewById(R.id.flot_filter);
        tab_cat = (TabLayout) view.findViewById( R.id.tab_cat);
        tab_filter =(LinearLayout) view.findViewById( R.id.tab_layout );
        tab_grid = (ImageView) view.findViewById( R.id.grid );
        tab_sort =(ImageView) view.findViewById( R.id.sort );
        tab_grid.setOnClickListener( this );
        tab_sort.setOnClickListener( this );

        session_management=new Session_management(getActivity());

        //   banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        rv_cat =  view.findViewById( R.id.rv_subcategory);

        no_product =(ImageView)view.findViewById( R.id.img_no_product );

        rv_cat.setLayoutManager(new LinearLayoutManager(getActivity()));
        getcat_id = getArguments().getString("cat_id");
        max_slab = getArguments().getString("max_slab");
        slab_value=getArguments().getString("slab_value");
        String id = getArguments().getString("id");
        String get_deal_id = getArguments().getString("cat_deal");
        String get_top_sale_id = getArguments().getString("cat_top_selling");
        String getcat_title = getArguments().getString("title");
        max = getArguments().getString("max");
        min = getArguments().getString("min");
        Log.e("DATA","min"+min+"max"+max+"max_slab"+max_slab+"slab"+slab_value);

        db_cart=new DatabaseCartHandler(getActivity());
        ((MainActivity) getActivity()).setTitle(getcat_title);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetProductRequest(getcat_id);
            maketopsaleProductRequest(get_top_sale_id);
            makeGetSliderCategoryRequest(id);

        }
        else
        {
            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
            getActivity().startActivity(intent);
        }

        float_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slab_value.equalsIgnoreCase("")||slab_value==null)
                    Toast.makeText(getActivity(),"No Filter Available",Toast.LENGTH_LONG).show();
                else
                    getFilterFragment();
                                                /*final Dialog dialog = new Dialog(getActivity());
                                                dialog.setContentView(R.layout.seekbar_dialog);
                                                dialog.setCanceledOnTouchOutside(true);
                                                org.florescu.android.rangeseekbar.RangeSeekBar rangeBar = dialog.findViewById(R.id.rangebar);
                                                Button btn_go = dialog.findViewById(R.id.btn_go);


                                                    for (int i = 0; i < product_modelList.size(); i++) {
                                                        if (max_value < Integer.parseInt(product_modelList.get(i).getPrice())) {
                                                            max_value = Integer.parseInt(product_modelList.get(i).getPrice());
                                                        }


                                                    }


                                                rangeBar.setRangeValues(min_value,max_value);
                                                rangeBar.setNotifyWhileDragging(true);
                                                rangeBar.setTextAboveThumbsColor(R.id.txtColor);
                                                rangeBar.setOnRangeSeekBarChangeListener(new org.florescu.android.rangeseekbar.RangeSeekBar.OnRangeSeekBarChangeListener() {
                                                    @Override
                                                    public void onRangeSeekBarValuesChanged(org.florescu.android.rangeseekbar.RangeSeekBar bar, Object minValue, Object maxValue) {
                                                     preMax= (int) bar.getSelectedMaxValue();
                                                     preMin= (int) bar.getSelectedMinValue();
                                                     price_product_list.clear();
                                                        for (int i = 0 ; i <product_modelList.size();i++)
                                                        {
                                                            Product_model model = product_modelList.get(i);
                                                            int price = Integer.parseInt(model.getPrice());
                                                            if (price <= preMax && price >= preMin )
                                                            {
                                                                price_product_list.add(model);
                                                            }

                                                        }
                                                        int mx=(int)bar.getAbsoluteMaxValue();
                                                        int mn=(int)bar.getAbsoluteMinValue();
                                                        if(price_product_list.isEmpty())
                                                        {
                                                            no_product.setVisibility(View.VISIBLE);
                                                            rv_cat.setVisibility(View.GONE);
                                                        }
                                                        else {
                                                            no_product.setVisibility(View.GONE);
                                                            rv_cat.setVisibility(View.VISIBLE);
                                                            gridAdapter = new gridAdapter(price_product_list, getActivity());
                                                            rv_cat.setAdapter(gridAdapter);
                                                            gridAdapter.notifyDataSetChanged();
                                                        }
//                                                     Toast.makeText(getActivity(),"values "+ preMax +preMin,Toast.LENGTH_LONG).show();
//                                                     Log.e("values:", String.valueOf(preMax));

                                                    }
                                                });
                                                btn_go.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {



                                                        dialog.dismiss();

                                                    }

                                                });






                                                dialog.show();*/

            }
        });
//
        tab_cat.setSelectedTabIndicatorColor(getActivity().getResources().getColor( R.color.white));

        tab_cat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String getcat_id = cat_menu_id.get(tab.getPosition());
                if (ConnectivityReceiver.isConnected()) {
                    //Shop By Catogary Products
                    //makeGetProductRequest(getcat_id);
                    ((MainActivity) getActivity()).setTitle(String.valueOf( tab.getText() ));
                    if(getcat_id.isEmpty())
                    {
                        no_product.setVisibility(View.VISIBLE);
                        rv_cat.setVisibility( View.GONE );
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    private void getFilter() {
        if (max!=null&&min!=null)
        {
            price_product_list.clear();
            Log.e("PRICE!",String.valueOf(product_modelList.size()));
            for (int i = 0 ; i <product_modelList.size();i++)
            {
                Log.e("PRICE!",product_modelList.get(i).getPrice());
                NewProductModel model = product_modelList.get(i);
                int price = Integer.parseInt(model.getPrice());
                if (price <= Integer.valueOf(max) && price >= Integer.valueOf(min) )
                {
                    Log.e("PRICE",String.valueOf(price));
                    price_product_list.add(model);
                }

            }
            Log.e("DATA1",price_product_list.toString());
            if(price_product_list.isEmpty())
            {
                no_product.setVisibility(View.VISIBLE);
                rv_cat.setVisibility(View.GONE);
            }
            else {
                no_product.setVisibility(View.GONE);
                rv_cat.setVisibility(View.VISIBLE);
                gridAdapter = new NewProductAdapter(price_product_list, getActivity(),"p");
                rv_cat.setAdapter(gridAdapter);
                gridAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getFilterFragment() {
        Bundle args = new Bundle();
       Fragment fm = new FilterFragment();
        args.putString("slab_value",slab_value);
        args.putString("max_slab",max_slab);
        args.putString("cat_id",getcat_id);
        fm.setArguments(args);
       FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace( R.id.fragment_container, fm)
                .addToBackStack(null).commit();
    }


    @Override
    public void onStart() {
        super.onStart();
        //  loadingBar.show();

    }

    /**
     * Method to make json object request where json response starts wtih
     */
    //Get Shop By Catogary
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
                            tab_cat.setVisibility(View.VISIBLE);
                            cat_menu_id.clear();
                            for (int i = 0; i < category_modelList.size(); i++) {
                                cat_menu_id.add(category_modelList.get(i).getId());

                                // preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
                                tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getTitle()));
                                /*language=preferences.getString("language","");
                                if (language.contains("english")) {
                                    tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getTitle()));
                                }
                                else {
                                    tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getArb_title()));

                                }*/
                                // ((MainActivity) getActivity()).setTitle(String.valueOf(category_modelList.get(i).getTitle()  ));
                            }
                            loadingBar.dismiss();
                        } else {
                            loadingBar.dismiss();
                            makeGetProductRequest(parent_id);


                        }

                    }
                } catch (JSONException e) {
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

    //Get Shop By Catogary Products
    private void makeGetProductRequest(String cat_id) {

        loadingBar1 = new LoadingBar(getActivity());
        loadingBar1.show();


        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("rett " +
                        "", response.toString());
                loadingBar1.dismiss();
                try {
//                    if(response.has("data"))
//                    {
//
//                        no_product.setVisibility( View.GONE );
//                        rv_cat.setVisibility( View.VISIBLE );
//                    }
//                    else
//                    {
//
//                        no_product.setVisibility( View.VISIBLE );
//                        rv_cat.setVisibility( View.GONE );
//                    }

                    Boolean status = response.getBoolean("responce");

                    if (status) {
                        ///         Toast.makeText(getActivity(),""+response.getString("data"),Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewProductModel>>() {
                        }.getType();
                        if (response.has( "data" )) {
                            product_modelList = gson.fromJson(response.getString("data"), listType);
                        }
                        else
                        {
                            loadingBar1.dismiss();

                            no_product.setVisibility( View.VISIBLE );
                            rv_cat.setVisibility( View.GONE );
                        }
//                            loadingBar.dismiss();
                        //   adapter_product = new Product_adapter(product_modelList, getActivity());
                        gridAdapter = new NewProductAdapter(product_modelList, getActivity(),"p");
                        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(), 2 );
                        rv_cat.setLayoutManager( gridLayoutManager );
                        rv_cat.setAdapter( gridAdapter );
                        gridAdapter.notifyDataSetChanged();
                        if (getActivity() != null) {

                            if (product_modelList.isEmpty()) {

                                loadingBar1.dismiss();

                                no_product.setVisibility( View.VISIBLE );
                                rv_cat.setVisibility( View.GONE );
                                //Toast.makeText(getActivity(), getResources().getString( R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                no_product.setVisibility( View.GONE);
                                rv_cat.setVisibility( View.VISIBLE );
                            }

                        }
//                        }
//                        else
//                        {
//                            no_product.setVisibility( View.VISIBLE );
//                            rv_cat.setVisibility( View.GONE );
//                        }
                        getFilter();

                    }
                } catch (JSONException e) {
                    //   e.printStackTrace();
                    String ex=e.getMessage();




                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar1.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



    //Get Shop By Catogary
    private void makeGetSliderCategoryRequest(final String sub_cat_id) {
        loadingBar.show();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sub_cat", sub_cat_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_SLIDER_CATEGORY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("sub_cat_slider", response.toString());
                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        no_product.setVisibility(View.GONE);
                        rv_cat.setVisibility(View.VISIBLE);
                        Gson gson = new Gson();
                        loadingBar.dismiss();
                        Type listType = new TypeToken<List<SliderSubcatModel>>() {}.getType();
                        slider_subcat_models = gson.fromJson(response.getString("subcat"), listType);
                        if (!slider_subcat_models.isEmpty()) {

                            if(slider_subcat_models.size()==1)
                            {
                                tab_cat.setVisibility(View.GONE);
                            }
                            else if(slider_subcat_models.size()<=0)
                            {
                                tab_cat.setVisibility(View.GONE);
                                rv_cat.setVisibility(View.GONE);
                                no_product.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                tab_cat.setVisibility(View.VISIBLE);
                                no_product.setVisibility(View.GONE);
                                rv_cat.setVisibility(View.VISIBLE);
                            }
                            cat_menu_id.clear();
                            for (int i = 0; i < slider_subcat_models.size(); i++) {
                                cat_menu_id.add(slider_subcat_models.get(i).getId());
                                preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);

                                language=preferences.getString("language","");
                                if (language.contains("english")) {

                                    tab_cat.addTab(tab_cat.newTab().setText(slider_subcat_models.get(i).getTitle()));
                                }
                                else {
                                    tab_cat.addTab(tab_cat.newTab().setText(slider_subcat_models.get(i).getTitle()));
//                                    gifImageView.setVisibility( View.VISIBLE );
//                                    no_product.setVisibility( View.VISIBLE );

                                }
                            }
                        } else {
//                               rv_cat.setVisibility(View.GONE);
//                               no_product.setVisibility(View.VISIBLE);

                            //     makeGetProductRequest(getcat_id);
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

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }




    ////Get DEal Products
    private void makeDescendingProductRequest(String cat_id) {
        String tag_json_obj = "json_product_desc_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);
        loadingBar.show();
        product_modelList.clear();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_DESC, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewProductModel>>() {
                        }.getType();
//                        if (response.has( "data" )) {
                        product_modelList = gson.fromJson( response.getString( "data" ), listType );
                        no_product.setVisibility( View.GONE );
                        rv_cat.setVisibility( View.VISIBLE);
                        //  adapter_product = new Product_adapter(product_modelList, getActivity());
                        gridAdapter = new NewProductAdapter(product_modelList, getActivity(),"p");
                        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(), 2 );
                        rv_cat.setLayoutManager( gridLayoutManager );
                        rv_cat.setAdapter( gridAdapter );
                        gridAdapter.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                Toast.makeText( getActivity(), "No Record Found", Toast.LENGTH_SHORT ).show();
                            } else {
                                //  Toast.makeText(getActivity(),""+response, Toast.LENGTH_SHORT).show();
                            }
                        }
//                        }
                        else
                        {
                            no_product.setVisibility( View.VISIBLE );
                            rv_cat.setVisibility( View.GONE );
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
    private void makeAscendingProductRequest(String cat_id) {
        String tag_json_obj = "json_product_asc_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);
        product_modelList.clear();
        loadingBar.show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_ASC, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewProductModel>>() {
                        }.getType();
//                        if (response.has( "data" )) {
                        product_modelList = gson.fromJson( response.getString( "data" ), listType );
                        no_product.setVisibility( View.GONE );
                        rv_cat.setVisibility( View.VISIBLE );
                        //    adapter_product = new Product_adapter(product_modelList, getActivity());
                        gridAdapter = new NewProductAdapter(product_modelList, getActivity(),"p");
                        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(), 2 );

                        rv_cat.setLayoutManager( gridLayoutManager );
                        rv_cat.setAdapter( gridAdapter );
                        gridAdapter.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                no_product.setVisibility( View.VISIBLE );
                                rv_cat.setVisibility( View.GONE );
                                Toast.makeText( getActivity(), "No Record Found", Toast.LENGTH_SHORT ).show();
                            } else {
                                //  Toast.makeText(getActivity(),""+response, Toast.LENGTH_SHORT).show();
                            }
                        }
//                        }
                        else
                        {
                            no_product.setVisibility( View.VISIBLE );
                            rv_cat.setVisibility( View.GONE );
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

    private void makeNewestProductRequest(String cat_id) {
        String tag_json_obj = "json_product_newest_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);
        product_modelList.clear();
        loadingBar.show();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_NEWEST, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewProductModel>>() {
                        }.getType();
                        if (response.has("data" )) {
                            product_modelList = gson.fromJson( response.getString( "data" ), listType );
                            no_product.setVisibility( View.GONE);
                            rv_cat.setVisibility( View.VISIBLE);
                            //  adapter_product = new Product_adapter(product_modelList, getActivity());
                            gridAdapter = new NewProductAdapter(product_modelList, getActivity(),"p");
                            GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(), 2 );
                            rv_cat.setLayoutManager( gridLayoutManager );
                            rv_cat.setAdapter( gridAdapter );
                            gridAdapter.notifyDataSetChanged();
                            if (getActivity() != null) {
                                if (product_modelList.isEmpty()) {
                                    no_product.setVisibility( View.VISIBLE );
                                    rv_cat.setVisibility( View.GONE );
                                    Toast.makeText( getActivity(), "No Record Found", Toast.LENGTH_SHORT ).show();
                                } else {
                                    //  Toast.makeText(getActivity(),""+response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else
                        {
                            no_product.setVisibility( View.VISIBLE );
                            rv_cat.setVisibility( View.GONE );
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




    ////Get Top Sale Products
    private void maketopsaleProductRequest(String cat_id) {
        loadingBar.show();
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("top_selling_product", cat_id);
        product_modelList.clear();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("salesss", response.toString());

                try {
                    loadingBar.dismiss();
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NewProductModel>>() {
                        }.getType();
//                        if (response.has( "data" )) {
                        product_modelList = gson.fromJson( response.getString( "top_selling_product" ), listType );
                        // adapter_product = new Product_adapter(product_modelList, getActivity());
                        gridAdapter = new NewProductAdapter(product_modelList, getActivity(),"p");
                        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(), 2 );
                        rv_cat.setLayoutManager( gridLayoutManager );
                        rv_cat.setAdapter( gridAdapter );
                        gridAdapter.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                loadingBar.dismiss();
                                no_product.setVisibility( View.VISIBLE );
                                rv_cat.setVisibility( View.GONE );
                                Toast.makeText( getActivity(), "No Record Found", Toast.LENGTH_SHORT ).show();
                            }
                        }
//                        }
//                        else
//                        {
//                            no_product.setVisibility( View.VISIBLE );
//                            rv_cat.setVisibility( View.GONE );
//                        }
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mCart,new IntentFilter("Cart"));
    }
    // broadcast reciver for receive data
    // broadcast reciver for receive data



    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        final String cat_id = getArguments().getString("cat_id");
        String p_id = getArguments().getString("id");
        String get_deal_id = getArguments().getString("cat_deal");
        String get_top_sale_id = getArguments().getString("cat_top_selling");
        String getcat_title = getArguments().getString("title");

        if (id == R.id.sort)
        {
            final ArrayList <String>  sort_List = new ArrayList<>(  );
            sort_List.add( "Price Low - High" );
            sort_List.add("Price High - Low");
            sort_List.add("Newest First");
            //  sort_List.add ("Trending");
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=layoutInflater.inflate( R.layout.dialog_sort_layout,null);
            ListView l1=(ListView)row.findViewById( R.id.list_sort);
            sortAdapter=new SortAdapter(getActivity(),sort_List);
            //productVariantAdapter.notifyDataSetChanged();
            l1.setAdapter(sortAdapter);
            builder.setView(row);
            final AlertDialog ddlg=builder.create();
            ddlg.show();
            l1.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ddlg.dismiss();
                    String item = sort_List.get( i ).toString();

                    String c_id=session_management.getCategoryId();
                    if (item.equals( "Price Low - High" ))
                    {
                        // ddlg.dismiss();
                        makeAscendingProductRequest( c_id );
                        tab_grid.setImageResource( R.drawable.icons8_activity_grid_2_48px);
                        tab_grid.setTag( "grid" );
                    }
                    else if(item.equals( "Price High - Low" ))
                    {
                        // Toast.makeText( getActivity(), "category id :" +getcat_id, Toast.LENGTH_SHORT ).show();
                        // ddlg.dismiss();
                        makeDescendingProductRequest(c_id);
                        tab_grid.setImageResource( R.drawable.icons8_activity_grid_2_48px);
                        tab_grid.setTag( "grid" );


                    }
                    else if(item.equals( "Newest First" ))
                    {
                        // ddlg.dismiss();
                        makeNewestProductRequest( c_id );
                        tab_grid.setImageResource( R.drawable.icons8_activity_grid_2_48px);
                        tab_grid.setTag( "grid" );
                    }
                    else if (item.equals( "Trending" ))
                    {

                    }

                    // Toast.makeText( getActivity(),"Showing items:" +item,Toast.LENGTH_LONG ).show();
                }
            } );
        }
        else if (id == R.id.grid)
        {
            String tag = String.valueOf( tab_grid.getTag() );
            if(tag.equals( "grid" )) {
                tab_grid.setImageResource( R.drawable.icons8_menu_48px );
                tab_grid.setTag( "linear" );

                gridAdapter = new NewProductAdapter( product_modelList, getActivity(),"p" );
                rv_cat.setLayoutManager( new GridLayoutManager( getActivity(), 2 ) );
                rv_cat.setAdapter( gridAdapter );
                //   gridAdapter.notifyDataSetChanged();
            }
            else if (tag.equals( "linear" ))
            {
                tab_grid.setImageResource( R.drawable.icons8_activity_grid_2_48px);
                tab_grid.setTag( "grid" );

//              adapter_product = new Product_adapter( product_modelList, getActivity() );
//                rv_cat.setLayoutManager( new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
//                rv_cat.setAdapter(adapter_product );
            }

        }
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


}