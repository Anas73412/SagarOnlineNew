package com.ecom.sagaronline.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ecom.sagaronline.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 21,February,2021
 */
class DetailFragment extends Fragment {
    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        return view;
    }
}
