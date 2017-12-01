package com.kora.android.presentation.ui.main.fragments.deposit.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.DepositState;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.ui.base.adapter.filter.FilterModel;

public class DepositFilterModel extends FilterModel implements Parcelable {

    private Direction mDirection;
    private DepositState mState;

    public DepositFilterModel() {

    }

    protected DepositFilterModel(final Parcel in) {
        String s = in.readString();
        if (!s.equals(""))
            mDirection = Direction.valueOf(s);
        s = in.readString();
        if (!s.equals(""))
            mState = DepositState.valueOf(s);
    }

    public static final Creator<DepositFilterModel> CREATOR = new Creator<DepositFilterModel>() {
        @Override
        public DepositFilterModel createFromParcel(Parcel in) {
            return new DepositFilterModel(in);
        }

        @Override
        public DepositFilterModel[] newArray(int size) {
            return new DepositFilterModel[size];
        }
    };

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction mDirection) {
        this.mDirection = mDirection;
    }

    public DepositState getState() {
        return mState;
    }

    public void setState(DepositState mState) {
        this.mState = mState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mDirection == null ? "" : mDirection.name());
        dest.writeString(mState == null ? "" : mState.name());
    }

    public String getDirectionAsString() {
        return mDirection == null ? "" : mDirection.name().toLowerCase();
    }

    public String getStateAsString() {
        return mState == null ? "" : mState.name().toLowerCase();
    }
}
