package com.example.sagaronlineyash.Fragments;


import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sagaronlineyash.Adapter.FilterAdapter;
import com.example.sagaronlineyash.Model.FilterModel;
import com.example.sagaronlineyash.R;
import com.example.sagaronlineyash.Utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;



public class FilterFragment extends Fragment {

    String max_slab , slab_value , cat_id;
    RecyclerView recyclerFilter;
    List<FilterModel> filter ;
    FilterAdapter adapter;


    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_filter, container, false);
        max_slab = getArguments().getString("max_slab");
        slab_value = getArguments().getString("slab_value");
        cat_id = getArguments().getString("cat_id");
        recyclerFilter = view.findViewById(R.id.recyclerFilter);
        filter = new ArrayList<>();
        int max = 2*Integer.parseInt(slab_value);
        int min = Integer.parseInt(slab_value);
        filter.add(new FilterModel("Upto \n"+slab_value,"0",""+slab_value));
        while(max<=Integer.parseInt(max_slab))
        {
            filter.add(new FilterModel("From "+min+"\n to "+max,String.valueOf(min),String.valueOf(max)));
            min=max;
            max=max+Integer.parseInt(slab_value);
        }
        filter.add(new FilterModel("Above \n"+max_slab,max_slab, String.valueOf(Integer.MAX_VALUE)));
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity(), 2 );
        recyclerFilter.setLayoutManager( gridLayoutManager );
        adapter = new FilterAdapter(filter);
        recyclerFilter.setAdapter(adapter);

        recyclerFilter.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerFilter, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FilterModel mList = filter.get(position);
                String text  = mList.getText();
                String min = mList.getMin();
                String max = mList.getMax();
                Bundle bundle = new Bundle();
                Fragment fm = new ProductFragment();
                bundle.putString("cat_id",cat_id);
                bundle.putString("max_slab",max_slab);
                bundle.putString("slab_value",slab_value);
                bundle.putString("max",max);
                bundle.putString("min",min);
                fm.setArguments(bundle);
             FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace( R.id.fragment_container, fm).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        Log.e("SLAB",max_slab+slab_value);
        Log.e("DATA",filter.toString());

        return view;

    }
}