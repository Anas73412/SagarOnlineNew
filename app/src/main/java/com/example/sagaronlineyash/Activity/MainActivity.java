package com.example.sagaronlineyash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sagaronlineyash.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity{

    Toolbar toolbar;
    int padding = 0;
    DrawerLayout drawer;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_shop_now:
                        drawer.openDrawer(GravityCompat.START);
                        if (drawer.isDrawerOpen(GravityCompat.START))
                            drawer.closeDrawer(GravityCompat.START);
                        return true;
                }
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