package com.kora.android.presentation.model.builder;

import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.TransactionState;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.Date;
import java.util.List;

public class TransactionEntityBuilder {
    private String mId;
    private double mFromAmount;
    private double mToAmount;
    private List<String> mTransactionHash;
    private UserEntity mSender;
    private UserEntity mReceiver;
    private TransactionType mTransactionType;
    private Direction mDirection;
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private TransactionState mTransactionState;

    public TransactionEntityBuilder setId(String id) {
        mId = id;
        return this;
    }

    public TransactionEntityBuilder setFromAmount(double fromAmount) {
        mFromAmount = fromAmount;
        return this;
    }

    public TransactionEntityBuilder setToAmount(double toAmount) {
        mToAmount = toAmount;
        return this;
    }

    public TransactionEntityBuilder setTransactionHash(List<String> transactionHash) {
        mTransactionHash = transactionHash;
        return this;
    }

    public TransactionEntityBuilder setSender(UserEntity sender) {
        mSender = sender;
        return this;
    }

    public TransactionEntityBuilder setReceiver(UserEntity receiver) {
        mReceiver = receiver;
        return this;
    }

    public TransactionEntityBuilder setTransactionType(TransactionType transactionType) {
        mTransactionType = transactionType;
        return this;
    }

    public TransactionEntityBuilder setDirection(Direction direction) {
        mDirection = direction;
        return this;
    }

    public TransactionEntityBuilder setCreatedAt (Date createdAt) {
        mCreatedAt = createdAt;
        return this;
    }

    public TransactionEntityBuilder setUpdatedAt (Date updatedAt) {
        mUpdatedAt = updatedAt;
        return this;
    }

    public TransactionEntityBuilder setTransactionState (TransactionState transactionState) {
        mTransactionState = transactionState;
        return this;
    }

    public TransactionEntity createTransactionEntity() {
        return new TransactionEntity(
                mId,
                mFromAmount,
                mToAmount,
                mTransactionHash,
                mSender,
                mReceiver,
                mTransactionType,
                mDirection,
                mCreatedAt,
                mUpdatedAt,
                mTransactionState);
    }
}