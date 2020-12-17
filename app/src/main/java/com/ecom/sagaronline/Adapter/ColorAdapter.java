package com.ecom.sagaronline.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.ecom.sagaronline.Model.ColorModel;
import com.ecom.sagaronline.R;

import java.util.List;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {

    private List<ColorModel> modelList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout colors;
        RelativeLayout boundary;

        public MyViewHolder(View view) {
            super(view);
            colors = view.findViewById(R.id.ll_color);
            boundary = view.findViewById(R.id.ll_color1);
        }
    }

    public ColorAdapter(List<ColorModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public ColorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_color, parent, false);

        context = parent.getContext();

        return new ColorAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ColorAdapter.MyViewHolder holder, int position) {
        String mList = modelList.get(position).getColor();
        boolean ischecked = modelList.get(position).isSelected();
        holder.colors.setBackgroundColor(Color.parseColor(mList));
        if (ischecked)
        {
            holder.boundary.setBackgroundColor(Color.DKGRAY);
        }
        else
        {
            holder.boundary.setBackgroundColor(Color.parseColor(mList));
        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

