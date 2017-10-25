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
public class RequestResponse {

    @JsonField(name = "from")
    private UserResponse mFrom;
    @JsonField(name = "to")
    private UserResponse mTo;
    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "additionalNote")
    private String mAdditionalNote;
    @JsonField(name = "state", typeConverter = RequestStateConverter.class)
    private RequestState mState;
    @JsonField(name = "createdAt", typeConverter = DateTypeCustomConverter.class)
    private Date mCreatedAt;
    @JsonField(name = "updatedAt", typeConverter = DateTypeCustomConverter.class)
    private Date mUpdatedAt;
    @JsonField(name = "direction", typeConverter = DirectionTypeConverter.class)
    private Direction mDirection;

    public UserResponse getFrom() {
        return mFrom;
    }

    public void setFrom(UserResponse mFrom) {
        this.mFrom = mFrom;
    }

    public UserResponse getTo() {
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

    public void setTo(UserResponse mTo) {
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

    public RequestState getState() {
        return mState;
    }

    public void setState(RequestState mState) {
        this.mState = mState;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date mUpdatedAt) {
        this.mUpdatedAt = mUpdatedAt;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }
}
