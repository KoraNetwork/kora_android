package com.kora.android.presentation.model.builder;

import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.RequestState;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;

public class RequestEntityBuilder {

    private UserEntity mFrom;
    private UserEntity mTo;
    private double mFromAmount;
    private double mToAmount;
    private String mAdditionalNote;
    private RequestState mState;
    private Date mCreatedAt;
    private Direction mDirection;

    public RequestEntityBuilder setFrom(final UserEntity from) {
        mFrom = from;
        return this;
    }

    public RequestEntityBuilder setTo(final UserEntity to) {
        mTo = to;
        return this;
    }

    public RequestEntityBuilder setFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public RequestEntityBuilder setToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public RequestEntityBuilder setAdditionalNote(final String additionalNote) {
        mAdditionalNote = additionalNote;
        return this;
    }

    public RequestEntityBuilder setState(final RequestState state) {
        mState = state;
        return this;
    }

    public RequestEntityBuilder setCreatedAt (final Date createdAt) {
        mCreatedAt = createdAt;
        return this;
    }

    public RequestEntityBuilder setDirection (final Direction direction) {
        mDirection = direction;
        return this;
    }

    public RequestEntity createRequestEntity() {
        return new RequestEntity(mFrom, mTo, mFromAmount, mToAmount, mAdditionalNote, mState, mCreatedAt, mDirection);
    }
}
