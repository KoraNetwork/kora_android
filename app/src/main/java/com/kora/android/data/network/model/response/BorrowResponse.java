package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.kora.android.data.network.model.converter.DateTypeCustomConverter;
import com.kora.android.data.network.model.converter.DirectionTypeConverter;
import com.kora.android.data.network.model.converter.RequestStateConverter;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.RequestState;

import java.util.Date;

@JsonObject
public class BorrowResponse {

    @JsonField(name = "id") private String mId;
    @JsonField(name = "direction", typeConverter = DirectionTypeConverter.class) private Direction mDirection;
    @JsonField(name = "state", typeConverter = RequestStateConverter.class) private RequestState mState;
    @JsonField(name = "fromAmount") private double mFromAmount;
    @JsonField(name = "toAmount") private double mToAmount;
    @JsonField(name = "rate") private int mRate;
    @JsonField(name = "additionalNote") private String mAdditionalNote;
    @JsonField(name = "startDate", typeConverter = DateTypeCustomConverter.class) private Date mStartDate;
    @JsonField(name = "maturityDate", typeConverter = DateTypeCustomConverter.class) private Date mMaturityDate;
    @JsonField(name = "createdAt", typeConverter = DateTypeCustomConverter.class) private Date mCreatedAt;
    @JsonField(name = "from") private UserResponse mSender;
    @JsonField(name = "to") private UserResponse mReceiver;
    @JsonField(name = "guarantor1") private UserResponse mGuarantor1 = new UserResponse();
    @JsonField(name = "guarantor2") private UserResponse mGuarantor2 = new UserResponse();
    @JsonField(name = "guarantor3") private UserResponse mGuarantor3 = new UserResponse();

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

    public RequestState getState() {
        return mState;
    }

    public void setState(RequestState state) {
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

    public UserResponse getGuarantor1() {
        return mGuarantor1;
    }

    public void setGuarantor1(UserResponse guarantor1) {
        mGuarantor1 = guarantor1;
    }

    public UserResponse getGuarantor2() {
        return mGuarantor2;
    }

    public void setGuarantor2(UserResponse guarantor2) {
        mGuarantor2 = guarantor2;
    }

    public UserResponse getGuarantor3() {
        return mGuarantor3;
    }

    public void setGuarantor3(UserResponse guarantor3) {
        mGuarantor3 = guarantor3;
    }
}
