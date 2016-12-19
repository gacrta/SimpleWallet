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

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class InsertValues extends AppCompatActivity {
    //public List<String> savedValues;
    private static String savedValues = "";
    private boolean sign = true;
    Spinner categories;
    DialogFragment datePicker;
    TextView showValue;
    TextView showDate;
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
        showValue.setText("");


        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        showDate = (TextView) findViewById(R.id.show_selected_date);
        showDate.setText(writeDateString(year, month, day));
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
            TextView dateView = (TextView) findViewById(R.id.show_selected_date);
            String dateOfValue = dateView.getText().toString();

            String[] parsedDate = dateOfValue.split(Pattern.quote("/"));
            int year =  Integer.parseInt(parsedDate[0]);
            int month = Integer.parseInt(parsedDate[1])-1;
            int day = Integer.parseInt(parsedDate[2]);

            fromOverview.putExtra(Overview.EXTRA_ADD, (valueAdded+dateOfValue));
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

    String writeDateString(int year, int month, int day) {
        String strYear = Integer.toString(year);
        String strMonth = Integer.toString(month+1);
        String strDay = Integer.toString(day);
        return strYear+"/"+strMonth+"/"+strDay;
    }

}
