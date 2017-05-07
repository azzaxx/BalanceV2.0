package com.example.alex.balance.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {
    public static final String DATE_DIALOG_DAY_KEY = "date_dialog_day_key";
    public static final String DATE_DIALOG_MONTH_KEY = "date_dialog_month_key";
    public static final String DATE_DIALOG_YEAR_KEY = "date_dialog_year_key";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (getTargetFragment() != null) {
            Intent intent = new Intent();

            intent.putExtra(DATE_DIALOG_DAY_KEY, day);
            intent.putExtra(DATE_DIALOG_MONTH_KEY, month);
            intent.putExtra(DATE_DIALOG_YEAR_KEY, year);

            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        }
    }
}
