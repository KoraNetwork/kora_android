package com.kora.android.presentation.ui.forgot_password.step1;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface ForgotPassword1View extends BaseView<ForgotPassword1Presenter> {

    void showEmptyEmail();
    void showIncorrectEmail();

    void showNext();
}
