package com.example.sagaronlineyash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sagaronlineyash.Model.FilterModel;
import com.example.sagaronlineyash.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.sagaronlineyash.Utils.SearchHistoryHandler.HISTORY_NAME;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private ArrayList<HashMap<String,String>> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout rel_flter;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.his_name);
        }
    }

    public HistoryAdapter(ArrayList<HashMap<String,String>> modelList) {
        this.modelList = modelList;
    }

    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history, parent, false);

        context = parent.getContext();

        return new HistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {
        HashMap<String,String> mList = modelList.get(position);
        String text  = mList.get(HISTORY_NAME);
        holder.title.setText(text);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

