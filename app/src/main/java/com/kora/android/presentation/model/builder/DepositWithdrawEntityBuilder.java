package com.kora.android.presentation.model.builder;

import com.kora.android.presentation.enums.DepositWithdrawState;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;

public class DepositWithdrawEntityBuilder {

    private String mId;
    private UserEntity mFrom;
    private UserEntity mTo;
    private double mFromAmount;
    private double mToAmount;
    private int mInterestRate;
    private DepositWithdrawState mState;
    private Direction mDirection;
    private Date mCreatedAt;

    public DepositWithdrawEntityBuilder setId(final String id) {
        mId = id;
        return this;
    }

    public DepositWithdrawEntityBuilder setFrom(final UserEntity from) {
        mFrom = from;
        return this;
    }

    public DepositWithdrawEntityBuilder setTo(final UserEntity to) {
        mTo = to;
        return this;
    }

    public DepositWithdrawEntityBuilder setFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public DepositWithdrawEntityBuilder setToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public DepositWithdrawEntityBuilder setInterestRate(final int interestRate) {
        mInterestRate = interestRate;
        return this;
    }

    public DepositWithdrawEntityBuilder setState(final DepositWithdrawState state) {
        mState = state;
        return this;
    }

    public DepositWithdrawEntityBuilder setCreatedAt (final Date createdAt) {
        mCreatedAt = createdAt;
        return this;
    }

    public DepositWithdrawEntityBuilder setDirection (final Direction direction) {
        mDirection = direction;
        return this;
    }

    public DepositWithdrawEntity createDepositEntity() {
        return new DepositWithdrawEntity(
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
