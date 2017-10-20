package com.kora.android.presentation.dto;

import com.kora.android.presentation.enums.TransactionDirection;
import com.kora.android.presentation.enums.TransactionType;

public class TransactionFilterDto {

    private TransactionDirection mTransactionDirection;
    private TransactionType[] mTransactionTypes;

    public TransactionDirection getTransactionDirection() {
        return mTransactionDirection;
    }

    public void setTransactionDirection(TransactionDirection transactionDirection) {
        mTransactionDirection = transactionDirection;
    }

    public TransactionType[] getTransactionTypes() {
        return mTransactionTypes;
    }

    public void setTransactionTypes(TransactionType[] transactionTypes) {
        mTransactionTypes = transactionTypes;
    }
}
