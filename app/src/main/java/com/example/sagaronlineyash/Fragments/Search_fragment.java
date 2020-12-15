package com.example.sagaronlineyash.Fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sagaronlineyash.Activity.MainActivity;
import com.example.sagaronlineyash.Adapter.HistoryAdapter;
import com.example.sagaronlineyash.Adapter.SearchAdapter;
import com.example.sagaronlineyash.Adapter.SuggestionAdapter;
import com.example.sagaronlineyash.AppController;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.Model.NewProductModel;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.ConnectivityReceiver;
import com.example.sagaronlineyash.Utils.CustomVolleyJsonRequest;
import com.example.sagaronlineyash.Utils.LoadingBar;
import com.example.sagaronlineyash.Utils.RecyclerTouchListener;
import com.example.sagaronlineyash.Utils.SearchHistoryHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.sagaronlineyash.Utils.SearchHistoryHandler.HISTORY_NAME;


public class Search_fragment extends Fragment {

  Module module;
  private static String TAG = Search_fragment.class.getSimpleName();
  private AutoCompleteTextView acTextView;
  SearchHistoryHandler searchHistoryHandler;
  private CardView btn_search;
  private RecyclerView rv_search,rv_history;
  ImageView img_no_products;
  private List<NewProductModel> product_modelList = new ArrayList<>();
  private SearchAdapter adapter_product;
  LoadingBar loadingBar;

  public Search_fragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    loadingBar = new LoadingBar(getActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);

    ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.search));
    module=new Module(getActivity());
    loadingBar = new LoadingBar(getActivity());
    acTextView = (AutoCompleteTextView) view.findViewById(R.id.et_search);
    img_no_products = (ImageView) view.findViewById(R.id.img_no_products);
    searchHistoryHandler = new SearchHistoryHandler(getActivity());
    acTextView.setAdapter(new SuggestionAdapter(getActivity(), acTextView.getText().toString()));

    acTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
    btn_search = view.findViewById(R.id.btn_search);
    rv_search = (RecyclerView) view.findViewById(R.id.rv_search);
    rv_history = view.findViewById(R.id.history);
    rv_search.setLayoutManager(new GridLayoutManager(getActivity(),2));
    ArrayList<HashMap<String ,String>> search = searchHistoryHandler.getHistoryAll();
    rv_history.setAdapter(new HistoryAdapter(search));
    rv_history.setLayoutManager(new GridLayoutManager(getActivity(),2));
    btn_search.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String history_text = acTextView.getText().toString();
        String get_search_txt ="%"+ acTextView.getText().toString() +"%";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        Date date = cal.getTime();
        String dateString=sdf.format(date);
        if (acTextView.length()<=0) {
          acTextView.setError( "Enter product to search" );
          acTextView.requestFocus();
          // Toast.makeText(getActivity(), getResources().getString(R.string.enter_keyword), Toast.LENGTH_SHORT).show();
        }
        else {
          if (ConnectivityReceiver.isConnected()) {
            makeGetProductRequest(get_search_txt);
            searchHistoryHandler.setHistory(history_text,dateString);
          } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
          }
        }

      }
    });

    rv_search.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_search, new RecyclerTouchListener.OnItemClickListener() {
      @Override
      public void onItemClick(View view, int position) {



        Fragment details_fragment = new DetailsFragment();
        // bundle.putString("data",as);
        Bundle args = new Bundle();

        //Intent intent=new Intent(context, Product_details.class);
        args.putString("cat_id", product_modelList.get(position).getCategory_id());
        args.putString("product_id", product_modelList.get(position).getProduct_id());
        args.putString("product_images", product_modelList.get(position).getProduct_image());
        args.putString("product_name", product_modelList.get(position).getProduct_name());
        args.putString("product_description", product_modelList.get(position).getProduct_description());
        args.putString("stock", product_modelList.get(position).getStock());
        args.putString("in_stock", product_modelList.get(position).getIn_stock());
        args.putString("product_attribute", product_modelList.get(position).getProduct_attribute());
        args.putString("price", product_modelList.get(position).getPrice());
        args.putString("mrp", product_modelList.get(position).getMrp());
        args.putString("unit_value", product_modelList.get(position).getUnit_value());
        args.putString("unit", product_modelList.get(position).getUnit());
        args.putString("rewards", product_modelList.get(position).getRewards());
        args.putString("increment", product_modelList.get(position).getIncreament());
        args.putString("title", product_modelList.get(position).getTitle());
        details_fragment.setArguments(args);


        // Toast.makeText(getActivity(),"col"+product_modelList.get(position).getColor(),Toast.LENGTH_LONG).show();


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, details_fragment)

                .addToBackStack(null).commit();


      }

      @Override
      public void onLongItemClick(View view, int position) {

      }
    }));
    rv_history.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_history, new RecyclerTouchListener.OnItemClickListener() {
      @Override
      public void onItemClick(View view, int position) {

        String history_text = search.get(position).get(HISTORY_NAME);
        String get_search_txt ="%"+ history_text +"%";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
        Date date = cal.getTime();
        String dateString=sdf.format(date);
        if (ConnectivityReceiver.isConnected()) {
          makeGetProductRequest(get_search_txt);
          searchHistoryHandler.setHistory(history_text,dateString);
        } else {
          ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

      }

      @Override
      public void onLongItemClick(View view, int position) {

      }
    }));
    return view;
  }


   // Method to make json object request where json response starts wtih {

  private void makeGetProductRequest(String search_text) {

    // Tag used to cancel the request
    String tag_json_obj = "json_product_req";

    loadingBar.show();
    Map<String, String> params = new HashMap<String, String>();
    params.put("search", search_text);

    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
            BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

      @Override
      public void onResponse(JSONObject response) {
        Log.d("search", response.toString());

        try {
          loadingBar.dismiss();
          if(response.has("data"))
          {
            img_no_products.setVisibility(View.GONE);
            rv_search.setVisibility(View.VISIBLE);
            rv_history.setVisibility(View.GONE);
          }
          else
          {
            img_no_products.setVisibility(View.VISIBLE);
            rv_search.setVisibility(View.GONE);
            rv_history.setVisibility(View.GONE);
          }
          Boolean status = response.getBoolean("responce");
          if (status) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<NewProductModel>>() {
            }.getType();

            product_modelList = gson.fromJson(response.getString("data"), listType);

            adapter_product = new SearchAdapter(product_modelList, getActivity());
            rv_search.setAdapter(adapter_product);
            adapter_product.notifyDataSetChanged();


            if (getActivity() != null) {
              if (product_modelList.isEmpty()) {
                Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
              }
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
          Toast.makeText(getActivity(),""+msg, Toast.LENGTH_LONG).show();
        }
      }
    });

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
  }

}
