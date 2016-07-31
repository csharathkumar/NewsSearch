package com.codepath.newssearch.fragments;

/**
 * Created by Sharath on 7/28/16.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getParentFragment();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}