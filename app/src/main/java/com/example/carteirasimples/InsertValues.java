package com.example.carteirasimples;

import android.app.Activity;
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
    //public List<String> savedValues;
    private static String savedValues = "";
    private boolean sign = true;
    Spinner categories;
    DialogFragment datePicker;
    TextView showValue;
    Intent fromOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_value);

        // Now InsertValues is called from Overview
        fromOverview = getIntent();

        // Iniciate categories spinner
        categories = (Spinner) findViewById(R.id.choose_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_tipes, android.R.layout.simple_spinner_item);
        categories.setAdapter(adapter);
        datePicker = new DatePickerFragment();
        showValue = (TextView) findViewById(R.id.show_add_status);
        showValue.setText(R.string.no_date_input);
    }

    /* called after user press cancel button */
    public void cancelOperation(View view) {
        setResult(Activity.RESULT_CANCELED, fromOverview);
        finish();
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

        if (!valueAdded.isEmpty() && !valueAdded.equals("0") && !valueAdded.equals(".")) {

            valueAdded = signal + valueAdded;
            float floatValueAdded = Float.parseFloat(valueAdded);
            TextView dateView = (TextView) findViewById(R.id.)
            String dateOfValue =

            fromOverview.putExtra(Overview.EXTRA_ADD, valueAdded);
            setResult(Activity.RESULT_OK, fromOverview);
            finish();

        } else {
            showValue.setText(R.string.invalid_input);
        }

        valueToAdd.setText("");
    }

    public void showDatePickerDialog(View view){
        datePicker.show(getSupportFragmentManager(),"datePicker");
    }

}
