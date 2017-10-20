package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.TransactionType;

public class TransactionEntity implements Parcelable {

    private String id;
    private double fromAmount;
    private double toAmount;
    private String transactionHash;
    private UserEntity sender;
    private UserEntity receiver;
    private TransactionType transactionType;

    public TransactionEntity(String id, double fromAmount, double toAmount, String transactionHash, UserEntity sender, UserEntity receiver, TransactionType transactionType) {
        this.id = id;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.transactionHash = transactionHash;
        this.sender = sender;
        this.receiver = receiver;
        this.transactionType = transactionType;
    }

    protected TransactionEntity(Parcel in) {
        id = in.readString();
        fromAmount = in.readDouble();
        toAmount = in.readDouble();
        transactionHash = in.readString();
        sender = (UserEntity) in.readValue(UserEntity.class.getClassLoader());
        receiver = (UserEntity) in.readValue(UserEntity.class.getClassLoader());
        transactionType = TransactionType.valueOf(in.readString());
    }

    public static final Creator<TransactionEntity> CREATOR = new Creator<TransactionEntity>() {
        @Override
        public TransactionEntity createFromParcel(Parcel in) {
            return new TransactionEntity(in);
        }

        @Override
        public TransactionEntity[] newArray(int size) {
            return new TransactionEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(double toAmount) {
        this.toAmount = toAmount;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(fromAmount);
        dest.writeDouble(toAmount);
        dest.writeString(transactionHash);
        dest.writeValue(sender);
        dest.writeValue(receiver);
        dest.writeString(transactionType.name());
    }

}
