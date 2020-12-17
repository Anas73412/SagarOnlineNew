package com.ecom.sagaronline.Model;

public class AddressModel {
    String location_id;
    String user_id;
    String pincode;
    String socity_id;
    String house_no;
    String receiver_name;
    String receiver_moblie;
    String store_id;
    String title;
    String address_type;
    String socity_name;
    String delivery_charge;
    Boolean isChecked;

    public AddressModel(String location_id, String user_id, String pincode, String socity_id, String house_no, String receiver_name, String receiver_moblie, String store_id, String title, String address_type, String socity_name, String delivery_charge, Boolean isChecked) {
        this.location_id = location_id;
        this.user_id = user_id;
        this.pincode = pincode;
        this.socity_id = socity_id;
        this.house_no = house_no;
        this.receiver_name = receiver_name;
        this.receiver_moblie = receiver_moblie;
        this.store_id = store_id;
        this.title = title;
        this.address_type = address_type;
        this.socity_name = socity_name;
        this.delivery_charge = delivery_charge;
        this.isChecked = isChecked;
    }

    public AddressModel() {
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getSocity_id() {
        return socity_id;
    }

    public void setSocity_id(String socity_id) {
        this.socity_id = socity_id;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_moblie() {
        return receiver_moblie;
    }

    public void setReceiver_moblie(String receiver_moblie) {
        this.receiver_moblie = receiver_moblie;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getSocity_name() {
        return socity_name;
    }

    public void setSocity_name(String socity_name) {
        this.socity_name = socity_name;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
