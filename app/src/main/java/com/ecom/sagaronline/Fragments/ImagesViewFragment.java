package com.ecom.sagaronline.Fragments;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecom.sagaronline.Adapter.ImageAdapter;
import com.ecom.sagaronline.Adapter.ImageRecyclerAdapter;
import com.ecom.sagaronline.R;

import java.util.ArrayList;

import static com.ecom.sagaronline.Fragments.NewDetailFragment.image_list;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesViewFragment extends Fragment {
    ViewPager pager ;
    ImageAdapter adapter ;
    RecyclerView img_recycler ;
    ImageRecyclerAdapter imageRecyclerAdapter;
    int sel_pos =0 ;
    ArrayList<String>image_list;


public ImagesViewFragment(){}

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_images_view, container, false);
      pager = view.findViewById(R.id.img_pager);
      adapter = new ImageAdapter(getActivity(), (ArrayList<String>) image_list);
        img_recycler = view.findViewById(R.id.img_recycler);
      pager.setAdapter(adapter);

      pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         @Override
         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

         }

         @Override
         public void onPageSelected(int position) {
             sel_pos = position;

             imageRecyclerAdapter = new ImageRecyclerAdapter(getActivity(),image_list,sel_pos);
             img_recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
             img_recycler.setAdapter(imageRecyclerAdapter);
             imageRecyclerAdapter.notifyDataSetChanged();
         }

         @Override
         public void onPageScrollStateChanged(int state) {

         }
     });
//      sel_pos = adapter.getItemPosition(pager.getCurrentItem());
        imageRecyclerAdapter = new ImageRecyclerAdapter(getActivity(),image_list,sel_pos);
        img_recycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        img_recycler.setAdapter(imageRecyclerAdapter);

      return  view;
    }
}
