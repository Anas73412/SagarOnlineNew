package com.ecom.sagaronline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecom.sagaronline.Model.ProductVariantModel;
import com.ecom.sagaronline.R;

import java.util.ArrayList;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 21,February,2021
 */
public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductVariantModel> list;

    public AttributeAdapter(Context context, ArrayList<ProductVariantModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_attribute,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductVariantModel model=list.get(position);
        holder.tv_title.setText(model.getAttribute_size());
        if(model.isChecked()){
            holder.rl_main.setBackground(context.getResources().getDrawable(R.drawable.selected_rounded_border));
            holder.tv_title.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.rl_main.setBackground(context.getResources().getDrawable(R.drawable.rounded_border));
            holder.tv_title.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
     TextView tv_title;
     RelativeLayout rl_main;

        public ViewHolder(@NonNull View v) {
            super(v);
            tv_title=v.findViewById(R.id.tv_title);
            rl_main=v.findViewById(R.id.rl_main);
        }
    }
}
