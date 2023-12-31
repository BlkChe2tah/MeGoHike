package com.example.megohike.common;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputDateFormatter {

    private static final Pattern pattern = Pattern.compile("\\b\\d{1,2}/\\d{1,2}/\\d{4}\\b");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static Boolean isFormatCorrect(String inputDate) {
        if (inputDate != null) {
            final Matcher matcher = pattern.matcher(inputDate);
            final boolean isMatch = matcher.find();
            if (!isMatch) return false;
            try {
                FORMAT.parse(inputDate);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return null;
    }

}
