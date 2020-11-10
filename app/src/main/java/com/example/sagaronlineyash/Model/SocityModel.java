package com.example.sagaronlineyash.Model;

public class SocityModel {
    String socity_id;
    String pincode;
    String socity_name;
    String delivery_charge;

    public SocityModel(String socity_id, String pincode, String socity_name, String delivery_charge) {
        this.socity_id = socity_id;
        this.pincode = pincode;
        this.socity_name = socity_name;
        this.delivery_charge = delivery_charge;
    }

    public String getSocity_name() {
        return socity_name;
    }

    public void setSocity_name(String socity_name) {
        this.socity_name = socity_name;
    }

    public String getSocity_id() {
        return socity_id;
    }

    public void setSocity_id(String socity_id) {
        this.socity_id = socity_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }
}
