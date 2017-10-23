package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.typeconverters.DateTypeConverter;
import com.kora.android.data.network.model.converter.DateTypeCustomConverter;
import com.kora.android.data.network.model.converter.TransactionTypeConverter;
import com.kora.android.presentation.enums.TransactionType;

import java.util.Date;
import java.util.List;

@JsonObject
public class TransactionResponse {

    @JsonField(name = "id")
    private String mId;
    @JsonField(name = "type", typeConverter = TransactionTypeConverter.class)
    private TransactionType mType;
    @JsonField(name = "fromAmount")
    private double mFromAmount;
    @JsonField(name = "toAmount")
    private double mToAmount;
    @JsonField(name = "transactionHash")
    private List<String> mTransactionHash;
    @JsonField(name = "from")
    private UserResponse mSender;
    @JsonField(name = "to")
    private UserResponse mReceiver;

    @JsonField(name = "createdAt", typeConverter = DateTypeCustomConverter.class)
    private Date mCreatedAt;
    @JsonField(name = "updatedAt", typeConverter = DateTypeCustomConverter.class)
    private Date mUpdatedAt;

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
        mFromAmount = fromAmount;
    }

    public double getToAmount() {
        return mToAmount;
    }

    public void setToAmount(double toAmount) {
        mToAmount = toAmount;
    }

    public List<String> getTransactionHash() {
        return mTransactionHash;
    }

    public void setTransactionHash(List<String> transactionHash) {
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

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "mId=" + mId + "\n" +
                "mType=" + mType + "\n" +
                "mFromAmount=" + mFromAmount + "\n" +
                "mToAmount=" + mToAmount + "\n" +
                "mTransactionHash=" + mTransactionHash + "\n" +
                "mSender=" + mSender + "\n" +
                "mCreatedAt=" + mCreatedAt + "\n" +
                "mUpdatedAt=" + mUpdatedAt + "\n" +
                "}";
    }
}
