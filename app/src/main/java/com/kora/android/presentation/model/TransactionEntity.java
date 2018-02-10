package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.TransactionState;
import com.kora.android.presentation.enums.TransactionType;

import java.util.Date;
import java.util.List;

public class TransactionEntity implements Parcelable {

    private String id;
    private double fromAmount;
    private double toAmount;
    private List<String> transactionHash;
    private UserEntity sender;
    private UserEntity receiver;
    private TransactionType transactionType;
    private Direction mDirection;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
    private TransactionState transactionState;

    public TransactionEntity(String id,
                             double fromAmount,
                             double toAmount,
                             List<String> transactionHash,
                             UserEntity sender,
                             UserEntity receiver,
                             TransactionType transactionType,
                             Direction direction,
                             Date createdAt,
                             Date updatedAt,
                             TransactionState transactionState) {
        this.id = id;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.transactionHash = transactionHash;
        this.sender = sender;
        this.receiver = receiver;
        this.transactionType = transactionType;
        this.mDirection = direction;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.transactionState = transactionState;
    }

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

    public List<String> getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(List<String> transactionHash) {
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

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public TransactionState getTransactionState() {
        return transactionState;
    }

    public void setTransactionState(TransactionState transactionState) {
        transactionState = transactionState;
    }

    @Override
    public String toString() {
        return "TransactionEntity{" + "\n" +
                "id=" + id + "\n" +
                "fromAmount=" + fromAmount + "\n" +
                "toAmount=" + toAmount + "\n" +
                "transactionHash=" + transactionHash + "\n" +
                "sender=" + sender + "\n" +
                "receiver=" + receiver + "\n" +
                "transactionType=" + transactionType + "\n" +
                "mDirection=" + mDirection + "\n" +
                "createdAt=" + createdAt + "\n" +
                "updatedAt=" + updatedAt + "\n" +
                "mTransactionState=" + transactionState + "\n" +
                "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeDouble(this.fromAmount);
        dest.writeDouble(this.toAmount);
        dest.writeStringList(this.transactionHash);
        dest.writeParcelable(this.sender, flags);
        dest.writeParcelable(this.receiver, flags);
        dest.writeInt(this.transactionType == null ? -1 : this.transactionType.ordinal());
        dest.writeInt(this.mDirection == null ? -1 : this.mDirection.ordinal());
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeInt(this.transactionState == null ? -1 : this.transactionState.ordinal());
    }

    protected TransactionEntity(Parcel in) {
        this.id = in.readString();
        this.fromAmount = in.readDouble();
        this.toAmount = in.readDouble();
        this.transactionHash = in.createStringArrayList();
        this.sender = in.readParcelable(UserEntity.class.getClassLoader());
        this.receiver = in.readParcelable(UserEntity.class.getClassLoader());
        int tmpTransactionType = in.readInt();
        this.transactionType = tmpTransactionType == -1 ? null : TransactionType.values()[tmpTransactionType];
        int tmpMDirection = in.readInt();
        this.mDirection = tmpMDirection == -1 ? null : Direction.values()[tmpMDirection];
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        int tmpMTransactionState = in.readInt();
        this.transactionState = tmpMTransactionState == -1 ? null : TransactionState.values()[tmpMTransactionState];
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
