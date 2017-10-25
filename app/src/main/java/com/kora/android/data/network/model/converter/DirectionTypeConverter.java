package com.kora.android.data.network.model.converter;

import com.bluelinelabs.logansquare.typeconverters.StringBasedTypeConverter;
import com.kora.android.presentation.enums.Direction;

public class DirectionTypeConverter extends StringBasedTypeConverter<Direction> {

    @Override
    public Direction getFromString(String string) {
        return Enum.valueOf(Direction.class, string.toUpperCase());
    }

    @Override
    public String convertToString(Direction object) {
        return object.name().toLowerCase();
    }


}
