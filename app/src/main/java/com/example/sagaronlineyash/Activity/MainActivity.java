package com.example.sagaronlineyash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.sagaronlineyash.Fragments.AddressFragment;
import com.example.sagaronlineyash.Fragments.ContactUsFragment;
import com.example.sagaronlineyash.Fragments.HomeFragment;
import com.example.sagaronlineyash.Fragments.MyOrderFragment;
import com.example.sagaronlineyash.Fragments.ShopFragment;
import com.example.sagaronlineyash.Fragments.TermsFragment;
import com.example.sagaronlineyash.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity{

    Toolbar toolbar;
    int padding = 0;
    Fragment fragment = null;
    DrawerLayout drawer;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());
        navigationView = findViewById(R.id.nav_view);
        if (savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).addToBackStack(null).commit();
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

                    case R.id.nav_contactus:
                        fragment = new ContactUsFragment();
                           break;

                    case R.id.nav_share:
                        Intent inte= new Intent(MainActivity.this,NewAddressActivity.class);
                        startActivity(inte);
                        break;

                    case R.id.nav_logout:
                        fragment = new AddressFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
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
        switch (item.getItemId()){
            case R.id.nav_shop_now:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        super.onOptionsItemSelected(item);
        return true;
    }

}