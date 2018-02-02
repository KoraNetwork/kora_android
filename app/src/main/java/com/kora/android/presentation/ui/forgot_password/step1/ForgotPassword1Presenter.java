package com.kora.android.presentation.ui.forgot_password.step1;

import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.user.ForgotPasswordUseCase;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.functions.Action;

@ConfigPersistent
public class ForgotPassword1Presenter extends BasePresenter<ForgotPassword1View> {

    private final ForgotPasswordUseCase mForgotPasswordUseCase;

    private String mEmail;

    @Inject
    public ForgotPassword1Presenter(final ForgotPasswordUseCase forgotPasswordUseCase) {
        mForgotPasswordUseCase = forgotPasswordUseCase;
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

        mForgotPasswordUseCase.setData(mEmail);
        mForgotPasswordUseCase.execute(new ForgotPasswordSubscriber());
    }

    private Action mForgotPasswordAction = new Action() {
        @Override
        public void run() throws Exception {
            mForgotPasswordUseCase.execute(new ForgotPasswordSubscriber());
        }
    };

    private class ForgotPasswordSubscriber extends DefaultInternetSubscriber<Object> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onNext(final Object object) {
            if (!isViewAttached()) return;
            getView().showMessageIsSent();
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mForgotPasswordAction));
        }
    }

    @Override
    public void onDetachView() {
        mForgotPasswordUseCase.dispose();
    }
}
