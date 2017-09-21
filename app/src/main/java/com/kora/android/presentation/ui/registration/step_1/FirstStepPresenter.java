package com.kora.android.presentation.ui.registration.step_1;

import com.kora.android.common.utils.TextValidator;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.domain.base.DefaultCompletableObserver;
import com.kora.android.domain.usecase.registration.SendPhoneNumberUseCase;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

public class FirstStepPresenter extends BasePresenter<FirstStepView> {

    private final SendPhoneNumberUseCase mSendPhoneNumberUseCase;

    private String mPhoneNumber;

    @Inject
    public FirstStepPresenter(SendPhoneNumberUseCase sendPhoneNumberUseCase) {
        mSendPhoneNumberUseCase = sendPhoneNumberUseCase;
    }

    public void sendSms() {
        if (mPhoneNumber == null || mPhoneNumber.isEmpty()) {
            getView().showEmptyPhoneNumber();
            return;
        }
        if (!TextValidator.isPhoneNumberValid(mPhoneNumber)) {
            getView().showIncorrectPhoneNumber();
            return;
        }
        mPhoneNumber = TextValidator.addPlusIfNeeded(mPhoneNumber);
        mSendPhoneNumberUseCase.setData(mPhoneNumber);
        addDisposable(mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver()));
    }

    private Action sendPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver()));
        }
    };

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    private class SendPhoneNumberObserver extends DefaultCompletableObserver {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
            // good
        }

        @Override
        public void onError(@NonNull final Throwable e) {
            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
            // bad
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(sendPhoneNumberAction));
        }
    }
}
