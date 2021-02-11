package com.ecom.sagaronline.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Activity.NoInternetConnection;
import com.ecom.sagaronline.Adapter.My_order_detail_adapter;
import com.ecom.sagaronline.AppController;
import com.ecom.sagaronline.Config.BaseURL;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.Model.My_order_detail_model;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.CustomVolleyJsonArrayRequest;
import com.ecom.sagaronline.Utils.CustomVolleyJsonRequest;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.sagaronline.Config.BaseURL.KEY_ID;
import static com.ecom.sagaronline.Config.BaseURL.KEY_MOBILE;
import static com.ecom.sagaronline.Config.BaseURL.KEY_NAME;


public class OrderDetailsFragment extends Fragment {

//  Context getActivity()= OrderDetailsFragment.this;
    TextView txt_order_id ,txt_tot_item,txt_p_chrges,txt_s_chrge,txt_tot_chrge,
            txt_cust_name ,txt_cust_add ,txt_cust_mobile;
    RecyclerView rv_products ;
    String sale_id="";
    Dialog dialog;
   Session_management sessionManagement ;
    String user_name , user_mobile ,user_address ;
    ArrayList<My_order_detail_model> itemlist;
    My_order_detail_adapter detailsAdapter;
    ImageView back ;
    LoadingBar loadingBar;
    EditText et_remark;
    Button btn_yes,btn_no ,btn_cancel ,btn_reciept;
    Module module;

   SwipeRefreshLayout swipeRefreshLayout;
    Toolbar mActionBarToolbar;
    private static final int PERMISSION_REQUEST_CODE = 1;

    String user_id ;

    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_order_details, container, false);
        initViews(v);
        return v ;
    }

 

    private void initViews(View v) {
//        getSupportActionBar().setTitle("Order Details");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_order_id = v.findViewById(R.id.tv_order_id);
        module=new Module(getActivity());
        txt_p_chrges = v.findViewById(R.id.tv_product_chrge);
        txt_s_chrge = v.findViewById(R.id.tv_shipping_chrge);
        txt_tot_chrge=v.findViewById(R.id.tv_total_chrge);

        txt_cust_name = v.findViewById(R.id.tv_cust_name);
        txt_cust_add = v.findViewById(R.id.tv_cust_add);
        txt_cust_mobile = v.findViewById(R.id.tv_cust_mobile);
        btn_cancel = v.findViewById(R.id.tv_cancel);
        btn_reciept =v.findViewById(R.id.tv_reciept);
        loadingBar = new LoadingBar(getActivity());
        sessionManagement = new Session_management(getActivity());
        user_mobile = sessionManagement.getUserDetails().get(KEY_MOBILE);
        user_name = sessionManagement.getUserDetails().get(KEY_NAME);
        itemlist= new ArrayList<>();

        rv_products=(RecyclerView)v.findViewById(R.id.rv_products);
        rv_products.setLayoutManager(new LinearLayoutManager(getActivity()));

        user_id = sessionManagement.getUserDetails().get(KEY_ID);
        sale_id = getArguments().getString("sale_id");


        swipeRefreshLayout =v.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConnectivityReceiver.isConnected()) {

                        getOrderDetails(sale_id);
//
                }
                else
                {
                    startActivity(new Intent(getActivity(), NoInternetConnection.class));
                }
                if (swipeRefreshLayout.isRefreshing())
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        // check internet connection
        if (ConnectivityReceiver.isConnected()) {

                getOrderDetails(sale_id);


        } else {
            startActivity(new Intent(getActivity(), NoInternetConnection.class));
//
        }
//    btn_reciept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String appName=getActivity().getResources().getString(R.string.app_name);
//                File direct = new File(Environment.getExternalStorageDirectory()
//                        + "/" + appName + "/Orders/"+"BZ_ID"+sale_id+".pdf");
//                if (direct.exists()) {
//                    createDialogPdfViewer(direct);
//                }
//                else
//                {
//                    String urrrl=URL_GENERATE_PDF+"?order_id="+sale_id+"&user_id="+user_id;
//                    if (checkPermission()) {
//                        downloadVideo(urrrl, "BZ_ID" + sale_id);
//                    }
//                }
//
//            }
//        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setMessage(getResources().getString(R.string.cancle_order_note));
                    alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Session_management sessionManagement = new Session_management(getActivity());
                            String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                            // check internet connection
                            if (ConnectivityReceiver.isConnected()) {
                                makeDeleteOrderRequest(sale_id, user_id);
                            }

                            dialogInterface.dismiss();

                        }
                    });

                    alertDialog.show();

                }
                //finish();

        });

//        input_bar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

