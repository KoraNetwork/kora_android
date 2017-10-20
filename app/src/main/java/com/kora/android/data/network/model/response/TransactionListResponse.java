package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class TransactionListResponse {

    @JsonField(name = "data")
    private List<TransactionResponse> mTransactions;

    public List<TransactionResponse> getTransactions() {
        return mTransactions;
    }

    public void setTransactions(List<TransactionResponse> transactions) {
        mTransactions = transactions;
    }
}
