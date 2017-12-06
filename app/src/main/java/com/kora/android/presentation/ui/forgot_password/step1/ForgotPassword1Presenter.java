package com.kora.android.presentation.ui.forgot_password.step1;

import com.kora.android.common.utils.StringUtils;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class ForgotPassword1Presenter extends BasePresenter<ForgotPassword1View> {

    private String mEmail;

    @Inject
    public ForgotPassword1Presenter() {

    }

    public void setEmail(final String email) {
        mEmail = email;
    }

    public void sendEmail() {
        if (mEmail == null || mEmail.isEmpty()) {
            getView().showEmptyEmail();
            return;
        }
        if (!StringUtils.isEmailValid(mEmail)) {
            getView().showIncorrectEmail();
            return;
        }
        getView().showNext();
    }
}
