package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.RequestState;
import com.kora.android.presentation.enums.Direction;

import java.util.Date;

public class RequestEntity implements Parcelable {

    private UserEntity mFrom;
    private UserEntity mTo;
    private double mFromAmount;
    private double mToAmount;
    private String mAdditionalNote;
    private RequestState mState;
    private Date mCreatedAt = new Date();
    private Direction mDirection;

    public RequestEntity(UserEntity mFrom,
                         UserEntity mTo,
                         double mFromAmount,
                         double mToAmount,
                         String mAdditionalNote,
                         RequestState mState,
                         Date mCreatedAt,
                         Direction direction) {
        this.mFrom = mFrom;
        this.mTo = mTo;
        this.mFromAmount = mFromAmount;
        this.mToAmount = mToAmount;
        this.mAdditionalNote = mAdditionalNote;
        this.mState = mState;
        this.mCreatedAt = mCreatedAt;
        this.mDirection = direction;
    }

    public UserEntity getFrom() {
        return mFrom;
    }

    public void setFrom(UserEntity mFrom) {
        this.mFrom = mFrom;
    }

    public UserEntity getTo() {
        return mTo;
    }

    public void setTo(UserEntity mTo) {
        this.mTo = mTo;
    }

    public double getFromAmount() {
        return mFromAmount;
    }

    public void setFromAmount(double mFromAmount) {
        this.mFromAmount = mFromAmount;
    }

    public double getToAmount() {
        return mToAmount;
    }

    public void setToAmount(double mToAmount) {
        this.mToAmount = mToAmount;
    }

    public String getAdditionalNote() {
        return mAdditionalNote;
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

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mFrom, flags);
        dest.writeParcelable(this.mTo, flags);
        dest.writeDouble(this.mFromAmount);
        dest.writeDouble(this.mToAmount);
        dest.writeString(this.mAdditionalNote);
        dest.writeInt(this.mState == null ? -1 : this.mState.ordinal());
        dest.writeLong(this.mCreatedAt != null ? this.mCreatedAt.getTime() : -1);
        dest.writeString(mDirection == null ? "" : mDirection.name());
    }

    protected RequestEntity(Parcel in) {
        this.mFrom = in.readParcelable(UserEntity.class.getClassLoader());
        this.mTo = in.readParcelable(UserEntity.class.getClassLoader());
        this.mFromAmount = in.readDouble();
        this.mToAmount = in.readDouble();
        this.mAdditionalNote = in.readString();
        int tmpMState = in.readInt();
        this.mState = tmpMState == -1 ? null : RequestState.values()[tmpMState];
        long tmpMCreatedAt = in.readLong();
        this.mCreatedAt = tmpMCreatedAt == -1 ? null : new Date(tmpMCreatedAt);
        String d = in.readString();
        mDirection = d.equals("") ? null : Direction.valueOf(d);
    }

    public static final Creator<RequestEntity> CREATOR = new Creator<RequestEntity>() {
        @Override
        public RequestEntity createFromParcel(Parcel source) {
            return new RequestEntity(source);
        }

        @Override
        public RequestEntity[] newArray(int size) {
            return new RequestEntity[size];
        }
    };
}
