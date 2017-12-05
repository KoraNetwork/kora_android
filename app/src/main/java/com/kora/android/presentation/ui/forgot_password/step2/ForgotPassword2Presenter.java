package com.kora.android.presentation.ui.forgot_password.step1;


import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.registration.step1.FirstStepView;

import javax.inject.Inject;

@ConfigPersistent
public class ForgotPassword1Presenter extends BasePresenter<FirstStepView> {

    @Inject
    public ForgotPassword1Presenter() {

    }
}
