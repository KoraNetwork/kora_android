package com.kora.android.presentation.model.builder;

import com.kora.android.presentation.enums.DepositState;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.RequestState;
import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;

public class DepositEntityBuilder {

    private String mId;
    private UserEntity mFrom;
    private UserEntity mTo;
    private double mFromAmount;
    private double mToAmount;
    private int mInterestRate;
    private DepositState mState;
    private Direction mDirection;
    private Date mCreatedAt;

    public DepositEntityBuilder setId(final String id) {
        mId = id;
        return this;
    }

    public DepositEntityBuilder setFrom(final UserEntity from) {
        mFrom = from;
        return this;
    }

    public DepositEntityBuilder setTo(final UserEntity to) {
        mTo = to;
        return this;
    }

    public DepositEntityBuilder setFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public DepositEntityBuilder setToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public DepositEntityBuilder setInterestRate(final int interestRate) {
        mInterestRate = interestRate;
        return this;
    }

    public DepositEntityBuilder setState(final DepositState state) {
        mState = state;
        return this;
    }

    public DepositEntityBuilder setCreatedAt (final Date createdAt) {
        mCreatedAt = createdAt;
        return this;
    }

    public DepositEntityBuilder setDirection (final Direction direction) {
        mDirection = direction;
        return this;
    }

    public DepositEntity createDepositEntity() {
        return new DepositEntity(
                mId,
                mFrom,
                mTo,
                mFromAmount,
                mToAmount,
                mInterestRate,
                mState,
                mDirection,
                mCreatedAt
        );
    }
}
