package com.ecom.sagaronline.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ecom.sagaronline.R;
import com.synnapps.carouselview.CarouselView;

public class NewDetailFragment extends Fragment {
    TextView tv_details_product_price,tv_details_product_mrp,tv_details_product_off,tv_details_product_description,tv_descriptionTitle,tv_details_product_name;
    Button btn_add,btn_buy_now,btn_checkout;
    NumberPicker product_qty;
    RelativeLayout rel_out,lin_img;
    ImageView wish_before,wish_after;
    CarouselView img_slider;
    RecyclerView top_selling_recycler;

    String product_id;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, container, false);
        tv_details_product_price=view.findViewById(R.id.details_product_price);
        tv_details_product_mrp=view.findViewById(R.id.details_product_mrp);
        tv_details_product_off=view.findViewById(R.id.details_product_per);
        tv_details_product_description=view.findViewById(R.id.details_product_description);
        tv_descriptionTitle=view.findViewById(R.id.descriptionTitle);
        tv_details_product_name=view.findViewById(R.id.details_product_name);
        wish_before=view.findViewById(R.id.wish_before);
        wish_after=view.findViewById(R.id.wish_after);
        rel_out= view.findViewById( R.id.rel_out );
        lin_img = view.findViewById(R.id.relative_layout_img);
        img_slider = view.findViewById(R.id.img_slider);
        top_selling_recycler = view.findViewById(R.id.top_selling_recycler);

        btn_checkout = view.findViewById(R.id.btn_f_Add_to_cart);
        btn_add=view.findViewById(R.id.btn_add);
        btn_buy_now=view.findViewById(R.id.btn_buy_now);
        product_qty=view.findViewById(R.id.product_qty);

        Bundle bundle=getArguments();
        product_id=bundle.getString("product_id");

        return view;
    }


}
