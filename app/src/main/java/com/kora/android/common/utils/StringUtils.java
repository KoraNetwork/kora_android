package com.kora.android.common.utils;

import android.os.Build;
import android.telephony.PhoneNumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final String USER_NAME_PATTERN = "^[a-zA-Z0-9_]{3,}$"; // matcher.matches();
    private static final String EMAIL_PATTERN = "^(.)+(@)(.)+(\\.)(.)+$"; // matcher.find();
    private static final String FULL_PHONE_NUMBER_PATTERN = "(\\+?)(\\d{10,13})$"; // matcher.find();
    private static final String SHORT_PHONE_NUMBER_PATTERN = "(\\d{9,12})$"; // matcher.find();
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"; // matcher.find();
    private static final int DEFAULT_FONE_NUMBER_COUNT = 12;
    public static final int NIGERIAN_PHONE_NUMBER_COUNT = 13;

    public static boolean isUserNameValid(final String userName) {
        final Pattern pattern = Pattern.compile(USER_NAME_PATTERN);
        final Matcher matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    public static boolean isEmailValid(final String email) {
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public static boolean isPhoneNumberValid(final String phoneNumber) {
        final Pattern pattern = Pattern.compile(SHORT_PHONE_NUMBER_PATTERN);
        final Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.find();
    }

    public static boolean isIdentifierValid(final String identifier) {
        final Pattern phoneNumberPattern = Pattern.compile(FULL_PHONE_NUMBER_PATTERN);
        final Matcher phoneNumberMatcher = phoneNumberPattern.matcher(identifier);

        final Pattern userNamePattern = Pattern.compile(USER_NAME_PATTERN);
        final Matcher userNameMatcher = userNamePattern.matcher(identifier);

        final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher emailMatcher = emailPattern.matcher(identifier);

        return phoneNumberMatcher.find() || userNameMatcher.matches() || emailMatcher.find();
    }

    public static boolean isPasswordValid(final String password) {
        final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        final Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static boolean isConfirmationCodeValid(final String confirmationCode) {
        final Pattern pattern = Pattern.compile("^(\\d{4})$");
        final Matcher matcher = pattern.matcher(confirmationCode);
        return matcher.find();
    }

    public static boolean isPinCodeLongEnough(final String pinCode) {
        return pinCode.length() == 4;
    }

    public static String deletePlusIfNeeded(final String phoneNumber) {
        if (phoneNumber == null) return "";
        if (phoneNumber.startsWith("+"))
            return phoneNumber.substring(1);
        else
            return phoneNumber;
    }

    public static String addPlusIfNeeded(final String phoneNumber) {
        if (phoneNumber == null) return "";
        if ((phoneNumber.length() == DEFAULT_FONE_NUMBER_COUNT || phoneNumber.length() == NIGERIAN_PHONE_NUMBER_COUNT) && !phoneNumber.startsWith("+")) {
            return "+" + phoneNumber;
        } else return phoneNumber;
    }

    public static String getCodeFromMessage(final String message) {
        return message.replaceAll("[^0-9]", "");
    }

    public static String getFormattedPhoneNumber(String phone, String countryCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(addPlusIfNeeded(phone), countryCode);
        } else {
           return PhoneNumberUtils.formatNumber(addPlusIfNeeded(phone)); //Deprecated method
        }
    }

    public static String getSimplePhoneNumber(final String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]","");
    }
}
