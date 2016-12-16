package com.example.carteirasimples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Overview extends AppCompatActivity {

    public final static String EXTRA_ADD = "com.example.carteirasimples.ADD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
    }

    public void addValue(View view) {
        Intent callInsertValue = new Intent(this, InsertValues.class);
        startActivity(callInsertValue);
    }

}
