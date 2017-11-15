package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.kora.android.data.network.model.converter.DateTypeCustomConverter;

import java.util.Date;

@JsonObject
public class BorrowRequest {

    @JsonField(name = "fromAmount") private double mFromAmount;
    @JsonField(name = "toAmount") private double mToAmount;
    @JsonField(name = "interestRate") private int mRate;
    @JsonField(name = "additionalNote") private String mAdditionalNote;
    @JsonField(name = "startDate", typeConverter = DateTypeCustomConverter.class) private Date mStartDate;
    @JsonField(name = "maturityDate", typeConverter = DateTypeCustomConverter.class) private Date mMaturityDate;
    @JsonField(name = "to") private String mReceiver;
    @JsonField(name = "guarantor1") private String mGuarantor1;
    @JsonField(name = "guarantor2") private String mGuarantor2;
    @JsonField(name = "guarantor3") private String mGuarantor3;

    public BorrowRequest() {
    }

    public BorrowRequest(double fromAmount, double toAmount,
                         int rate, String additionalNote,
                         Date startDate, Date maturityDate, String receiver,
                         String guarantor1, String guarantor2, String guarantor3) {
        mFromAmount = fromAmount;
        mToAmount = toAmount;
        mRate = rate;
        mAdditionalNote = additionalNote;
        mStartDate = startDate;
        mMaturityDate = maturityDate;
        mReceiver = receiver;
        mGuarantor1 = guarantor1;
        mGuarantor2 = guarantor2;
        mGuarantor3 = guarantor3;
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

    public String getReceiver() {
        return mReceiver;
    }

    public void setReceiver(String receiver) {
        mReceiver = receiver;
    }

    public String getGuarantor1() {
        return mGuarantor1;
    }

    public void setGuarantor1(String guarantor1) {
        mGuarantor1 = guarantor1;
    }

    public String getGuarantor2() {
        return mGuarantor2;
    }

    public void setGuarantor2(String guarantor2) {
        mGuarantor2 = guarantor2;
    }

    public String getGuarantor3() {
        return mGuarantor3;
    }

    public void setGuarantor3(String guarantor3) {
        mGuarantor3 = guarantor3;
    }
}
