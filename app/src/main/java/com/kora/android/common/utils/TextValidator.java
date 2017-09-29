package com.kora.android.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidator {

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
}
