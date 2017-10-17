package com.kora.android.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String PRETTY_DATE_PATTERN = "dd MMMM yyyy";
    private static final String FORMATTED_DATE_PATTERN = "yyyy-MM-dd";

    public static Date getDate(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            return mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPrettyDate(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

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

    public static String getPrettyDate(final int year, final int month, final int day) {
        return getDate(PRETTY_DATE_PATTERN, year, month, day);
    }

    public static String getFormattedDate(final int year, final int month, final int day) {
        return getDate(FORMATTED_DATE_PATTERN, year, month, day);
    }

    public static String getDate(final String pattern, final int year, final int month, final int day) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return simpleDateFormat.format(calendar.getTime());
    }

    public static boolean isDateValid(final String date) {

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        final Date validDate = calendar.getTime();

        final Date userDate;
        try {
            userDate = new SimpleDateFormat(FORMATTED_DATE_PATTERN, Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        if (userDate.equals(validDate))
            return true;
        else if (userDate.before(validDate))
            return true;
        else if (userDate.after(validDate))
            return false;

        return false;
    }
}
