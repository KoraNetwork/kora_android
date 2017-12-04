package com.kora.android.data.network.model.converter;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.DepositWithdrawState;

public class DepositStateConverter extends StringBasedTypeConverter<DepositWithdrawState> {
    @Override
    public DepositWithdrawState getFromString(String string) {
        return Enum.valueOf(DepositWithdrawState.class, string.toUpperCase());
    }

    @Override
    public String convertToString(DepositWithdrawState depositWithdrawState) {
        return depositWithdrawState.name().toLowerCase();
    }
}
