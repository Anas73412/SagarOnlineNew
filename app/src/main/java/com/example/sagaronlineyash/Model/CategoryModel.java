package com.example.sagaronlineyash.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryModel {
    String id;
    String title;
    String slug;
    String parent;
    String leval;
    String description;
    String arb_title;
    String image;
    String status;
    String Count;
    String PCount;
    String slab_value;
    String max_slab_amt;



    @SerializedName("sub_cat")
    ArrayList<Category_subcat_model> category_sub_datas;
    public String getId() {
        return id;
    }

    public String getArb_title() {
        return arb_title;
    }

    public void setArb_title(String arb_title) {
        this.arb_title = arb_title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlab_value() {
        return slab_value;
    }

    public void setSlab_value(String slab_value) {
        this.slab_value = slab_value;
    }

    public String getMax_slab_amt() {
        return max_slab_amt;
    }

    public void setMax_slab_amt(String max_slab_amt) {
        this.max_slab_amt = max_slab_amt;
    }

    public void setCategory_sub_datas(ArrayList<Category_subcat_model> category_sub_datas) {
        this.category_sub_datas = category_sub_datas;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLeval() {
        return leval;
    }

    public void setLeval(String leval) {
        this.leval = leval;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getPCount() {
        return PCount;
    }

    public void setPCount(String PCount) {
        this.PCount = PCount;
    }


    public ArrayList<Category_subcat_model> getCategory_sub_datas(){
        return category_sub_datas;
    }
}
