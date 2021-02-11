package com.ecom.sagaronline.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecom.sagaronline.Model.My_order_model;
import com.ecom.sagaronline.Model.OrderStatusModel;
import com.ecom.sagaronline.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<My_order_model> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;
    SharedPreferences preferences;
    private Context context;
    ArrayList<OrderStatusModel>order_stat_list;

//    public OrderAdapter(Context context, List<My_order_model> modemodelList, final Fragment currentFragment) {
//
//        this.context = context;
//        this.modelList = modelList;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.currentFragment = currentFragment;
//
//
//    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date;
        public TextView tv_pending_date, tv_pending_time, tv_confirm_date, tv_confirm_time, tv_delevered_date, tv_delevered_time, tv_cancel_date, tv_cancel_time;
        public View view1, view2, view3, view4, view5, view6;
        public RelativeLayout relative_background;
        public ImageView Confirm, Out_For_Deliverde, Delivered ,iv_cancelled;
        CardView cardView;
        public TextView tv_methid1;
        public String method;
        LinearLayout linearLayout;
        RecyclerView rv_order_stat;

        public MyViewHolder(View view) {

            super(view);
            tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            relativetextstatus = (TextView) view.findViewById(R.id.status);
            tv_tracking_date = (TextView) view.findViewById(R.id.tracking_date);
            tv_date = (TextView) view.findViewById(R.id.tv_order_date);
            tv_time = (TextView) view.findViewById(R.id.tv_order_time);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(R.id.tv_order_item);
            rv_order_stat = view.findViewById(R.id.rv_order_status);
            rv_order_stat.setLayoutManager(new LinearLayoutManager(context));
            cardView = view.findViewById(R.id.card_view);

            linearLayout=view.findViewById(R.id.l1);
//            //Payment Method
            tv_methid1 = (TextView) view.findViewById(R.id.method1);
            //Date And Time
            tv_pending_date = (TextView) view.findViewById(R.id.pending_date);
//            tv_pending_time = (TextView) view.findViewById(R.id.pending_time);
            tv_confirm_date = (TextView) view.findViewById(R.id.confirm_date);
//            tv_confirm_time = (TextView) view.findViewById(R.id.confirm_time);
            tv_delevered_date = (TextView) view.findViewById(R.id.delevered_date);
//            tv_delevered_time = (TextView) view.findViewById(R.id.delevered_time);
            tv_cancel_date = (TextView) view.findViewById(R.id.cancel_date);
//            tv_cancel_time = (TextView) view.findViewById(R.id.cancel_time);
            //Oredre Tracking
            view1 = (View) view.findViewById(R.id.view1);
            view2 = (View) view.findViewById(R.id.view2);
            view3 = (View) view.findViewById(R.id.view3);
            view4 = (View) view.findViewById(R.id.view4);
            view5 = (View) view.findViewById(R.id.view5);
            view6 = (View) view.findViewById(R.id.view6);
            relative_background = (RelativeLayout) view.findViewById(R.id.relative_background);
            iv_cancelled = view.findViewById(R.id.iv_cancelled);
            Confirm = (ImageView) view.findViewById(R.id.confirm_image);
            Out_For_Deliverde = (ImageView) view.findViewById(R.id.delivered_image);
            Delivered = (ImageView) view.findViewById(R.id.cancal_image);

        }
    }

    public OrderAdapter(List<My_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public OrderAdapter .MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_past_order_rv, parent, false);
        context = parent.getContext();
        return new OrderAdapter .MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(OrderAdapter .MyViewHolder holder, int position) {
        My_order_model mList = modelList.get(position);
        order_stat_list = new ArrayList<>();
        order_stat_list.add(new OrderStatusModel("Order Placed",mList.getOn_date(),false));
        order_stat_list.add(new OrderStatusModel("Order Confirmed",mList.getOn_date(),false));
        order_stat_list.add(new OrderStatusModel("Ready For Pickup",mList.getOn_date(),false));
        order_stat_list.add(new OrderStatusModel("Out for Delivery",mList.getOn_date(),false));
        order_stat_list.add(new OrderStatusModel("Delivered",mList.getOn_date(),false));
        OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(order_stat_list,context);
        holder.rv_order_stat.setAdapter(orderStatusAdapter);
        holder.tv_orderno.setText(mList.getSale_id());

        if (mList.getStatus().equals("0")) {
            holder.tv_status.setText(context.getResources().getString(R.string.pending));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            order_stat_list.get(0).setIs_checked(true);
            orderStatusAdapter.notifyDataSetChanged();

        } else if (mList.getStatus().equals("1")) {
            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.orange));
            holder.Confirm.setImageResource(R.color.colorPrimary);
            holder.tv_status.setText(context.getResources().getString(R.string.confirm));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            order_stat_list.get(1).setIs_checked(true);
            orderStatusAdapter.notifyDataSetChanged();

        } else if (mList.getStatus().equals("2")) {
            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.tot));
            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.view3.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.view4.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.Confirm.setImageResource(R.color.colorPrimary);
            holder.Out_For_Deliverde.setImageResource(R.color.colorPrimary);
            holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.outfordeliverd));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            order_stat_list.get(3).setIs_checked(true);
            orderStatusAdapter.notifyDataSetChanged();
        }
           else if (mList.getStatus().equals("4")) {
            holder.linearLayout.setVisibility(View.GONE);
            holder.iv_cancelled.setVisibility(View.VISIBLE);
            order_stat_list.get(4).setIs_checked(true);
            orderStatusAdapter.notifyDataSetChanged();
        }

        if (mList.getPayment_method().equals("Store Pick Up")) {
            holder.tv_methid1.setText("Store Pick Up");
        } else if (mList.getPayment_method().equals("Cash On Delivery")) {
            holder.tv_methid1.setText(context.getResources().getString(R.string.cash));
        } else if (mList.getPayment_method().equals("Debit / Credit Card")) {
            holder.tv_methid1.setText("PrePaid");
        } else if (mList.getPayment_method().equals("Online Payment on delivery via Paytm/Upi.")) {
            holder.tv_methid1.setText("Online Payment on delivery via Paytm/Upi");
        }
        else if(mList.getPayment_method().equalsIgnoreCase("Pay Now"))
        {
            holder.tv_methid1.setText("Online(Paid)");
        }
        holder.tv_date.setText(mList.getOn_date());
        holder.tv_tracking_date.setText(mList.getOn_date());

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");

        if (language.contains("spanish")) {
            String timefrom=mList.getDelivery_time_from();
            String timeto=mList.getDelivery_time_to();

            timefrom=timefrom.replace("pm","م");
            timefrom=timefrom.replace("am","ص");

            timeto=timeto.replace("pm","م");
            timeto=timeto.replace("am","ص");

            String time=timefrom;

            holder.tv_time.setText(time);
        }else {

            String timefrom=mList.getDelivery_time_from();
            String timeto=mList.getDelivery_time_to();
            String time=timefrom;

            holder.tv_time.setText(time);

        }

        holder.tv_price.setText(context.getResources().getString(R.string.currency) + mList.getTotal_amount());
        holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());
//        holder.tv_pending_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_pending_date.setText(mList.getOn_date());
//        holder.tv_confirm_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_confirm_date.setText(mList.getOn_date());
//        holder.tv_delevered_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_delevered_date.setText(mList.getOn_date());
//        holder.tv_cancel_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_cancel_date.setText(mList.getOn_date());
    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

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
        View v = LayoutInflater.from(context).inflate(R.layout.row_order_status,null);
        return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv_status.setText(stat_list.get(position).getStatus());

        }

        @Override
        public int getItemCount() {
            return stat_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_status ,tv_date;
            ImageView iv_order_status;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_status = itemView.findViewById(R.id.tv_order_status);
                tv_date = itemView.findViewById(R.id.tv_order_date);
              iv_order_status = itemView.findViewById(R.id.iv_order_status);
            }
        }
    }


}

