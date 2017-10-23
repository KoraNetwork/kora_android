package com.kora.android.presentation.dto;

import com.kora.android.presentation.enums.TransactionDirection;
import com.kora.android.presentation.enums.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class TransactionFilterDto {

    private List<TransactionDirection> mTransactionDirections;
    private List<TransactionType> mTransactionTypes;

    public TransactionFilterDto() {
    }

    public TransactionFilterDto(List<TransactionDirection> transactionDirections, List<TransactionType> transactionTypes) {
        mTransactionDirections = transactionDirections;
        mTransactionTypes = transactionTypes;
    }

    public List<TransactionDirection> getTransactionDirections() {
        return mTransactionDirections;
    }

    public void setTransactionDirections(List<TransactionDirection> transactionDirections) {
        mTransactionDirections = transactionDirections;
    }

    public List<TransactionType> getTransactionTypes() {
        return mTransactionTypes;
    }

    public void setTransactionTypes(List<TransactionType> transactionTypes) {
        mTransactionTypes = transactionTypes;
    }

    public List<String> getTransactionDirectionsAsStrings() {
        List strings = new ArrayList();
        for (TransactionDirection direction : mTransactionDirections) {
            strings.add(direction.name().toLowerCase());
        }
        return strings;
    }

    public List<String> getTransactionTypesAsStrings() {
        List strings = new ArrayList();
        for (TransactionType type : mTransactionTypes) {
            strings.add(type.name().toLowerCase());
        }
        return strings;
    }
}
