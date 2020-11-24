package com.example.sagaronlineyash.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sagaronlineyash.Config.BaseURL;
import com.example.sagaronlineyash.Model.CategoryModel;
import com.example.sagaronlineyash.R;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Home_adapter extends RecyclerView.Adapter<Home_adapter.MyViewHolder> {

    private List<CategoryModel> modelList;
    private Context context;
    String language;
    String type;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CircleImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.name_category);
            image = view.findViewById(R.id.image_category);
        }
    }

    public Home_adapter(List<CategoryModel> modelList,String type) {
        this.modelList = modelList;
        this.type = type;
    }

    @Override
    public Home_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if(type.equalsIgnoreCase("h"))
        {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_category, parent, false);
        }
        else
        {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_cat, parent, false);
        }

        context = parent.getContext();

        return new Home_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Home_adapter.MyViewHolder holder, int position) {
        CategoryModel mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + mList.getImage())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.title.setText(mList.getTitle());
        }
        else {
            holder.title.setText(mList.getTitle());

        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

