package com.kora.android.data.network.model.converter;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.TransactionType;

public class TransactionTypeConverter extends StringBasedTypeConverter<TransactionType> {

    @Override
    public TransactionType getFromString(String string) {
        return Enum.valueOf(TransactionType.class, string.toUpperCase());
    }

    @Override
    public String convertToString(TransactionType object) {
        return object.name().toLowerCase();
    }

}
