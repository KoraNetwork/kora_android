package com.kora.android.presentation.enums;

public enum  TransactionType {

    SEND("send"),
    REQUEST("request"),
    BORROWFUND("borrowFund"),
    BORROWPAYBACK("borrowPayBack"),
    DEPOSIT("deposit"),
    WITHDRAW("withdraw");

    private String mText;

    TransactionType(String text) {
        this.mText = text;
    }

    public String getText() {
        return mText;
    }
}
