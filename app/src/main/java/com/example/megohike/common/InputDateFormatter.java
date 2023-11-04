package com.example.megohike.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputDateFormatter {

    private static final Pattern pattern = Pattern.compile("\\b\\d{1,2}/\\d{1,2}/\\d{4}\\b");

    public static Boolean isFormatCorrect(String inputDate) {
        if (inputDate != null) {
            final Matcher matcher = pattern.matcher(inputDate);
            return matcher.find();
        }
        return false;
    }

}
