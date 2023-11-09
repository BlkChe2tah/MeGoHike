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
    private static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT2 = new SimpleDateFormat("d MMM yyyy");
    private static final SimpleDateFormat FORMAT2_TIME = new SimpleDateFormat("d MMM yyyy hh:mm aaa");

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

    public static long covertTime(String inputDate) {
        try {
            final Date parseDate = FORMAT_TIME.parse(inputDate);
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

    public static String convertTimeToDateTimeString(Long time) {
        try {
            return FORMAT2_TIME.format(new Date(time));
        } catch (NumberFormatException e) {
            return "0";
        }
    }
}
