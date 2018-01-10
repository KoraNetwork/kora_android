package com.kora.android.presentation.ui.main.fragments.send_feedback;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class SendFeedbackPresenter extends BasePresenter<SendFeedbackView> {

    @Inject
    public SendFeedbackPresenter() {

    }
}
