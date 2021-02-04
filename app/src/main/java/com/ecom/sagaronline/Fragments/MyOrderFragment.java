package com.ecom.sagaronline.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Adapter.OrderViewPagerAdapter;
import com.ecom.sagaronline.R;
import com.google.android.material.tabs.TabLayout;


public class MyOrderFragment extends Fragment {

    private TabLayout tabLayout2;
    private ViewPager viewPager;

    public MyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_order, container, false);
        ((MainActivity) getActivity()).setTitle("My Orders");
        tabLayout2= v.findViewById(R.id.tabs);
        viewPager= v.findViewById(R.id.myViewPager);
        setupViewPager(viewPager);
        tabLayout2.setupWithViewPager(viewPager);

//        v.setFocusableInTouchMode(true);
//        v.requestFocus();
//        v.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
////                    Fragment fm = new HomeFragment();
////                    androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
////                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
////                            .addToBackStack(null).commit();
//                    startActivity(new Intent(getActivity(),MainActivity.class));
//                    return true;
//                }
//                return false;
//            }
//        });
            return v;
    }
    private void setupViewPager(ViewPager viewPager){
        OrderViewPagerAdapter orderViewPagerAdapter=new OrderViewPagerAdapter(getChildFragmentManager());
        orderViewPagerAdapter.addFragment(new RecentFragment(),"RECENT");
        orderViewPagerAdapter.addFragment(new PastFragment(),"PAST");
        orderViewPagerAdapter.addFragment(new CancelledFragment(),"CANCELLED");
        viewPager.setAdapter(orderViewPagerAdapter);
    }


}