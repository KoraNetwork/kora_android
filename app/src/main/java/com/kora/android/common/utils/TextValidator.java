package com.kora.android.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidator {

    public static boolean isPhoneNumberValid(String phoneNumber) {
        final Pattern pattern = Pattern.compile("^(\\+?)(\\d{10,12})$");
        final Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.find();
    }

    public static String addPlusIfNeeded(String phoneNumber) {
        if (!phoneNumber.startsWith("+"))
            phoneNumber = "+" + phoneNumber;
        return phoneNumber;
    }
}
