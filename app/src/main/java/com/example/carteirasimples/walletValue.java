package com.example.carteirasimples;

import android.database.Cursor;

/**
 * Created by gabriel.almeida on 19/12/2016.
 */

public class WalletValue {
    private float value;
    private String category;
    private String date;
    private boolean sign;   //sign == true represents income

    public WalletValue(float value, String category, String date, boolean sign) {
        this.value = value;
        this.category = category;
        this.date = date;
        this.sign = sign;
    }

    public WalletValue(Cursor cursor){
        value = Float.parseFloat(cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_VALUE)));
        category = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_CATEGORY));
        date = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_DATE));
        sign = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_SIGN)));
    }

    public float getValue() { return value; }

    public String getDate() { return date; }

    public String getCategory() { return category; }

    public boolean getSign() { return sign; }

    public void setValue(float value) {
        this.value = value;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSign(boolean sign) { this.sign = sign; }
}
