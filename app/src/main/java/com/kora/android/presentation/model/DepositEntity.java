package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.DepositState;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.RequestState;

import java.util.Date;

public class DepositEntity implements Parcelable {

    private String mId;
    private UserEntity mFrom;
    private UserEntity mTo;
    private double mFromAmount;
    private double mToAmount;
    private int mInterestRate;
    private DepositState mState;
    private Direction mDirection;
    private Date mCreatedAt = new Date();

    public DepositEntity(String mId,
                         UserEntity mFrom,
                         UserEntity mTo,
                         double mFromAmount,
                         double mToAmount,
                         int interestRate,
                         DepositState mState,
                         Direction mDirection,
                         Date mCreatedAt) {
        this.mId = mId;
        this.mFrom = mFrom;
        this.mTo = mTo;
        this.mFromAmount = mFromAmount;
        this.mToAmount = mToAmount;
        this.mInterestRate = interestRate;
        this.mState = mState;
        this.mDirection = mDirection;
        this.mCreatedAt = mCreatedAt;
    }

    @Override
    public String toString() {
        return "DepositEntity{" + "\n" +
                "mId=" + mId + "\n" +
                "mFrom=" + mFrom + "\n" +
                "mTo=" + mTo + "\n" +
                "mFromAmount=" + mFromAmount + "\n" +
                "mToAmount=" + mToAmount + "\n" +
                "interestRate=" + mInterestRate + "\n" +
                "mState=" + mState + "\n" +
                "mDirection=" + mDirection + "\n" +
                "mCreatedAt=" + mCreatedAt + "\n" +
                "}";
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
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

    public int getInterestRate() {
        return mInterestRate;
    }

    public void setInterestRate(int mInterestRate) {
        this.mInterestRate = mInterestRate;
    }

    public DepositState getState() {
        return mState;
    }

    public void setState(DepositState mState) {
        this.mState = mState;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction mDirection) {
        this.mDirection = mDirection;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeParcelable(this.mFrom, flags);
        dest.writeParcelable(this.mTo, flags);
        dest.writeDouble(this.mFromAmount);
        dest.writeDouble(this.mToAmount);
        dest.writeInt(this.mInterestRate);
        dest.writeInt(this.mState == null ? -1 : this.mState.ordinal());
        dest.writeInt(this.mDirection == null ? -1 : this.mDirection.ordinal());
        dest.writeLong(this.mCreatedAt != null ? this.mCreatedAt.getTime() : -1);
    }

    protected DepositEntity(Parcel in) {
        this.mId = in.readString();
        this.mFrom = in.readParcelable(UserEntity.class.getClassLoader());
        this.mTo = in.readParcelable(UserEntity.class.getClassLoader());
        this.mFromAmount = in.readDouble();
        this.mToAmount = in.readDouble();
        this.mInterestRate = in.readInt();
        int tmpMState = in.readInt();
        this.mState = tmpMState == -1 ? null : DepositState.values()[tmpMState];
        int tmpMDirection = in.readInt();
        this.mDirection = tmpMDirection == -1 ? null : Direction.values()[tmpMDirection];
        long tmpMCreatedAt = in.readLong();
        this.mCreatedAt = tmpMCreatedAt == -1 ? null : new Date(tmpMCreatedAt);
    }

    public static final Creator<DepositEntity> CREATOR = new Creator<DepositEntity>() {
        @Override
        public DepositEntity createFromParcel(Parcel source) {
            return new DepositEntity(source);
        }

        @Override
        public DepositEntity[] newArray(int size) {
            return new DepositEntity[size];
        }
    };
}
