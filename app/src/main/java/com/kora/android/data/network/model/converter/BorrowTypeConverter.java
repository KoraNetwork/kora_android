package com.kora.android.data.network.model.converter;


import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.BorrowType;

public class BorrowTypeConverter extends StringBasedTypeConverter<BorrowType> {

    @Override
    public BorrowType getFromString(final String string) {
        return Enum.valueOf(BorrowType.class, string.toUpperCase());
    }

    @Override
    public String convertToString(final BorrowType borrowType) {
        return borrowType.name().toLowerCase();
    }
}
