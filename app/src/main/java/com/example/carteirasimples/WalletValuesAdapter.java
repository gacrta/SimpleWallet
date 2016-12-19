package com.example.carteirasimples;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel.almeida on 19/12/2016.
 */

public class WalletValuesAdapter extends ArrayAdapter<WalletValue> {
    private List<WalletValue> obj;

    public WalletValuesAdapter(Context c, int viewId, List<WalletValue> objects) {
        super(c,viewId,objects);
        obj = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_wallet_values, null);
        }

        WalletValue walletValue = obj.get(position);
        if(walletValue!= null) {
            ImageView iv = (ImageView) v.findViewById(R.id.sign_image);
            TextView tvValue = (TextView) v.findViewById(R.id.tv_value);
            TextView tvCategory = (TextView) v.findViewById(R.id.tv_category);
            TextView tvDate = (TextView) v.findViewById(R.id.tv_date);

            if (tvValue != null) {
                tvValue.setText(Float.toString(walletValue.getValue()));
            }
            if (tvCategory != null) {
                tvCategory.setText(walletValue.getCategory());
            }
            if (tvDate != null) {
                tvDate.setText(walletValue.getDate());
            }
            if (iv != null) {
                if(walletValue.getSign()) {
                    iv.setImageResource(R.drawable.green_plus);
                }
                else {
                    iv.setImageResource(R.drawable.red_minus);
                }
            }
        }
        return v;
    }

}
