package com.example.carteirasimples;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

public class InsertValues extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.carteirasimples.MESSAGE";
    //public List<String> savedValues;
    private static String savedValues = "";
    private boolean sign = true;
    Spinner categories;
    DialogFragment datePicker;
    TextView showValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_value);

        // Iniciate categories spinner
        categories = (Spinner) findViewById(R.id.choose_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_tipes, android.R.layout.simple_spinner_item);
        categories.setAdapter(adapter);
        datePicker = new DatePickerFragment();
        showValue = (TextView) findViewById(R.id.show_value_added);
        showValue.setText(R.string.no_date_input);
    }

    /* called after user press view button */
    public void changeToWalletView(View view) {
        Intent walletViewIntent = new Intent(this, ViewValues.class);
        walletViewIntent.putExtra(EXTRA_MESSAGE, savedValues);
        startActivity(walletViewIntent);
    }

    /* called after user press save button */
    public void saveValue(View view) {
        String signal = "";
        //showValue = (TextView) findViewById(R.id.show_value_added);
        EditText valueToAdd = (EditText) findViewById(R.id.value_to_add);

        // check if value is income or outcome. Only income is first value 'salary'
        if (categories.getSelectedItemPosition() != 0) {
            signal = "-";
        }
        String valueAdded = valueToAdd.getText().toString();

        if (!valueAdded.isEmpty() && !valueAdded.equals("0")) {
            savedValues += signal + valueAdded + "|";
            String message = getString(R.string.added) + " " + signal + valueAdded;
            showValue.setText(message);
        } else {
            showValue.setText(R.string.invalid_input);
        }

        //savedValues.add(valueAdded);

        valueToAdd.setText("");
    }

    public void showDatePickerDialog(View view){
        datePicker.show(getSupportFragmentManager(),"datePicker");
        Intent date = getIntent();
    }

}
