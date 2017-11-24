package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.kora.android.data.network.model.converter.BorrowStateConverter;
import com.kora.android.data.network.model.converter.BorrowTypeConverter;
import com.kora.android.data.network.model.converter.DateTypeCustomConverter;
import com.kora.android.data.network.model.converter.DirectionTypeConverter;
import com.kora.android.presentation.enums.BorrowState;
import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.enums.Direction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonObject
public class BorrowResponse {

    @JsonField(name = "id") private String mId;
    @JsonField(name = "direction", typeConverter = DirectionTypeConverter.class) private Direction mDirection;
    @JsonField(name = "state", typeConverter = BorrowStateConverter.class) private BorrowState mState;
    @JsonField(name = "fromAmount") private double mFromAmount;
    @JsonField(name = "toAmount") private double mToAmount;
    @JsonField(name = "interestRate") private int mRate;
    @JsonField(name = "additionalNote") private String mAdditionalNote;
    @JsonField(name = "startDate", typeConverter = DateTypeCustomConverter.class) private Date mStartDate;
    @JsonField(name = "maturityDate", typeConverter = DateTypeCustomConverter.class) private Date mMaturityDate;
    @JsonField(name = "createdAt", typeConverter = DateTypeCustomConverter.class) private Date mCreatedAt;
    @JsonField(name = "from") private UserResponse mSender;
    @JsonField(name = "to") private UserResponse mReceiver;
    @JsonField(name = "guarantors") private List<UserResponse> mGuarantors = new ArrayList<>();
    @JsonField(name = "loanId") private String mLoanId;
    @JsonField(name = "type", typeConverter = BorrowTypeConverter.class) private BorrowType mType;

    // borrower and lender all fields !!!
    @JsonField(name = "totalFromAmount") private double mTotalFromAmount;
    @JsonField(name = "totalToAmount") private double mTotalToAmount;
    @JsonField(name = "fromBalance") private double mFromBalance;
    @JsonField(name = "toBalance") private double mToBalance;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    public BorrowState getState() {
        return mState;
    }

    public void setState(BorrowState state) {
        mState = state;
    }

    public double getFromAmount() {
        return mFromAmount;
    }

    public void setFromAmount(double fromAmount) {
        mFromAmount = fromAmount;
    }

    public double getToAmount() {
        return mToAmount;
    }

    public void setToAmount(double toAmount) {
        mToAmount = toAmount;
    }

    public int getRate() {
        return mRate;
    }

    public void setRate(int rate) {
        mRate = rate;
    }

    public String getAdditionalNote() {
        return mAdditionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        mAdditionalNote = additionalNote;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getMaturityDate() {
        return mMaturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        mMaturityDate = maturityDate;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public UserResponse getSender() {
        return mSender;
    }

    public void setSender(UserResponse sender) {
        mSender = sender;
    }

    public UserResponse getReceiver() {
        return mReceiver;
    }

    public void setReceiver(UserResponse receiver) {
        mReceiver = receiver;
    }

    public List<UserResponse> getGuarantors() {
        return mGuarantors;
    }

    public void setGuarantors(List<UserResponse> guarantors) {
        mGuarantors = guarantors;
    }

    public String getLoanId() {
        return mLoanId;
    }

    public void setLoanId(String mLoanId) {
        this.mLoanId = mLoanId;
    }

    public BorrowType getType() {
        return mType;
    }

    public void setType(BorrowType mType) {
        this.mType = mType;
    }

    public double getTotalFromAmount() {
        return mTotalFromAmount;
    }

    public void setTotalFromAmount(double mTotalFromAmount) {
        this.mTotalFromAmount = mTotalFromAmount;
    }

    public double getTotalToAmount() {
        return mTotalToAmount;
    }

    public void setTotalToAmount(double mTotalToAmount) {
        this.mTotalToAmount = mTotalToAmount;
    }

    public double getFromBalance() {
        return mFromBalance;
    }

    public void setFromBalance(double mFromBalance) {
        this.mFromBalance = mFromBalance;
    }

    public double getToBalance() {
        return mToBalance;
    }

    public void setToBalance(double mToBalance) {
        this.mToBalance = mToBalance;
    }
}
