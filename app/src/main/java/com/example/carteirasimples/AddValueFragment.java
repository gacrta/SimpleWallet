package com.example.carteirasimples;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddValueFragment extends DialogFragment {
    Spinner categories;
    View view;
    TextView selectedDate;

    public interface AddValueListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void dismissAddValueFragment();
    }

    // Use this instance of the interface to deliver action events
    AddValueListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Overview callerActivity = (Overview) getActivity();
        try {
            mListener = (AddValueListener) callerActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(callerActivity.toString() +
                    " must implement AddValueListener");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(callerActivity);
        LayoutInflater inflater = callerActivity.getLayoutInflater();
        view = inflater.inflate(R.layout.add_value_fragment, null);

        builder.setPositiveButton(R.string.button_send, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    boolean sign = true;
                    EditText valueToAdd = (EditText) view.findViewById(R.id.add_value_et);

                    // check if value is income or outcome. Only income is first value 'salary'
                    if (categories.getSelectedItemPosition() != 0) {
                        sign = false;
                    }

                    String valueAdded = valueToAdd.getText().toString();

                    if (!valueAdded.isEmpty() && !valueAdded.equals("0") && !valueAdded.equals(".")) {

                        Overview callerActivity = (Overview) getActivity();

                        float floatValueAdded = Float.parseFloat(valueAdded);
                        TextView dateView = (TextView) view.findViewById(R.id.add_value_tv_show_selected_date);
                        String dateOfValue = dateView.getText().toString();
                        String selectedCategory = (String) categories.getSelectedItem();
                        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
                        long date;
                        try {
                            date = dateFormat.parse(dateOfValue).getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            date = System.currentTimeMillis();
                        }

                        WalletValue newValue = new WalletValue(floatValueAdded, selectedCategory, date, sign);
                        callerActivity.valuesAdded.add(newValue);

                        mListener.onDialogPositiveClick(AddValueFragment.this);

                        AddValueFragment.this.getDialog().dismiss();
                    }
                    else {
                        showMessage();
                    }
                }
            })
            .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddValueFragment.this.getDialog().cancel();
                }
            })
            .setTitle(R.string.insert_value_title)
                .setView(view);

        categories = (Spinner) view.findViewById(R.id.add_value_spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(callerActivity,
                R.array.category_tipes, android.R.layout.simple_spinner_item);
        categories.setAdapter(adapter);

        Button setDateButton = (Button) view.findViewById(R.id.add_value_button_date);
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), getString(R.string.date_picker_fragment_tag));
            }
        });

        final Calendar c = Calendar.getInstance(Locale.getDefault());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        String s = dateFormat.format(new Date(c.getTimeInMillis()));

        selectedDate = (TextView) view.findViewById(R.id.add_value_tv_show_selected_date);
        selectedDate.setText(s);

        return builder.create();
    }

    private void showMessage() {
        Toast.makeText(getContext(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(final DialogInterface dialogInterface){
        super.onDismiss(dialogInterface);
        mListener.dismissAddValueFragment();
    }

}
