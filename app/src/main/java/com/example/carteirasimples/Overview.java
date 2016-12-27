package com.example.carteirasimples;

import android.content.ContentValues;
import android.database.Cursor;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Overview extends AppCompatActivity implements AddValueFragment.AddValueListener,
        WalletOverviewFragment.ViewWalletListListener {

    List<WalletValue> valuesAdded;
    Handler mHandler;
    private static final Uri CONTENT_URI = WalletValuesContract.WalletItens.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        valuesAdded = new ArrayList<>();
        readWalletValuesDatabase();
        
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what){
                    case 1:
                        showMessage();
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

    private void showMessage() {
        Toast.makeText(this, getString(R.string.added), Toast.LENGTH_SHORT).show();
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

    public void viewWalletDetails() {
        WalletListFragment secondFragment = new WalletListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, secondFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
