package com.example.carteirasimples;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Overview extends AppCompatActivity {

    static final int GET_NEW_VALUE = 1; //request code for new value
    static List<WalletValue> valuesAdded;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        textView = (TextView) findViewById(R.id.show_value_added);
        valuesAdded = new ArrayList<WalletValue>();
    }

    @Override
    protected void onStart () {
        super.onStart();
        float income = getIncomeSum();
        float outcome = getOutcomeSum();
        float balance = income - outcome;

        TextView tv_income = (TextView) findViewById(R.id.tv_income_value);
        tv_income.setText(String.format(java.util.Locale.getDefault(),"%.2f", income));
        TextView tv_outcome = (TextView) findViewById(R.id.tv_outcome_value);
        tv_outcome.setText(String.format(java.util.Locale.getDefault(),"%.2f", outcome));
        TextView tv_balance = (TextView) findViewById(R.id.tv_balance_value);
        tv_balance.setText(String.format(java.util.Locale.getDefault(),"%.2f", balance));
        if (balance < 0.0) {
            tv_balance.setTextColor(Color.parseColor("#a00103"));
        }
        else {
            tv_balance.setTextColor(Color.parseColor("#74ba48"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_button:
                Intent callInsertValue = new Intent(this, InsertValues.class);
                startActivityForResult(callInsertValue, GET_NEW_VALUE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check requestCode
        if (requestCode == GET_NEW_VALUE) {
            // Check if request was sucessful
            if (resultCode == RESULT_OK) {
                WalletValue newValue = valuesAdded.get(valuesAdded.size()-1);
                String message = getString(R.string.added) + " " + newValue.getValue()
                        + " on " + newValue.getCategory() + " on " + newValue.getDate();
                textView.setText(message);
            }

        }
    }

    /* called after user press view button */
    public void changeToWalletView(View view) {
        Intent walletViewIntent = new Intent(this, ViewValues.class);
        //walletViewIntent.putExtra(EXTRA_MESSAGE, valuesAdded);
        startActivity(walletViewIntent);
    }

    // function to evaluate total income
    public float getIncomeSum() {
        int valuesNumber = valuesAdded.size();
        float sum = 0;
        WalletValue walletValue;
        for(int i = 0; i < valuesNumber; i++) {
            walletValue = valuesAdded.get(i);
            if (walletValue.getSign()) {
                sum += walletValue.getValue();
            }
        }
        return sum;
    }

    // function to evaluate total outcome
    public float getOutcomeSum() {
        int valuesNumber = valuesAdded.size();
        float sum = 0;
        WalletValue walletValue;
        for(int i = 0; i < valuesNumber; i++) {
            walletValue = valuesAdded.get(i);
            if (!walletValue.getSign()) {
                sum += walletValue.getValue();
            }
        }
        return sum;
    }
}
