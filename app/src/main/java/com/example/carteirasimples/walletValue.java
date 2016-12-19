package com.example.carteirasimples;

/**
 * Created by gabriel.almeida on 19/12/2016.
 */

public class WalletValue {
    private float value;
    private String category;
    private String date;

    public WalletValue() {
    }

    public WalletValue(float value, String category, String date) {
        this.value = value;
        this.category = category;
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {

        return category;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
