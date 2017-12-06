package com.kora.android.presentation.ui.forgot_password.step2;

import com.kora.android.common.utils.StringUtils;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class ForgotPassword2Presenter extends BasePresenter<ForgotPassword2View> {

    private String mNewPassword;
    private String mConfirmPassword;

    @Inject
    public ForgotPassword2Presenter() {

    }

    public void setNewPassword(final String newPassword) {
        mNewPassword = newPassword;
    }

    public void setConfirmPassword(final String confirmPassword) {
        mConfirmPassword = confirmPassword;
    }

    public void confirmPassword() {
        if (mNewPassword == null || mNewPassword.isEmpty()) {
            getView().showEmptyNewPassword();
            return;
        }
        if (!StringUtils.isPasswordValid(mNewPassword)) {
            getView().showIncorrectNewPassword();
            return;
        }
        if (mConfirmPassword == null || mConfirmPassword.isEmpty()) {
            getView().showEmptyConfirmPassword();
            return;
        }
        if (!mNewPassword.equals(mConfirmPassword)) {
            getView().showIncorrectConfirmPassword();
            return;
        }
        getView().showNext();
    }
}
