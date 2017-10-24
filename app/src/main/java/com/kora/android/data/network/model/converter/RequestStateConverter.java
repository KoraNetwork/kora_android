package com.kora.android.data.network.model.converter;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.RequestState;

public class RequestStateConverter extends StringBasedTypeConverter<RequestState> {

    @Override
    public RequestState getFromString(String string) {
        return Enum.valueOf(RequestState.class, string.toUpperCase());
    }

    @Override
    public String convertToString(RequestState object) {
        return object.name().toLowerCase();
    }
}
