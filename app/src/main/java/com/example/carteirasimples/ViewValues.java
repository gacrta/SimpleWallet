package com.example.carteirasimples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ViewValues extends AppCompatActivity {
    ListView walletListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_values);

        WalletValuesAdapter adapter = new WalletValuesAdapter(this, R.layout.list_wallet_values, Overview.valuesAdded);

        walletListView = (ListView) findViewById(R.id.wallet_list_view);
        walletListView.setAdapter(adapter);
    }

}
