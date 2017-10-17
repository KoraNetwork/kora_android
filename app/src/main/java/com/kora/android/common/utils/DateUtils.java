package com.kora.android.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static Date getDate(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);

        try {
            return mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPrettyDate(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM YYYY", Locale.ENGLISH);

        Date date1 = getDate(date);

        if (date1 == null) return "";

        return mFormat.format(date1);
    }

    public static Calendar getCalendarByDatePattern(String dateString, String pattern) {
        SimpleDateFormat mFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date date = null;
        try {
           date =  mFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String getPrettyDate(int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM YYYY", Locale.ENGLISH);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        return mFormat.format(cal.getTime());

    }
}
