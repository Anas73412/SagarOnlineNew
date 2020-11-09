package com.example.sagaronlineyash.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sagaronlineyash.Activity.OrderViewPagerAdapter;
import com.example.sagaronlineyash.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrderFragment extends Fragment {

    private TabLayout tabLayout2;
    private ViewPager viewPager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrderFragment newInstance(String param1, String param2) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_my_order, container, false);

        tabLayout2= v.findViewById(R.id.tabs);
        viewPager= v.findViewById(R.id.myViewPager);
        setupViewPager(viewPager);
        tabLayout2.setupWithViewPager(viewPager);
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