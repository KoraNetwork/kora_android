package com.kora.android.data.network.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ErrorResponseTwilio implements Parcelable {

    @SerializedName("status")
    private int mStatus;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("code")
    private int mCode;
    @SerializedName("moreInfo")
    private String mMoreinfo;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int mCode) {
        this.mCode = mCode;
    }

    public String getMoreinfo() {
        return mMoreinfo;
    }

    public void setMoreinfo(String mMoreinfo) {
        this.mMoreinfo = mMoreinfo;
    }

    @Override
    public String toString() {
        return "ErrorResponseTwilio{" + "\n" +
                "mStatus=" + mStatus + "\n" +
                "mMessage=" + mMessage + "\n" +
                "mCode=" + mCode + "\n" +
                "mMoreinfo=" + mMoreinfo + "\n" +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mStatus);
        dest.writeString(this.mMessage);
        dest.writeInt(this.mCode);
        dest.writeString(this.mMoreinfo);
    }

    public ErrorResponseTwilio() {
    }

    protected ErrorResponseTwilio(Parcel in) {
        this.mStatus = in.readInt();
        this.mMessage = in.readString();
        this.mCode = in.readInt();
        this.mMoreinfo = in.readString();
    }

    public static final Creator<ErrorResponseTwilio> CREATOR = new Creator<ErrorResponseTwilio>() {
        @Override
        public ErrorResponseTwilio createFromParcel(Parcel source) {
            return new ErrorResponseTwilio(source);
        }

        @Override
        public ErrorResponseTwilio[] newArray(int size) {
            return new ErrorResponseTwilio[size];
        }
    };
}
