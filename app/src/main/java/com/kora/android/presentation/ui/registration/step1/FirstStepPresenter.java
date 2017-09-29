package com.kora.android.presentation.ui.registration.step1;

import android.util.Log;

import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.TextValidator;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.registration.GetPhoneNumberUseCase;
import com.kora.android.domain.usecase.registration.SendPhoneNumberUseCase;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

public class FirstStepPresenter extends BasePresenter<FirstStepView> {

    private final RegistrationPrefHelper mRregistrationPrefHelper;
    private final GetPhoneNumberUseCase mGetPhoneNumberUseCase;
    private final SendPhoneNumberUseCase mSendPhoneNumberUseCase;

    private String mPhoneNumber;

    @Inject
    public FirstStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                              final GetPhoneNumberUseCase getPhoneNumberUseCase,
                              final SendPhoneNumberUseCase sendPhoneNumberUseCase) {
        mRregistrationPrefHelper = registrationPrefHelper;
        mGetPhoneNumberUseCase = getPhoneNumberUseCase;
        mSendPhoneNumberUseCase = sendPhoneNumberUseCase;
    }

    public void startGetPhoneNumberTask() {
        addDisposable(mGetPhoneNumberUseCase.execute(new GetPhoneNumberObserver()));
    }

    public void startSendPhoneNumberTask() {
        if (mPhoneNumber == null || mPhoneNumber.isEmpty()) {
            getView().showEmptyPhoneNumber();
            return;
        }
        if (!TextValidator.isPhoneNumberValid(mPhoneNumber)) {
            getView().showIncorrectPhoneNumber();
            return;
        }
        mPhoneNumber = TextValidator.deletePlusIfNeeded(mPhoneNumber);
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


    private class GetPhoneNumberObserver extends DefaultSingleObserver<String> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull String phoneNumber) {
            if (!isViewAttached()) return;
            getView().showProgress(false);

            if (phoneNumber == null || phoneNumber.isEmpty())
                return;
            if (!TextValidator.isPhoneNumberValid(phoneNumber))
                return;
            getView().showPhoneNumber(phoneNumber);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }
    }

    private class SendPhoneNumberObserver extends DefaultSingleObserver<PhoneNumberResponse> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull PhoneNumberResponse phoneNumberResponse) {
            if(!isViewAttached()) return;
            getView().showProgress(false);

            if (phoneNumberResponse.isSent()) {
                mRregistrationPrefHelper.storePhoneNumber(mPhoneNumber);
                getView().showNextScreen();
            }
        }

        @Override
        public void onError(@NonNull final Throwable e) {
            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);

            Log.e("_____", "onError()");
            e.printStackTrace();
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(sendPhoneNumberAction));
        }
    }
}