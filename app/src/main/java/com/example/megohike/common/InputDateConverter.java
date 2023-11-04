package com.example.megohike.common;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputDateConverter {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT2 = new SimpleDateFormat("d MMM yyyy");

    public static long covert(String inputDate) {
        try {
            final Date parseDate = FORMAT.parse(inputDate);
            if (parseDate == null) {
                return 0;
            } else {
                return parseDate.getTime();
            }
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String convertTimeToDateString(Long time) {
        try {
            return FORMAT2.format(new Date(time));
        } catch (NumberFormatException e) {
            return "0";
        }
    }
}
