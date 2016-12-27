package com.example.carteirasimples;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Overview extends AppCompatActivity implements AddValueFragment.AddValueListener,
        WalletOverviewFragment.ViewWalletListListener {

    static List<WalletValue> valuesAdded;
    TextView textView;
    Handler mHandler;
    private static final Uri CONTENT_URI = WalletValuesContract.WalletItens.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        textView = (TextView) findViewById(R.id.show_value_added);
        valuesAdded = new ArrayList<WalletValue>();
        readWalletValuesDatabase();
        updateSummary();
        
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                WalletValue newValue = (WalletValue) inputMessage.obj;
                switch (inputMessage.what){
                    case 1:
                        String message = getString(R.string.added) + " " + newValue.getValue()
                                + " on " + newValue.getCategory() + " on " + newValue.getDate();
                        textView.setText(message);
                        updateSummary();
                        break;
                    default:
                        super.handleMessage(inputMessage);
                }
            }
        };
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState == null) {
                WalletOverviewFragment firstFragment = new WalletOverviewFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, firstFragment).commit();
            }
        }
    }

    protected void updateSummary() {
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

    public void addValue() {
        DialogFragment addDialog = new AddValueFragment();
        addDialog.show(getSupportFragmentManager(), getString(R.string.add_value_fragment_tag));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_button:
                addValue();
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

    /* called after user press view button */
    public void changeToWalletView(View view) {
        Intent walletViewIntent = new Intent(this, ViewValues.class);
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

    public void onDialogPositiveClick(DialogFragment dialog) {
        WalletValue newValue = valuesAdded.get(valuesAdded.size()-1);

        ContentValues contentValues = new ContentValues();
        contentValues.put(WalletValuesContract.WalletItens.COLUMN_VALUE, Float.toString(newValue.getValue()));
        contentValues.put(WalletValuesContract.WalletItens.COLUMN_CATEGORY, newValue.getCategory());
        contentValues.put(WalletValuesContract.WalletItens.COLUMN_DATE, newValue.getDate());
        contentValues.put(WalletValuesContract.WalletItens.COLUMN_SIGN, Boolean.toString(newValue.getSign()));
        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);

        Message message = mHandler.obtainMessage(1,newValue);
        mHandler.sendMessage(message);
    }

    public void readWalletValuesDatabase() {
        Cursor cursor = getContentResolver().query(CONTENT_URI, WalletValuesContract.WalletItens.PROJECTION_ALL, null, null, null);
        try {
            cursor.moveToFirst();
            WalletValue newValue;
            while (!cursor.isAfterLast()) {
                String strValue = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_VALUE));
                String strCategory = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_CATEGORY));
                String strDate = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_DATE));
                String strSign = cursor.getString(cursor.getColumnIndex(WalletValuesContract.WalletItens.COLUMN_SIGN));
                newValue = new WalletValue(Float.parseFloat(strValue), strCategory, strDate, Boolean.parseBoolean(strSign));
                valuesAdded.add(newValue);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (NullPointerException e) {
            throw new NullPointerException("Not possible to read database");
        }
    }

    public void onButtonClick() {
        WalletValuesFragment secondFragment = new WalletValuesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, secondFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
