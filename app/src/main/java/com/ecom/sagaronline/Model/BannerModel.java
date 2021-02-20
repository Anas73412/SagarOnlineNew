package com.ecom.sagaronline.Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 20,February,2021
 */
public class BannerModel {
    String id;
    String slider_title;
    String slider_image;
    String sub_cat;
    String slider_status;
    String cat_id;
    String title;
    String parent;
    String leval;
    String slab_value;
    String max_slab_amt;

    public BannerModel() {
    }

    public BannerModel(String id, String slider_title, String slider_image, String sub_cat, String slider_status, String cat_id, String title, String parent, String leval, String slab_value, String max_slab_amt) {
        this.id = id;
        this.slider_title = slider_title;
        this.slider_image = slider_image;
        this.sub_cat = sub_cat;
        this.slider_status = slider_status;
        this.cat_id = cat_id;
        this.title = title;
        this.parent = parent;
        this.leval = leval;
        this.slab_value = slab_value;
        this.max_slab_amt = max_slab_amt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlider_title() {
        return slider_title;
    }

    public void setSlider_title(String slider_title) {
        this.slider_title = slider_title;
    }

    public String getSlider_image() {
        return slider_image;
    }

    public void setSlider_image(String slider_image) {
        this.slider_image = slider_image;
    }

    public String getSub_cat() {
        return sub_cat;
    }

    public void setSub_cat(String sub_cat) {
        this.sub_cat = sub_cat;
    }

    public String getSlider_status() {
        return slider_status;
    }

    public void setSlider_status(String slider_status) {
        this.slider_status = slider_status;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
