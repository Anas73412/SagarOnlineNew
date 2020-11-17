package com.example.sagaronlineyash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.Fragment;

import android.app.Fragment;
//import android.content.DialogInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.TextView;

import com.example.sagaronlineyash.Config.Module;
import com.example.sagaronlineyash.Fragments.AddressFragment;
import com.example.sagaronlineyash.Fragments.CartFragment;
import com.example.sagaronlineyash.Fragments.ContactUsFragment;
import com.android.volley.NetworkError;
import com.example.sagaronlineyash.Fragments.HomeFragment;
import com.example.sagaronlineyash.Fragments.MyOrderFragment;
import com.example.sagaronlineyash.Fragments.ProfileFragment;
import com.example.sagaronlineyash.Fragments.ShopFragment;
import com.example.sagaronlineyash.Fragments.TermsFragment;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.ConnectivityReceiver;
import com.example.sagaronlineyash.Utils.Session_management;
import com.google.android.material.navigation.NavigationView;

import binplus.SagarOnline.Fragment.WishlistFragment;

import static com.example.sagaronlineyash.Config.BaseURL.KEY_ID;
import static com.example.sagaronlineyash.Config.BaseURL.KEY_MOBILE;
import static com.example.sagaronlineyash.Config.BaseURL.KEY_NAME;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;
    int padding = 0;
    Module module;
    Fragment fragment = null;
    DrawerLayout drawer;
    private String[] mNavigationDrawerItemTitles;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
   // ActionBarDrawerToggle actionBarDrawerToggle;
  //  DataModel[] drawerItem = new DataModel[10];
  //  DrawerAdapter adapter;

    NavigationView navigationView;
    Session_management session_management;
    TextView tv_name;
    ImageView iv_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session_management = new Session_management(MainActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


       // tv_name = (TextView) findViewById(R.id.viewProfile);

        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());
        navigationView = findViewById(R.id.nav_view);

       View header = navigationView.getHeaderView(0);
       tv_name = (TextView) header.findViewById(R.id.viewProfile);
        iv_edit = (ImageView) header.findViewById(R.id.editProfile);
        module=new Module(MainActivity.this);
        updatename();

       // tv_name.setText("XYZ");

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(MainActivity.this,ProfileFragment.class);
                if (savedInstanceState == null) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                }
            }
        });

 /**       edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bun = new Bundle();
                bun.putString("type","edit");
                bun.putString("id",session_management.getUserDetails().get(KEY_ID));
                bun.putString("name",session_management.getUserDetails().get(KEY_NAME));
                bun.putString("number",session_management.getUserDetails().get(KEY_MOBILE));
                loadFragment(profileFragment,bun);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment profileFragment = new ProfileFragment();
                Bundle bun = new Bundle();
                bun.putString("type","view");
                bun.putString("id",session_management.getUserDetails().get(KEY_ID));
                bun.putString("name",session_management.getUserDetails().get(KEY_NAME));
                bun.putString("number",session_management.getUserDetails().get(KEY_MOBILE));
                loadFragment(profileFragment,bun);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
*/
        if (savedInstanceState==null)
        {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).addToBackStack(null).commit();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_shop_now:
                        fragment = new ShopFragment();
                         break;

                    case R.id.nav_wishlist:
                        fragment = new WishlistFragment();
                        break;

                    case R.id.nav_cart:
                        fragment = new CartFragment();
                        break;

                    case R.id.nav_address:
                        fragment = new AddressFragment();
                        break;

                    case R.id.nav_aboutus:

                        Intent in = new Intent(MainActivity.this,AboutUsActivity.class);
                        startActivity(in);
                           break;
                    case R.id.nav_policy:
                        fragment = new TermsFragment();
                           break;

                    case R.id.nav_contactus:
                        fragment = new ContactUsFragment();
                           break;

                    case R.id.nav_share:
                        fragment = new ShopFragment();
                        break;

                    case R.id.nav_logout:
                        session_management.logoutSession();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
//                        finishAffinity();
                        break;

                }
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

   /**   private BroadcastReceiver nameRec=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type=intent.getStringExtra("type");
            if(type.equalsIgnoreCase("name")){
                updatename();
            }
        }
    };*/

  public void updatename() {
        tv_name.setText(""+module.checkNull( session_management.getUserDetails().get(KEY_NAME)));
    }

  /**  @Override
    public void onResume() {
        super.onResume();
        registerReceiver(nameRec,new IntentFilter("namechanged"));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(nameRec);
    }*/

    public void loadFragment(Fragment fm, Bundle args) {

        fm.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fm)
                .addToBackStack(null).commit();
        HomeFragment home = new HomeFragment();
        Log.e("frag_pos", String.valueOf(getFragmentManager().getBackStackEntryCount()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_shop_now:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        super.onOptionsItemSelected(item);
        return true;
    }


    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }


    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;

        if (!isConnected) {
            Intent intent = new Intent(MainActivity.this, NetworkError.class);
            startActivity(intent);
        }
    }
}