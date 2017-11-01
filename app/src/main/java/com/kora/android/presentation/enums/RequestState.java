package com.kora.android.presentation.enums;

public enum RequestState {

    INPROGRESS("In progress"), REJECTED("Rejected"), REQUESTED("Requested");

    private String text;

    RequestState(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
