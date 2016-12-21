package com.example.carteirasimples;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.regex.Pattern;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    AddValueFragment callerActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get caller activity
        callerActivity = (AddValueFragment) getFragmentManager().findFragmentByTag(getString(R.string.add_value_fragment_tag));
        String dateFromTextView = callerActivity.selectedDate.getText().toString();
        String[] parsedDate = dateFromTextView.split(Pattern.quote("/"));

        int year =  Integer.parseInt(parsedDate[0]);
        int month = Integer.parseInt(parsedDate[1])-1;
        int day = Integer.parseInt(parsedDate[2]);

        return new DatePickerDialog(getContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = callerActivity.writeDateString(year, month, day);

        callerActivity.selectedDate.setText(date);
    }

}
