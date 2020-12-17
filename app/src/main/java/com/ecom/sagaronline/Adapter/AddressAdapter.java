package com.ecom.sagaronline.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ecom.sagaronline.Model.AddressModel;
import com.ecom.sagaronline.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    List<AddressModel> itemList1;

    int selectedPosition=0;
    Context mContext;
    public AddressAdapter(Context context,List<AddressModel> itemList) {
        this.itemList1 = itemList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {

     /**   holder.tv_name.setText(itemList1.get(position).getReceiver_name());
        holder.tv_phone.setText(itemList1.get(position).getReceiver_moblie());
        holder.tv_address.setText(itemList1.get(position).getHouse_no());*/

        AddressModel model=itemList1.get(position);

        if(model.getTitle()==null || model.getTitle().equals("")) {

            holder.tv_name.setText(model.getReceiver_name());
        }
        else
        {
            holder.tv_name.setText(model.getTitle() + " " + model.getReceiver_name());
        }

        if(model.getReceiver_moblie()==null || model.getReceiver_moblie().equals(""))
        {
            holder.tv_phone.setText("");
        }
        else
        {
            holder.tv_phone.setText(model.getReceiver_moblie());
        }

        if (model.getHouse_no()==null || model.getHouse_no().equals(""))
        {
            holder.tv_address.setText(model.getHouse_no() + "\n" + model.getPincode());
        }
        else
        {
            holder.tv_address.setText(model.getPincode());
        }
        if(model.getChecked())
        {
            holder.addressLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.tv_name.setTextColor(Color.WHITE);
            holder.tv_address.setTextColor(Color.WHITE);
            holder.tv_phone.setTextColor(Color.WHITE);
        }
        else
        {
            holder.addressLayout.setBackgroundColor(Color.WHITE);
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.tv_address.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.tv_phone.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    public int getItemCount() {
        return itemList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_phone,tv_address;
        CardView addressLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=itemView.findViewById(R.id.tv_addname);
            tv_phone=itemView.findViewById(R.id.tv_phone);
            tv_address=itemView.findViewById(R.id.tv_address);
            addressLayout = itemView.findViewById(R.id.address_item);


        }
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public int getSelectedPosition()
    {
        return selectedPosition;
    }
}
