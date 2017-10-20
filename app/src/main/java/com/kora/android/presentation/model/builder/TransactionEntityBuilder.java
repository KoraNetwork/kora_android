package com.kora.android.presentation.model.builder;

import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.model.UserEntity;

public class TransactionEntityBuilder {
    private String mId;
    private double mFromAmount;
    private double mToAmount;
    private String mTransactionHash;
    private UserEntity mSender;
    private UserEntity mReceiver;
    private TransactionType mTransactionType;

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

    public TransactionEntityBuilder setTransactionHash(String transactionHash) {
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

    public TransactionEntity createTransactionEntity() {
        return new TransactionEntity(mId, mFromAmount, mToAmount, mTransactionHash, mSender, mReceiver, mTransactionType);
    }
}