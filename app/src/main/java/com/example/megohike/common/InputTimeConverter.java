package com.example.megohike.common;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputTimeConverter {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat INPUT_TIME_FORMAT = new SimpleDateFormat("H:mm");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat OUTPUT_TIME_FORMAT = new SimpleDateFormat("hh:mm aaa");

    public static String convert24to12(String time) {
        try {
            final Date parseTime = INPUT_TIME_FORMAT.parse(time);
            if (parseTime == null) {
                return "0";
            } else {
                return OUTPUT_TIME_FORMAT.format(parseTime);
            }
        } catch (ParseException | NumberFormatException e) {
            return "0";
        }
    }
}
