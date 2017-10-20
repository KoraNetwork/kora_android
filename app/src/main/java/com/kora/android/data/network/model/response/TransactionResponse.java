package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.kora.android.data.network.model.converter.TransactionTypeConverter;
import com.kora.android.presentation.enums.TransactionType;

@JsonObject
public class TransactionResponse {

    @JsonField(name = "id") private String id;
    @JsonField(name = "type", typeConverter = TransactionTypeConverter.class) private TransactionType mType;
    @JsonField(name = "fromAmount") private double mFromAmount;
    @JsonField(name = "toAmount") private double mToAmount;
    @JsonField(name = "transactionHash") private String mTransactionHash;
    @JsonField(name = "from") private UserResponse mSender;
    @JsonField(name = "to") private UserResponse mReceiver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getFromAmount() {
        return mFromAmount;
    }

    public void setFromAmount(double fromAmount) {
        mFromAmount = fromAmount;
    }

    public double getToAmount() {
        return mToAmount;
    }

    public void setToAmount(double toAmount) {
        mToAmount = toAmount;
    }

    public String getTransactionHash() {
        return mTransactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        mTransactionHash = transactionHash;
    }

    public UserResponse getSender() {
        return mSender;
    }

    public void setSender(UserResponse sender) {
        mSender = sender;
    }

    public UserResponse getReceiver() {
        return mReceiver;
    }

    public void setReceiver(UserResponse receiver) {
        mReceiver = receiver;
    }

    public TransactionType getType() {
        return mType;
    }

    public void setType(TransactionType type) {
        mType = type;
    }
}
