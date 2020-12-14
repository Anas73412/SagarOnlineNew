package com.example.sagaronlineyash.Model;



public class Socity_model {

    String socity_id;
    String socity_name;
    String pincode;

    String delivery_charge;

    public void setSocity_id(String socity_id) {
        this.socity_id = socity_id;
    }

    public void setSocity_name(String socity_name) {
        this.socity_name = socity_name;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getSocity_id(){
        return socity_id;
    }

    public String getSocity_name(){
        return socity_name;
    }

    public String getPincode(){
        return pincode;
    }



    public String getDelivery_charge(){
        return delivery_charge;
    }

}
