package com.kora.android.data.network.model.converter;


import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.TransactionState;

public class TransactionStateConverter extends StringBasedTypeConverter<TransactionState> {

    @Override
    public TransactionState getFromString(String string) {
        return Enum.valueOf(TransactionState.class, string.toUpperCase());
    }

    @Override
    public String convertToString(TransactionState object) {
        return object.name().toLowerCase();
    }
}
