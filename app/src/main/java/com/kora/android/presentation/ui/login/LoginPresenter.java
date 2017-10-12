package com.kora.android.presentation.ui.login;

import com.kora.android.common.helper.SessionPrefHelper;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.login.LoginUseCase;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginView> {

    private final SessionPrefHelper mSessionPrefHelper;
    private final LoginUseCase mLoginUseCase;

    private String mIdentifier;
    private String mPassword;

    @Inject
    public LoginPresenter(final SessionPrefHelper sessionPrefHelper,
                          final LoginUseCase loginUseCase) {
        mSessionPrefHelper = sessionPrefHelper;
        mLoginUseCase = loginUseCase;
    }

    public void startLoginTask() {
        if (mIdentifier == null || mIdentifier.isEmpty()) {
            getView().showEmptyIdentifier();
            return;
        }
        if (!StringUtils.isIdentifierValid(mIdentifier)) {
            getView().showIncorrectIdentifier();
            return;
        }
        if (!StringUtils.isIdentifierLongEnough(mIdentifier)) {
            getView().showTooShortIdentifier();
            return;
        }
        if (mPassword == null || mPassword.isEmpty()) {
            getView().showEmptyPassword();
            return;
        }
        if (!StringUtils.isPasswordLongEnough(mPassword)) {
            getView().showTooShortPassword();
            return;
        }

        mLoginUseCase.setData(mIdentifier, mPassword);
        mLoginUseCase.execute(new LoginObserver());
    }

    private Action mLoginAction = new Action() {
        @Override
        public void run() throws Exception {
            mLoginUseCase.execute(new LoginObserver());
        }
    };

    public void setIdentifier(final String identifier) {
        mIdentifier = identifier;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    private class LoginObserver extends DefaultInternetSubscriber {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onComplete() {
            if(!isViewAttached()) return;
            getView().showProgress(false);

            getView().showNextScreen();
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mLoginAction));
        }

        @Override
        public void handleUnauthorizedException() {
            getView().showServerError();
        }
    }
}
