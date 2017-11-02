package com.kora.android.common.utils;

import java.text.DateFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public final class Validator {

    private final static String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
    private final static String PHONE_PATTERN = "^\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}$";
    private final static String USER_ID_PATTERN = "([a-zA-Z]{1,})(?=.*[\\d]{0,})[a-zA-Z0-9]{3,65}$";
    private final static String PASSWORD_PATTERN = "((?=\\S*?[A-Z])(?=\\S*?[a-z])(?=\\S*?[0-9]).{5,})\\S";
    private final static String LINK_PATTERN = "^(https?|http|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private final static char DECIMAL_SEPARATOR = DecimalFormatSymbols.getInstance().getDecimalSeparator();
    private final static String PRICE_PATTERN = "^(?!$)(([0-9]{1})([0-9]{0,2})?)?(\\" + DECIMAL_SEPARATOR + "[0-9]{0,2})?";

    private final static int PASSWORD_MIN_LENGTH = 6;

    public static boolean isEmpty(final String field) {
        return field == null || field.equals("");
    }

    public static boolean isNull(final int field) {
        return field <= 0;
    }

    public static boolean isNull(final Object field) {
        return field == null;
    }

    public static boolean isNull(final long field) {
        return field <= 0;
    }

    public static boolean isNull(final Date field) {
        return field == null;
    }

    public static boolean isCorrectEmail(final String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static boolean isCorrectUserId(final String userId) {
        return Pattern.compile(USER_ID_PATTERN).matcher(userId).matches();
    }

    public static boolean isCorrectPassword(final String password) {
        return password.length() >= PASSWORD_MIN_LENGTH;
    }

    public static boolean isCorrectPhone(final String phone) {
        return Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
    }

    public static boolean isEquals(final String password, final String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public static boolean isValidPrice(final CharSequence priceStr) {
        return Pattern.compile(PRICE_PATTERN).matcher(priceStr).matches();
    }

    public static boolean isValidPrice(final double price) {
        return price > 0;
    }

    public static boolean isCorrectLink(String website) {
        return Pattern.compile(LINK_PATTERN).matcher(website).matches();
    }

    public static boolean isCorrectTime(String time) {
        DateFormat df = new SimpleDateFormat("HH:mm", Locale.US);

        try {
            df.parse(time);
            return true;
        } catch (ParseException ignored) {
        }

        return false;
    }

    public static boolean isNewTimeEarly(String newTime) {
        try {
            DateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
            Date newDate = df.parse(newTime);
            Date currentDate = df.parse(df.format(new Date()));
            return newDate.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isFutureDate(String startDate, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.US);
            Date newDate = df.parse(startDate);
            Date currentDate = df.parse(df.format(new Date()));
            return currentDate.before(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isBefore(String startDate, String maturityDate, String prettyDatePattern) {
        try {
            DateFormat df = new SimpleDateFormat(prettyDatePattern, Locale.US);
            Date newDate = df.parse(startDate);
            Date currentDate = df.parse(maturityDate);
            return newDate.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}

