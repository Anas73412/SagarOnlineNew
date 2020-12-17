package com.ecom.sagaronline.Fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.LoadingBar;


public class EmptyCartFragment extends Fragment {

    private static String TAG = EmptyCartFragment.class.getSimpleName();

    RelativeLayout Shop_now;

    LoadingBar loadingBar;
    public EmptyCartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar = new LoadingBar(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty_cart, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.cart));
        loadingBar = new LoadingBar(getActivity());
        Shop_now = (RelativeLayout) view.findViewById(R.id.btn_shopnow);
        Shop_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Fragment fm = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                        .addToBackStack(null).commit();
            }
        });

        return view;
    }


}

