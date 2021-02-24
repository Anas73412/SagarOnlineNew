package com.ecom.sagaronline.Model;

public class ProductVariantModel {

    String id,product_id,attribute_name,attribute_value,attribute_mrp,attribute_color,att_stock,status,attribute_size;
    boolean checked;


    public ProductVariantModel() {
    }

    public ProductVariantModel(String id, String product_id, String attribute_name, String attribute_value,
                               String attribute_mrp, String attribute_color) {
        this.id = id;
        this.product_id = product_id;
        this.attribute_name = attribute_name;
        this.attribute_value = attribute_value;
        this.attribute_mrp = attribute_mrp;
        this.attribute_color = attribute_color;
    }

    public ProductVariantModel(String id, String product_id, String attribute_name,
                               String attribute_value, String attribute_mrp, String attribute_color,
                               String att_stock, String status, String attribute_size, boolean checked) {
        this.id = id;
        this.product_id = product_id;
        this.attribute_name = attribute_name;
        this.attribute_value = attribute_value;
        this.attribute_mrp = attribute_mrp;
        this.attribute_color = attribute_color;
        this.att_stock = att_stock;
        this.status = status;
        this.attribute_size = attribute_size;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }

    public String getAttribute_mrp() {
        return attribute_mrp;
    }

    public void setAttribute_mrp(String attribute_mrp) {
        this.attribute_mrp = attribute_mrp;
    }

    public String getAttribute_color() {
        return attribute_color;
    }

    public void setAttribute_color(String attribute_color) {
        this.attribute_color = attribute_color;
    }

    public String getAtt_stock() {
        return att_stock;
    }

    public void setAtt_stock(String att_stock) {
        this.att_stock = att_stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAttribute_size() {
        return attribute_size;
    }

    public void setAttribute_size(String attribute_size) {
        this.attribute_size = attribute_size;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
