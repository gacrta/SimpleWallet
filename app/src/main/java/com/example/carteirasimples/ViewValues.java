package com.example.carteirasimples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.regex.Pattern;

public class ViewValues extends AppCompatActivity {
    ListView walletListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_values);

        Intent fromInsertValues = getIntent();
        String message = fromInsertValues.getStringExtra(Overview.EXTRA_MESSAGE);
        String data[] = message.split(Pattern.quote("|"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        walletListView = (ListView) findViewById(R.id.wallet_list_view);
        walletListView.setAdapter(arrayAdapter);
    }

}
