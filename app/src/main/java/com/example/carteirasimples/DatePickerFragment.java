package com.example.carteirasimples;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    InsertValues callerActivity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get caller activity
        callerActivity = (InsertValues) getActivity();
        String dateFromTextView = callerActivity.showDate.getText().toString();
        int year = 0;
        int month = 0;
        int day = 0;
        if (dateFromTextView.equals(getString(R.string.no_date_input))) {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        else {
            String[] parsedDate = dateFromTextView.split(Pattern.quote("/"));
            year =  Integer.parseInt(parsedDate[0]);
            month =  Integer.parseInt(parsedDate[1])-1;
            day =  Integer.parseInt(parsedDate[2]);

        }
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = writeDateString(year, month, day);

        callerActivity.showDate.setText(date);
    }

    private String writeDateString(int year, int month, int day) {
        String strYear = Integer.toString(year);
        String strMonth = Integer.toString(month+1);
        String strDay = Integer.toString(day);
        return strYear+"/"+strMonth+"/"+strDay;
    }

}
