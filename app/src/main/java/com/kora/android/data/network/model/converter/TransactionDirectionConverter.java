package com.kora.android.data.network.model.converter;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.TransactionDirection;

public class TransactionDirectionConverter  extends StringBasedTypeConverter<TransactionDirection> {

    @Override
    public TransactionDirection getFromString(String string) {
        return Enum.valueOf(TransactionDirection.class, string.toUpperCase());
    }

    @Override
    public String convertToString(TransactionDirection object) {
        return object.name().toLowerCase();
    }


}
