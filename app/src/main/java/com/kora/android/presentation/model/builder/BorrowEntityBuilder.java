package com.kora.android.presentation.model.builder;

import com.kora.android.presentation.enums.BorrowState;
import com.kora.android.presentation.enums.BorrowListType;
import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;
import java.util.List;

public class BorrowEntityBuilder {
    private String mId;
    private Direction mDirection;
    private BorrowState mState;
    private double mFromAmount;
    private double mToAmount;
    private int mRate;
    private String mAdditionalNote;
    private Date mStartDate;
    private Date mMaturityDate;
    private Date mCreatedAt;
    private UserEntity mSender;
    private UserEntity mReceiver;
    private List<UserEntity> mGuarantors;
    private String mLoanId;
    private BorrowType mType;

    public BorrowEntityBuilder setId(String id) {
        mId = id;
        return this;
    }

    public BorrowEntityBuilder setDirection(Direction direction) {
        mDirection = direction;
        return this;
    }

    public BorrowEntityBuilder setState(BorrowState state) {
        mState = state;
        return this;
    }

    public BorrowEntityBuilder setFromAmount(double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public BorrowEntityBuilder setToAmount(double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public BorrowEntityBuilder setRate(int rate) {
        mRate = rate;
        return this;
    }

    public BorrowEntityBuilder setAdditionalNote(String additionalNote) {
        mAdditionalNote = additionalNote;
        return this;
    }

    public BorrowEntityBuilder setStartDate(Date startDate) {
        mStartDate = startDate;
        return this;
    }

    public BorrowEntityBuilder setMaturityDate(Date maturityDate) {
        mMaturityDate = maturityDate;
        return this;
    }

    public BorrowEntityBuilder setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
        return this;
    }

    public BorrowEntityBuilder setSender(UserEntity sender) {
        mSender = sender;
        return this;
    }

    public BorrowEntityBuilder setReceiver(UserEntity receiver) {
        mReceiver = receiver;
        return this;
    }

    public BorrowEntityBuilder setGuarantors(List<UserEntity> guarantors) {
        mGuarantors = guarantors;
        return this;
    }

    public BorrowEntityBuilder setLoanId(String loanId) {
        mLoanId = loanId;
        return this;
    }

    public BorrowEntityBuilder setType(BorrowType type) {
        mType = type;
        return this;
    }

    public BorrowEntity createBorrowEntity() {
        return new BorrowEntity(mId, mDirection, mState, mFromAmount, mToAmount,
                mRate, mAdditionalNote, mStartDate, mMaturityDate, mCreatedAt,
                mSender, mReceiver, mGuarantors, mLoanId, mType);
    }
}