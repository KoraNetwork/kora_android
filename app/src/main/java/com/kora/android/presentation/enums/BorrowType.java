package com.kora.android.presentation.enums;

public enum BorrowType {

    REQUEST("requests"), LOANS("loans"), IN_PROGRESS("inProgress");

    private String text;

    BorrowType(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}