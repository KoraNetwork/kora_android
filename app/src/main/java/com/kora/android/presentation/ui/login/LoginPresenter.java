package com.kora.android.presentation.ui.login;

import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.login.LoginUseCase;
import com.kora.android.domain.usecase.login.LogoutUseCase;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<LoginView> {

    private final LoginUseCase mLoginUseCase;
    private final LogoutUseCase mLogoutUseCase;

    private String mIdentifier;
    private String mPassword;

    @Inject
    public LoginPresenter(final LoginUseCase loginUseCase,
                          final LogoutUseCase logoutUseCase) {
        mLoginUseCase = loginUseCase;
        mLogoutUseCase = logoutUseCase;
    }

    public void setIdentifier(final String identifier) {
        mIdentifier = identifier;
    }

    public void setPassword(String password) {
        mPassword = password;
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
        if (mPassword == null || mPassword.isEmpty()) {
            getView().showEmptyPassword();
            return;
        }
        mIdentifier = StringUtils.deletePlusIfNeeded(mIdentifier);

        mLoginUseCase.setData(mIdentifier, mPassword);
        mLoginUseCase.execute(new LoginSubscriber());
    }

    private Action mLoginAction = new Action() {
        @Override
        public void run() throws Exception {
            mLoginUseCase.execute(new LoginSubscriber());
        }
    };

    private class LoginSubscriber extends DefaultInternetSubscriber {

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
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mLoginAction));
        }
    }

    public void startLogoutTask() {
        mLogoutUseCase.setData(false);
        mLogoutUseCase.execute(new LogoutSubscriberSubscriber());
    }

    private Action mLogoutAction = new Action() {

        @Override
        public void run() throws Exception {
            mLogoutUseCase.execute(new LogoutSubscriberSubscriber());
        }
    };

    private class LogoutSubscriberSubscriber extends DefaultInternetSubscriber {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onComplete() {
            if(!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mLogoutAction));
        }
    }

    @Override
    public void onDetachView() {
        mLoginUseCase.dispose();
        mLogoutUseCase.dispose();
    }
}
