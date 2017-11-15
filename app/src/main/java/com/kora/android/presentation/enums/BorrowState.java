package com.kora.android.presentation.enums;


public enum BorrowState {

    ONGOING("On going"),
    AGREED("Agreed"),
    REJECTED("Rejected"),
    EXPIRED("Expired"),
    PENDING("Pending"),
    OVERDUE("Overdue");

    private String mText;

    BorrowState(final String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }
}
