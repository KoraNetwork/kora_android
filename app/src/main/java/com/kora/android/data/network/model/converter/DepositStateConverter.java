package com.kora.android.data.network.model.converter;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.DepositState;

public class DepositStateConverter extends StringBasedTypeConverter<DepositState> {
    @Override
    public DepositState getFromString(String string) {
        return Enum.valueOf(DepositState.class, string.toUpperCase());
    }

    @Override
    public String convertToString(DepositState depositState) {
        return depositState.name().toLowerCase();
    }
}
