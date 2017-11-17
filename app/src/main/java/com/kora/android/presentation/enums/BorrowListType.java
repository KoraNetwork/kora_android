package com.kora.android.presentation.enums;

public enum BorrowListType {

    REQUESTS("requests"), LOANS("loans"), IN_PROGRESS("inProgress");

    private String text;

    BorrowListType(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}