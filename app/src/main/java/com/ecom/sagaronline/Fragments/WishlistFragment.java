package com.ecom.sagaronline.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Adapter.NewProductAdapter;
import com.ecom.sagaronline.Adapter.Wishlist_Adapter;
import com.ecom.sagaronline.Model.NewProductModel;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.DatabaseCartHandler;
import com.ecom.sagaronline.Utils.LoadingBar;
import com.ecom.sagaronline.Utils.Session_management;
import com.ecom.sagaronline.Utils.WishlistHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ecom.sagaronline.Config.BaseURL.KEY_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFragment extends Fragment {
    private static String TAG = WishlistFragment.class.getSimpleName();
    private Bundle savedInstanceState;
    private WishlistHandler db_wish;
    public static RecyclerView rv_wishlist;
    DatabaseCartHandler db_cart;
    LoadingBar loadingBar;
    String user_id ;
    Session_management sessionManagement ;
    ArrayList<NewProductModel>p_list;
    NewProductAdapter productAdapter;
    public static RelativeLayout rel_no;

    public WishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_wishlist, container, false );
        setHasOptionsMenu( true );

        ((MainActivity) getActivity()).setTitle("My WishList" );
        rv_wishlist = view.findViewById( R.id.rv_wishlist );
        rel_no = view.findViewById( R.id.rel_no );
        rv_wishlist.setLayoutManager( new GridLayoutManager( getActivity(),2) );
        db_cart=new DatabaseCartHandler(getActivity());
        //db = new DatabaseHandler(getActivity());
        db_wish = new WishlistHandler( getActivity() );
        sessionManagement = new Session_management( getActivity() );
        user_id=sessionManagement.getUserDetails().get(KEY_ID);
        loadingBar = new LoadingBar(getActivity());
        p_list = new ArrayList<>();
        p_list=db_wish.getProductList(user_id);
        productAdapter = new NewProductAdapter(p_list,getActivity(),"w");
        ArrayList<HashMap<String, String>> map = db_wish.getWishtableAll(user_id);
        if (map.isEmpty())
        {
            rel_no.setVisibility(View.VISIBLE);

        }
        else {
            rel_no.setVisibility(View.GONE);

        }


//        Log.d("cart all ",""+db_cart.getCartAll());

        Wishlist_Adapter adapter = new Wishlist_Adapter( map,getActivity() );
        rv_wishlist.setAdapter( productAdapter);
        adapter.notifyDataSetChanged();
        productAdapter.notifyDataSetChanged();



        return view;
    }



   /* private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder( getActivity() );
        alertDialog.setMessage( getResources().getString( R.string.sure_del ) );
        alertDialog.setNegativeButton( getResources().getString( R.string.cancle ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );
        alertDialog.setPositiveButton( getResources().getString( R.string.yes ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
                db_wish.clearWishtable(user_id);
                ArrayList<HashMap<String, String>> map = db_wish.getWishtableAll(user_id);
                Wishlist_Adapter adapter = new Wishlist_Adapter(  map,getActivity() );
                rv_wishlist.setAdapter( adapter );
                adapter.notifyDataSetChanged();
                rel_no.setVisibility(View.VISIBLE);
                rv_wishlist.setVisibility(View.GONE);
                //updateData();

                dialogInterface.dismiss();
            }
        } );

        alertDialog.show();


    }*/


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("My WishList" );
        // register reciver
        getActivity().registerReceiver(mCart, new IntentFilter("Cart"));
    }

    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    private void updateData() {

        //((MainActivity) getActivity()).setCartCounter("" + db_cart.getCartCount());
    }


    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

}

