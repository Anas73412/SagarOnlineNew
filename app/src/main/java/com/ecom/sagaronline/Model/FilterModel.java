package com.ecom.sagaronline.Model;

public class FilterModel {
    String text;
    String min;
    String max;

    public FilterModel(String text, String min, String max) {
        this.text = text;
        this.min = min;
        this.max = max;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
