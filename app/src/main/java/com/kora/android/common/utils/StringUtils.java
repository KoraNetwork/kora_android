package com.kora.android.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isIdentifierValid(final String identifier) {
        final Pattern phoneNumberPattern = Pattern.compile("(\\+?)(\\d{10,12})$");
        final Matcher phoneNumberMatcher = phoneNumberPattern.matcher(identifier);

        final Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9_]*$");
        final Matcher userNameMatcher = userNamePattern.matcher(identifier);

        final Pattern emailPattern = Pattern.compile("^(.)+(@)(.)+(\\.)(.)+$");
        final Matcher emailMatcher = emailPattern.matcher(identifier);

        return phoneNumberMatcher.find() || userNameMatcher.find() || emailMatcher.find();
    }

    public static boolean isIdentifierLongEnough(final String identifier) {
        return identifier.length() >= 3;
    }

    public static boolean isPhoneNumberValid(final String phoneNumber) {
        final Pattern pattern = Pattern.compile("(\\d{9,12})$");
        final Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.find();
    }

    public static String deletePlusIfNeeded(final String phoneNumber) {
        if (phoneNumber.startsWith("+"))
            return phoneNumber.substring(1);
        else
            return phoneNumber;
    }

    public static String addPlusIfNeeded(final String phoneNumber) {
        if (phoneNumber.startsWith("+"))
            return phoneNumber;
        else
            return "+" + phoneNumber;
    }

    public static boolean isConfirmationCodeValid(final String confirmationCode) {
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
