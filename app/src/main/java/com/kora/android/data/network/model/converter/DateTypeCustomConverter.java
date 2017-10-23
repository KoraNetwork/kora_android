package com.kora.android.data.network.model.converter;

import com.bluelinelabs.logansquare.typeconverters.DateTypeConverter;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTypeCustomConverter extends DateTypeConverter {

    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    @Override
    public DateFormat getDateFormat() {
        return new SimpleDateFormat(PATTERN, Locale.ENGLISH);
    }

    private final ThreadLocal<DateFormat> mDateFormat = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return getDateFormat();
        }
    };

    @Override
    public Date parse(JsonParser jsonParser) throws IOException {
        String dateString = jsonParser.getValueAsString(null).replaceAll("Z$", "+0000");
        if (dateString != null) {
            try {
                return mDateFormat.get().parse(dateString);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
