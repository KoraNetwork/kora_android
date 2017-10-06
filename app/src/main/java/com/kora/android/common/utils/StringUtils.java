package com.kora.android.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isPhoneNumberValid(String phoneNumber) {
        final Pattern pattern = Pattern.compile("^(\\+?)(\\d{10,12})$");
        final Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.find();
    }

    public static String deletePlusIfNeeded(String phoneNumber) {
        if (phoneNumber.startsWith("+"))
            phoneNumber = phoneNumber.substring(1);
        return phoneNumber;
    }

    public static boolean isConfirmationCodeValid(String confirmationCode) {
        final Pattern pattern = Pattern.compile("^(\\d{4})$");
        final Matcher matcher = pattern.matcher(confirmationCode);
        return matcher.find();
    }

    public static String getCodeFromMessage(final String message) {
        return message.replaceAll("[^0-9]", "");
    }

    public static boolean isPinCodeLongEnough(final String pinCode) {
        return pinCode.length() == 4;
    }

    public static boolean isUserNameLongEnough(final String userName) {
        return userName.length() >= 3;
    }

    public static boolean isUserNameValid(final String userName) {
        final Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]*$");
        final Matcher matcher = pattern.matcher(userName);
        return matcher.find();
    }

    public static boolean isEmailValid(final String email) {
        final Pattern pattern = Pattern.compile("^(.)+(@)(.)+(\\.)(.)+$");
        final Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public static boolean isPasswordLongEnough(final String password) {
        return password.length() >= 6;
    }
}
