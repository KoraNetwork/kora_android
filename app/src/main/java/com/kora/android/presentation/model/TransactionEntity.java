package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.TransactionType;

import java.util.Date;
import java.util.List;

public class TransactionEntity implements Parcelable {

    private String mId;
    private double mFromAmount;
    private double mToAmount;
    private List<String> mTransactionHash;
    private UserEntity mSender;
    private UserEntity mReceiver;
    private TransactionType mTransactionType;
    private Date mCreatedAt;
    private Date mUpdatedAt;

    public TransactionEntity(String id,
                             double fromAmount,
                             double toAmount,
                             List<String> transactionHash,
                             UserEntity sender,
                             UserEntity receiver,
                             TransactionType transactionType,
                             Date createdAt,
                             Date updatedAt) {
        this.mId = id;
        this.mFromAmount = fromAmount;
        this.mToAmount = toAmount;
        this.mTransactionHash = transactionHash;
        this.mSender = sender;
        this.mReceiver = receiver;
        this.mTransactionType = transactionType;
        this.mCreatedAt = createdAt;
        this.mUpdatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "TransactionEntity{" + "\n" +
                "mId=" + mId + "\n" +
                "mTransactionType=" + mTransactionType + "\n" +
                "mFromAmount=" + mFromAmount + "\n" +
                "mToAmount=" + mToAmount + "\n" +
                "mTransactionHash=" + mTransactionHash + "\n" +
                "mSender=" + mSender + "\n" +
                "mReceiver=" + mReceiver + "\n" +
                "mCreatedAt=" + mCreatedAt + "\n" +
                "mUpdatedAt=" + mUpdatedAt + "\n" +
                "}";
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public double getFromAmount() {
        return mFromAmount;
    }

    public void setFromAmount(double fromAmount) {
        this.mFromAmount = fromAmount;
    }

    public double getToAmount() {
        return mToAmount;
    }

    public void setToAmount(double toAmount) {
        this.mToAmount = toAmount;
    }

    public List<String> getTransactionHash() {
        return mTransactionHash;
    }

    public void setTransactionHash(List<String> transactionHash) {
        this.mTransactionHash = transactionHash;
    }

    public UserEntity getSender() {
        return mSender;
    }

    public void setSender(UserEntity sender) {
        this.mSender = sender;
    }

    public UserEntity getReceiver() {
        return mReceiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.mReceiver = receiver;
    }

    public TransactionType getTransactionType() {
        return mTransactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.mTransactionType = transactionType;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeDouble(this.mFromAmount);
        dest.writeDouble(this.mToAmount);
        dest.writeStringList(this.mTransactionHash);
        dest.writeParcelable(this.mSender, flags);
        dest.writeParcelable(this.mReceiver, flags);
        dest.writeInt(this.mTransactionType == null ? -1 : this.mTransactionType.ordinal());
        dest.writeLong(this.mCreatedAt != null ? this.mCreatedAt.getTime() : -1);
        dest.writeLong(this.mUpdatedAt != null ? this.mUpdatedAt.getTime() : -1);
    }

    protected TransactionEntity(Parcel in) {
        this.mId = in.readString();
        this.mFromAmount = in.readDouble();
        this.mToAmount = in.readDouble();
        this.mTransactionHash = in.createStringArrayList();
        this.mSender = in.readParcelable(UserEntity.class.getClassLoader());
        this.mReceiver = in.readParcelable(UserEntity.class.getClassLoader());
        int tmpMTransactionType = in.readInt();
        this.mTransactionType = tmpMTransactionType == -1 ? null : TransactionType.values()[tmpMTransactionType];
        long tmpMCreatedAt = in.readLong();
        this.mCreatedAt = tmpMCreatedAt == -1 ? null : new Date(tmpMCreatedAt);
        long tmpMUpdatedAt = in.readLong();
        this.mUpdatedAt = tmpMUpdatedAt == -1 ? null : new Date(tmpMUpdatedAt);
    }

    public static final Creator<TransactionEntity> CREATOR = new Creator<TransactionEntity>() {
        @Override
        public TransactionEntity createFromParcel(Parcel source) {
            return new TransactionEntity(source);
        }

        @Override
        public TransactionEntity[] newArray(int size) {
            return new TransactionEntity[size];
        }
    };
}
