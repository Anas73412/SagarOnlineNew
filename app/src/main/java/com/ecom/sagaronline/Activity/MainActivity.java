package com.ecom.sagaronline.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.Fragment;


//import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.TextView;

import com.ecom.sagaronline.Fragments.CartFragment;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.Fragments.ContactUsFragment;
import com.android.volley.NetworkError;
import com.ecom.sagaronline.Fragments.EmptyCartFragment;
import com.ecom.sagaronline.Fragments.HomeFragment;
import com.ecom.sagaronline.Fragments.MyOrderFragment;
import com.ecom.sagaronline.Fragments.Search_fragment;
import com.ecom.sagaronline.Fragments.ProfileFragment;
import com.ecom.sagaronline.Fragments.ShopFragment;
import com.ecom.sagaronline.Fragments.TermsFragment;
import com.ecom.sagaronline.Fragments.WishlistFragment;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.ConnectivityReceiver;
import com.ecom.sagaronline.Utils.DatabaseCartHandler;
import com.ecom.sagaronline.Utils.Session_management;
import com.google.android.material.navigation.NavigationView;


import static com.ecom.sagaronline.Config.BaseURL.KEY_NAME;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;
    int padding = 0;
    Module module;
   androidx.fragment.app.Fragment fragment = null;
    DrawerLayout drawer;
    private String[] mNavigationDrawerItemTitles;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    TextView tv_count;
    NavigationView navigationView;
    private DatabaseCartHandler db_cart;
    Session_management session_management;
    TextView tv_name;
    ImageView iv_edit;
    TextView tv_cart_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session_management = new Session_management(MainActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_cart_count = findViewById(R.id.tv_cart_count);
        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());
        navigationView = findViewById(R.id.nav_view);
        db_cart=new DatabaseCartHandler(this);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
       View header = navigationView.getHeaderView(0);
       tv_name = (TextView) header.findViewById(R.id.viewProfile);
        iv_edit = (ImageView) header.findViewById(R.id.editProfile);
        module=new Module(MainActivity.this);
        toolbar.setTitleTextAppearance(MainActivity.this,R.style.titlebartext);
        updatename();

        if (savedInstanceState != null) {
          //  mCloseNavDrawer = savedInstanceState.getBoolean(CLOSE_NAV_DRAWER);
        }


        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(MainActivity.this,ProfileFragment.class);
                if (savedInstanceState == null) {
                    if (drawer.isDrawerOpen(GravityCompat.START))
                        drawer.closeDrawer(GravityCompat.START);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                }
            }
        });


        if (savedInstanceState==null)
        {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).addToBackStack(null).commit();
        }

        getSupportFragmentManager().

                addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        try {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                         Fragment fr = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                            final String fm_name = fr.getClass().getSimpleName();
                            Log.e("backstack: ", ": " + fm_name);
                            if (fm_name.contentEquals("HomeFragment")) {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                toggle.setDrawerIndicatorEnabled(true);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                toggle.syncState();


                            }
                            else if (
                                    fm_name.contentEquals("Thanks_fragment")) {
                               // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();

                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                      Fragment fm = new HomeFragment();
                                     FragmentManager fragmentManager = getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                                                .addToBackStack(null).commit();
                                       // onBackPressed();
                                    }
                                });
                            }
                            else {

                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();

                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        onBackPressed();
                                    }
                                });
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_shop_now:
                        fragment = new ShopFragment();
                         break;

                    case R.id.nav_cart:
                        if (db_cart.getCartCount() > 0) {

                           // Bundle args = new Bundle();
                            fragment = new CartFragment();
//                            args.putString("type", "cart");
//                            fragment.setArguments(args);


                        } else {
                           fragment = new EmptyCartFragment();
                        }
                        break;

         case R.id.nav_wishlist:
                        fragment = new WishlistFragment();
                        break;

                    case R.id.nav_aboutus:
                        fragment = new AboutUsActivity();
                        break;
                    case R.id.nav_policy:
                        fragment = new TermsFragment();
                       // fragment = new NewDetailFragment();
                           break;

                    case R.id.nav_contactus:
                        fragment = new ContactUsFragment();
                           break;

                    case R.id.nav_orders:
                        fragment = new MyOrderFragment();
                           break;

                    case R.id.nav_share:
                        Toast.makeText(getApplicationContext(),"To Be Added",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_logout:
                        session_management.logoutSession();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                        break;

                }

                if (fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
                }
                if (drawer.isDrawerOpen(GravityCompat.START))
                    drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }
    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        drawer.closeDrawers();
    }
    public void updatename() {
        tv_name.setText(""+module.checkNull( session_management.getUserDetails().get(KEY_NAME)));
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
               FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                        .addToBackStack(null).commit();

            } else {
                Fragment fm = new EmptyCartFragment();
               FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                        .addToBackStack(null).commit();

            }
            return true;
        }
        if(id== R.id.action_search)
        {
            Fragment fm = new Search_fragment();
          FragmentManager fragmentManager = getSupportFragmentManager();
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
        View count = item.getActionView();
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(item.getItemId(), 0);
            }
        });

        tv_count = (TextView) count.findViewById(R.id.tv_cart_count);
        tv_count.setText("" + db_cart.getCartCount());
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

    public void setCartCounter(int count){
        if(count<=0){
            tv_count.setVisibility(View.GONE);
        }else{
            tv_count.setVisibility(View.VISIBLE);
        }
        tv_count.setText(String.valueOf(count));
    }
    public void setCount(String str){
       // tv_count.setText("" + db_cart.getCartCount());
        tv_count.setText(str);
    }
}