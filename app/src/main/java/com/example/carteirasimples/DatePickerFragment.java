package com.example.carteirasimples;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    AddValueFragment callerFragment;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get caller fragment
        callerFragment = (AddValueFragment) getFragmentManager().findFragmentByTag(getString(R.string.add_value_fragment_tag));

        // get date from addValeFrag textView
        String dateFromTextView = callerFragment.selectedDate.getText().toString();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        final Calendar c = Calendar.getInstance(Locale.getDefault());
        try {
            long date = dateFormat.parse(dateFromTextView).getTime();
            c.setTimeInMillis(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // set date as default DatePicker value
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // get selected date
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.set(year, month, day);

        // write it in AddValueFrag textView
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        String date = dateFormat.format(new Date(c.getTimeInMillis()));

        callerFragment.selectedDate.setText(date);
    }

}
