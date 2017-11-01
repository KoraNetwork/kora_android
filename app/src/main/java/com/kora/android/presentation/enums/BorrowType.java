package com.kora.android.presentation.enums;

public enum BorrowType {

    REQUEST("requested"), LOANS("rejected"), IN_PROGRESS("inProgress");

    private String text;

    BorrowType(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}