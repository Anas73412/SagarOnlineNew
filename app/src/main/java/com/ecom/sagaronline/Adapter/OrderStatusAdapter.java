package com.ecom.sagaronline.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ecom.sagaronline.Model.OrderStatusModel;
import com.ecom.sagaronline.R;

import java.util.ArrayList;

public  class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder>
{
    ArrayList<OrderStatusModel> stat_list;
    Context activity;

    public OrderStatusAdapter(ArrayList<OrderStatusModel> stat_list, Context activity) {
        this.stat_list = stat_list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.row_order_status,null);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_status.setText(stat_list.get(position).getStatus());
        if (stat_list.get(position).getDate()==null|| stat_list.get(position).getDate().equalsIgnoreCase("null")) {
            holder.tv_date.setText("");
        }
        else
        {
            String d[] = stat_list.get(position).getDate().split("-");

            holder.tv_date.setText(d[2]+"-"+d[1]+"-"+d[0]);
        }
        Log.e("stat","pos-----"+position+"-----"+stat_list.get(position).isIs_checked()+stat_list.get(position).getDate());
        if (stat_list.get(position).isIs_checked())
        {
            holder.iv_order_status.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.card_color)));
            holder.v1.setBackgroundColor(activity.getResources().getColor(R.color.black));
            holder.v2.setBackgroundColor(activity.getResources().getColor(R.color.black));
        }
        else
        {
            holder.iv_order_status.setImageTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.white)));
            holder.v1.setBackgroundColor(activity.getResources().getColor(R.color.gray));
            holder.v2.setBackgroundColor(activity.getResources().getColor(R.color.gray));
        }

    }

    @Override
    public int getItemCount() {
        return stat_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_status ,tv_date ;
        ImageView iv_order_status;
        View v1 ,v2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_status = itemView.findViewById(R.id.tv_order_status);
            tv_date = itemView.findViewById(R.id.tv_order_date);
            v1 = itemView.findViewById(R.id.view1);
            v2 = itemView.findViewById(R.id.view2);
            iv_order_status = itemView.findViewById(R.id.iv_order_status);

        }
    }
}
