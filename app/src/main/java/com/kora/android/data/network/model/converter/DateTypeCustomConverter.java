package com.kora.android.data.network.model.converter;

import com.bluelinelabs.logansquare.typeconverters.DateTypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateTypeCustomConverter extends DateTypeConverter {

    public DateTypeCustomConverter() {
    }

    @Override
    public DateFormat getDateFormat() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    }
}
