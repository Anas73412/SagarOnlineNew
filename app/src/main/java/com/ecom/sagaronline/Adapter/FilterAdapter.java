package com.ecom.sagaronline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ecom.sagaronline.Model.FilterModel;
import com.ecom.sagaronline.R;

import java.util.List;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    private List<FilterModel> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout rel_flter;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_filter);
            rel_flter = view.findViewById(R.id.rel_filter);
        }
    }

    public FilterAdapter(List<FilterModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public FilterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_filter, parent, false);

        context = parent.getContext();

        return new FilterAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilterAdapter.MyViewHolder holder, int position) {
        FilterModel mList = modelList.get(position);
        String text  = mList.getText();
        String min = mList.getMin();
        String max = mList.getMax();
        holder.title.setText(text);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

