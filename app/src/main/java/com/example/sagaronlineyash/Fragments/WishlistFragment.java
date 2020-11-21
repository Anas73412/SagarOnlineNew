package com.example.sagaronlineyash.Fragments;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.sagaronlineyash.Activity.MainActivity;
import com.example.sagaronlineyash.Adapter.Wishlist_Adapter;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.DatabaseCartHandler;
import com.example.sagaronlineyash.Utils.LoadingBar;
import com.example.sagaronlineyash.Utils.Session_management;
import com.example.sagaronlineyash.Utils.WishlistHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.sagaronlineyash.Config.BaseURL.KEY_ID;


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
        rv_wishlist.setLayoutManager( new GridLayoutManager( getActivity(),1) );
        db_cart=new DatabaseCartHandler(getActivity());
        //db = new DatabaseHandler(getActivity());
        db_wish = new WishlistHandler( getActivity() );
        sessionManagement = new Session_management( getActivity() );
        user_id=sessionManagement.getUserDetails().get(KEY_ID);
        loadingBar = new LoadingBar(getActivity());
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
        rv_wishlist.setAdapter( adapter );
        adapter.notifyDataSetChanged();



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
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
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

