package com.dolphin.component;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {
    protected OnDateSetListener listener;
    protected Date initialDate;

    public interface OnDateSetListener {
        void onDateSet(Date date);
    }

    public static DatePickerDialog newInstance(Date initialDate, OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog();
        dialog.initialDate = initialDate;
        dialog.listener = listener;
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);

        if (listener != null) {
            listener.onDateSet(calendar.getTime());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        if (initialDate != null) {
            calendar.setTime(initialDate);
        }

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
    }
}
