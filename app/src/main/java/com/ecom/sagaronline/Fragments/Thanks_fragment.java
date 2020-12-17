package com.ecom.sagaronline.Fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ecom.sagaronline.Activity.MainActivity;
import com.ecom.sagaronline.Config.Module;
import com.ecom.sagaronline.R;
import com.ecom.sagaronline.Utils.LoadingBar;

import static android.content.Context.MODE_PRIVATE;


public class Thanks_fragment extends Fragment implements View.OnClickListener {

    TextView tv_info;
    RelativeLayout btn_home, btn_order;
  LoadingBar loadingBar ;
    SharedPreferences preferences;
    Module module;
    String language;
    public Thanks_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar = new LoadingBar(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_thanks, container, false);
        loadingBar = new LoadingBar(getActivity());
        module=new Module(getActivity());

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.thank_you));
        preferences = getActivity().getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment fm = new HomeFragment();
                  androidx.fragment.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        String data = getArguments().getString("msg");
        String dataarb=getArguments().getString("msg");
        tv_info = (TextView) view.findViewById(R.id.tv_thank_info);
        btn_home = (RelativeLayout) view.findViewById(R.id.btn_thank_home);
        btn_order = (RelativeLayout) view.findViewById(R.id.btn_track_order);

        if (language.contains("english")) {
            tv_info.setText(Html.fromHtml(data));
        }else {
            tv_info.setText(Html.fromHtml(dataarb));       }


        btn_home.setOnClickListener(this);
        btn_order.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_thank_home) {
            Fragment fm = new HomeFragment();
           FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                    .addToBackStack(null).commit();
        }
        if (id == R.id.btn_track_order) {
            Fragment fm = new MyOrderFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fm)
                    .addToBackStack(null).commit();
        }


    }

}
