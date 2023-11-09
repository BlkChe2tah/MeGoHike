package com.example.megohike.common;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class AppTimePicker {

    public interface TimeSetListener {
        void onTimeSet(String time, String tag);
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        private final TimeSetListener listener;
        private final String tag;

        public TimePickerFragment(@NonNull String tag, @NonNull TimeSetListener listener) {
            this.tag = tag;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(requireContext(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.onTimeSet(String.format("%d:%d", hourOfDay, minute), tag);
        }
    }

}
