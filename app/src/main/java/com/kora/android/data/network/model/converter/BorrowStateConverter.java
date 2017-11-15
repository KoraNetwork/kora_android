package com.kora.android.data.network.model.converter;


import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.BorrowState;

public class BorrowStateConverter extends StringBasedTypeConverter<BorrowState> {

    @Override
    public BorrowState getFromString(final String string) {
        return Enum.valueOf(BorrowState.class, string.toUpperCase());
    }

    @Override
    public String convertToString(final BorrowState borrowState) {
        return borrowState.name().toLowerCase();
    }
}
