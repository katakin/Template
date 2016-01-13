package ru.katakin.template.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Parser {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date stringToDate(String date) {
        if (TextUtils.isEmpty(date)) return null;
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static long stringToLong(String numbers) {
        try {
            return Long.parseLong(numbers);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
