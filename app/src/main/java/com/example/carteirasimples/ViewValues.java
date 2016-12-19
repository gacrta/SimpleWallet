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

        //Intent fromInsertValues = getIntent();
        //String message = fromInsertValues.getStringExtra(Overview.EXTRA_MESSAGE);
        //String data[] = message.split(Pattern.quote("|"));
        //WalletValue allValues[] = new WalletValue[Overview.valuesAdded.size()];
        ArrayList allValues = new ArrayList(Overview.valuesAdded);
        //allValues = Overview.valuesAdded.toArray(allValues);

        /*
        int N = allValues.length;
        Float data[] = new Float[N];
        for(int i = 0; i < N; i++){
            data[i] = allValues[i].getValue();
        }

        ArrayAdapter<Float> arrayAdapter = new ArrayAdapter<Float>(this, android.R.layout.simple_list_item_1, data);
        */
        WalletValuesAdapter adapter = new WalletValuesAdapter(this, R.layout.list_wallet_values, Overview.valuesAdded);

        walletListView = (ListView) findViewById(R.id.wallet_list_view);
        walletListView.setAdapter(adapter);
    }

}
