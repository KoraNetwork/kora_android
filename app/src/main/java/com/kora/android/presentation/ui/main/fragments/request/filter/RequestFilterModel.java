package com.kora.android.presentation.ui.main.fragments.request.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.RequestState;
import com.kora.android.presentation.ui.base.adapter.filter.FilterModel;

public class RequestFilterModel extends FilterModel implements Parcelable {

    private Direction mDirection;
    private RequestState mRequestStyte;

    public RequestFilterModel() {
    }

    protected RequestFilterModel(Parcel in) {
        String s = in.readString();
        if (!s.equals(""))
            mDirection = Direction.valueOf(s);
        s = in.readString();
        if (!s.equals(""))
            mRequestStyte = RequestState.valueOf(s);
    }

    public static final Creator<RequestFilterModel> CREATOR = new Creator<RequestFilterModel>() {
        @Override
        public RequestFilterModel createFromParcel(Parcel in) {
            return new RequestFilterModel(in);
        }

        @Override
        public RequestFilterModel[] newArray(int size) {
            return new RequestFilterModel[size];
        }
    };

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    public RequestState getRequestStyte() {
        return mRequestStyte;
    }

    public void setRequestStyte(RequestState requestStyte) {
        mRequestStyte = requestStyte;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDirection == null ? "" : mDirection.name());
        dest.writeString(mRequestStyte == null ? "" : mRequestStyte.name());
    }

    public String getDirectionAsStrings() {
        return mDirection == null ? "" : mDirection.name().toLowerCase();
    }

    public String getStateAsStrings() {
        return mRequestStyte == null ? "" : mRequestStyte.name().toLowerCase();
    }
}
