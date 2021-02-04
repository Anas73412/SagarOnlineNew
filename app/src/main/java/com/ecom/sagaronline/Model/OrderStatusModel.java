package com.ecom.sagaronline.Model;

public class OrderStatusModel {
    String status , date ;
    boolean is_checked;

    public OrderStatusModel(String status, String date, boolean is_checked) {
        this.status = status;
        this.date = date;
        this.is_checked = is_checked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }
}
