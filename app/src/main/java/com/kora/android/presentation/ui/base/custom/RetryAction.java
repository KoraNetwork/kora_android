package com.kora.android.presentation.ui.base.custom;

import android.view.View;

import io.reactivex.functions.Action;

public class RetryAction implements View.OnClickListener {

    private final Action mAction;

    public RetryAction(final Action action) {
        mAction = action;
    }

    @Override
    public void onClick(final View v) {
        try {
            mAction.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
