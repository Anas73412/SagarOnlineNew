package com.example.sagaronlineyash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sagaronlineyash.Fragments.CartFragment;
import com.example.sagaronlineyash.Fragments.ContactUsFragment;
import com.android.volley.NetworkError;
import com.example.sagaronlineyash.Fragments.EmptyCartFragment;
import com.example.sagaronlineyash.Fragments.HomeFragment;
import com.example.sagaronlineyash.Fragments.Search_fragment;
import com.example.sagaronlineyash.Fragments.ShopFragment;
import com.example.sagaronlineyash.Fragments.TermsFragment;
import com.example.sagaronlineyash.Fragments.WishlistFragment;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.ConnectivityReceiver;
import com.example.sagaronlineyash.Utils.DatabaseCartHandler;
import com.example.sagaronlineyash.Utils.Session_management;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;
    int padding = 0;
    Fragment fragment = null;
    DrawerLayout drawer;
    NavigationView navigationView;
    private DatabaseCartHandler db_cart;
    Session_management session_management;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session_management = new Session_management(MainActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());
        navigationView = findViewById(R.id.nav_view);
        db_cart=new DatabaseCartHandler(this);
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

                    case R.id.nav_aboutus:

                        Intent in= new Intent(MainActivity.this,AboutUsActivity.class);
                        startActivity(in);
                           break;
                    case R.id.nav_policy:
                        fragment = new TermsFragment();
                           break;
                    case R.id.nav_wishlist:
                        fragment = new WishlistFragment();
                        break;

                    case R.id.nav_contactus:
                        fragment = new ContactUsFragment();
                           break;

                    case R.id.nav_share:
                        Intent inte= new Intent(MainActivity.this,NewAddressActivity.class);
                        startActivity(inte);
                        break;

                    case R.id.nav_logout:
                        session_management.logoutSession();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finishAffinity();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
       /* if (id == R.id.action_language) {
            openLanguageDialog();
        }*/

        if (id == R.id.action_cart) {
            if (db_cart.getCartCount() > 0) {
                Fragment fm = new CartFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                        .addToBackStack(null).commit();

            } else {
                Fragment fm = new EmptyCartFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                        .addToBackStack(null).commit();

            }
            return true;
        }
        if(id== R.id.action_search)
        {
            Fragment fm = new Search_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                    .addToBackStack(null).commit();


        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_cart);
        item.setVisible(true);

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