//            onBackPressed();
            startActivity(new Intent(getActivity(), MainActivity.class));

        }
        return true;
    }

    public void getPermission()
    {
        if (!checkPermission()) {
            requestPermission(); // Code for permission
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    // alertdialog for cancle order
    private void showDeleteDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(getResources().getString(R.string.cancle_order_note));
        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

              
                String user_id = sessionManagement.getUserDetails().get(KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    //  makeDeleteOrderRequest(sale_id, user_id);
                }

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */

    private  void getOrderDetails(String sale_id){
        loadingBar.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);
        params.put("user_id", sessionManagement.getUserDetails().get(KEY_ID));

        module.postRequest(BaseURL.URL_ORDER_DETAIL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             try {
                 Log.d("order details", response.toString());
                 loadingBar.dismiss();
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("responce"))
                {
                    JSONObject data_obj = object.getJSONObject("data");
                    txt_s_chrge.setText(getActivity().getResources().getString(R.string.currency)+data_obj.getString("delivery_charge"));
                    txt_order_id.setText("SG_ID00"+data_obj.getString("sale_id"));
                    makeGetOrderDetailRequest(data_obj.getString("sale_id"));
                    txt_tot_chrge.setText(getActivity().getResources().getString(R.string.currency)+data_obj.getString("total_amount"));
                    txt_cust_add.setText(data_obj.getString("delivery_address"));
                String status =data_obj.getString("status");
                    if (status.equals("0")||status.equals("1"))
                    {
                        btn_cancel.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        btn_cancel.setVisibility(View.GONE);
                    }
                    JSONObject loc_obj = new JSONObject(data_obj.getString("location_id"));
                    txt_cust_mobile.setText(loc_obj.getString("receiver_mobile"));
                    txt_cust_name.setText(loc_obj.getString("receiver_name"));
//                    JSONArray sale_arr = new JSONArray(data_obj.getString("sale_items"));
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<My_order_detail_model>>(){}.getType();
//                    my_order_detail_modelList = gson.fromJson(sale_arr.toString(),listType);
//                    if (my_order_detail_modelList.size()>0) {
//                        My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
//                        rv_products.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                    }

                }




             }catch (Exception ex){
           ex.printStackTrace();
             }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 loadingBar.dismiss();
                 module.VolleyErrorMessage(error);
            }
        });
    }


    private void makeCancelOrderDetailRequest(String sale_id) {

        loadingBar.show();
        // "order_details" used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.URL_CANCEL_ORDER_DETAILS, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("order_details", response.toString());
                loadingBar.dismiss();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                my_order_detail_modelList = gson.fromJson(response.toString(), listType);

                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
                rv_products.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                    loadingBar.dismiss();
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                VolleyLog.d("order_details", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeDeleteOrderRequest(String sale_id, String user_id) {

        // "order_details" used to cancel the request
      
        loadingBar.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);
        params.put("user_id", user_id);
//        params.put("comment", remark);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DELETE_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("order_details", response.toString());

                loadingBar.dismiss();
                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");

                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
//                        getActivity().finish();
                        // ((MaingetActivity()) getActivity()).onBackPressed();

                    } else {
                        String error = response.getString("error");
                        module.showToast( error);
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
                   module.showToast(msg);
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"delete");


    }



    private void downloadFile(String mediaUrl, final String s) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading..");
        dialog.setCanceledOnTouchOutside(false);

        Log.e("adsdasdasd",""+mediaUrl.toString()+" - "+s);

        final String appName = getActivity().getResources().getString(R.string.app_name);
        // creating the download directory
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/" + appName + "/Orders");
        if (!direct.exists()) {
            direct.mkdirs();
        }

        String pdfFileName = s + ".pdf";

        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(mediaUrl));
        request1.setTitle("File Downloader");
        request1.setDescription("Downloading.....");
//        request1.setVisibleInDownloadsUi(false);
        request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request1.setShowRunningNotification(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
//        if (getFileExists(s,vvExt))
//        {
//            new ToastMsg(getActivity()).toastIconError("File is already downloaded!");
//            return;
//        }
        request1.setDestinationInExternalPublicDir("/" + appName + "/Orders", pdfFileName);
        final DownloadManager manager1 = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        // start the download
        final long downloadId = manager1.enqueue(request1);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            module.showToast("Download will be started soon.");
        }

        BroadcastReceiver mCompleted = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss");
                    String ss = simpleDateFormat.format(date);

//                    File movieFile = new File(Environment.getExternalStorageDirectory()
//                            +movieFileName);
//                    String appName22 = getResources().getString(R.string.app_namee)+getResources().getString(R.string.original_file_path);
                    module.showToast("File Download Successfully.");
                    File direct = new File(Environment.getExternalStorageDirectory()
                            + "/" + appName + "/Orders/"+"BZ_ID"+sale_id+".pdf");
                    createDialogPdfViewer(direct);

                }
            }
        };
        getActivity().registerReceiver(mCompleted, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void createDialogPdfViewer(final File file)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("Would you like to open downloaded order receipt?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        try
                        {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.fromFile(file);
                            intent.setDataAndType(uri, "application/pdf");
                            startActivity(intent);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });
        final AlertDialog dialog=builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        });
        dialog.show();
    }



    private void makeGetOrderDetailRequest(String sale_id) {
        loadingBar.show();

        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);


        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.ORDER_DETAIL_URL, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("saleeee", response.toString());

                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                my_order_detail_modelList = gson.fromJson(response.toString(), listType);

                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
                rv_products.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }
                loadingBar.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getActivity(),""+msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
    
}