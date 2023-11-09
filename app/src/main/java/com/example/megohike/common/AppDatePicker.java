package com.example.megohike.common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class AppDatePicker {

    public interface DateSetListener {
        void onDateSet(String date, String tag);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private final DateSetListener listener;
        private final String tag;

        public DatePickerFragment(@NonNull String tag, @NonNull DateSetListener listener) {
            this.tag = tag;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            listener.onDateSet(String.format("%d/%d/%d", day, month + 1, year), tag);
        }
    }

}
