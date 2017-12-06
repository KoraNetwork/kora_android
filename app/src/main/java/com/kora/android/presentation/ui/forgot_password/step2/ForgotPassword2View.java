package com.kora.android.presentation.ui.forgot_password.step2;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface ForgotPassword2View extends BaseView<ForgotPassword2Presenter> {

    void showEmptyNewPassword();
    void showIncorrectNewPassword();

    void showEmptyConfirmPassword();
    void showIncorrectConfirmPassword();

    void showNext();
}
