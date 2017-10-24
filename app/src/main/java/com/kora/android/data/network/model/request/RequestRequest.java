package com.kora.android.data.network.model.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class RequestRequest {

    @JsonField(name = "to")
    private String mTo;
    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "additionalNote")
    private String mAdditionalNote;

    public RequestRequest addTo(final String to) {
        mTo = to;
        return this;
    }

    public RequestRequest addFromAmount(final double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public RequestRequest addToAmount(final double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public RequestRequest addAdditionalNote(final String additionalNote) {
        mAdditionalNote = additionalNote;
        return this;
    }

    public String getTo() {
        return mTo;
    }

    public double getFromAmount() {
        return mFromAmount;
    }

    public double getToAmount() {
        return mToAmount;
    }

    public String getAdditionalNote() {
        return mAdditionalNote;
    }

    public void setTo(String mTo) {
        this.mTo = mTo;
    }

    public void setFromAmount(double mFromAmount) {
        this.mFromAmount = mFromAmount;
    }

    public void setToAmount(double mToAmount) {
        this.mToAmount = mToAmount;
    }

    public void setAdditionalNote(String mAdditionalNote) {
        this.mAdditionalNote = mAdditionalNote;
    }
}
