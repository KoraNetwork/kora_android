package com.kora.android.presentation.ui.main.fragments.deposit_withdraw.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.DepositWithdrawState;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.ui.base.adapter.filter.FilterModel;

public class DepositWithdrawFilterModel extends FilterModel implements Parcelable {

    private Direction mDirection;
    private DepositWithdrawState mState;

    public DepositWithdrawFilterModel() {

    }

    protected DepositWithdrawFilterModel(final Parcel in) {
        String s = in.readString();
        if (!s.equals(""))
            mDirection = Direction.valueOf(s);
        s = in.readString();
        if (!s.equals(""))
            mState = DepositWithdrawState.valueOf(s);
    }

    public static final Creator<DepositWithdrawFilterModel> CREATOR = new Creator<DepositWithdrawFilterModel>() {
        @Override
        public DepositWithdrawFilterModel createFromParcel(Parcel in) {
            return new DepositWithdrawFilterModel(in);
        }

        @Override
        public DepositWithdrawFilterModel[] newArray(int size) {
            return new DepositWithdrawFilterModel[size];
        }
    };

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction mDirection) {
        this.mDirection = mDirection;
    }

    public DepositWithdrawState getState() {
        return mState;
    }

    public void setState(DepositWithdrawState mState) {
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
