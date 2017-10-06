package com.kora.android.presentation.ui.registration.step1;

import android.util.Log;

import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.domain.base.DefaultCompletableObserver;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.registration.GetPhoneNumberUseCase;
import com.kora.android.domain.usecase.registration.SendPhoneNumberUseCase;
import com.kora.android.domain.usecase.wallet.DeleteWalletsUseCase;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

public class FirstStepPresenter extends BasePresenter<FirstStepView> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final DeleteWalletsUseCase mDeleteWalletsUseCase;
    private final GetPhoneNumberUseCase mGetPhoneNumberUseCase;
    private final SendPhoneNumberUseCase mSendPhoneNumberUseCase;

    private String mPhoneNumber;

    @Inject
    public FirstStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                              final DeleteWalletsUseCase deleteWalletsUseCase,
                              final GetPhoneNumberUseCase getPhoneNumberUseCase,
                              final SendPhoneNumberUseCase sendPhoneNumberUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mDeleteWalletsUseCase = deleteWalletsUseCase;
        mGetPhoneNumberUseCase = getPhoneNumberUseCase;
        mSendPhoneNumberUseCase = sendPhoneNumberUseCase;
    }

    public void startDeleteWalletsTask() {
        mRegistrationPrefHelper.clear();
        addDisposable(mDeleteWalletsUseCase.execute(new DeleteWalletsObserver()));
    }

    public void startGetPhoneNumberTask() {
        addDisposable(mGetPhoneNumberUseCase.execute(new GetPhoneNumberObserver()));
    }

    public void startSendPhoneNumberTask() {
        if (mPhoneNumber == null || mPhoneNumber.isEmpty()) {
            getView().showEmptyPhoneNumber();
            return;
        }
        if (!StringUtils.isPhoneNumberValid(mPhoneNumber)) {
            getView().showIncorrectPhoneNumber();
            return;
        }
        mPhoneNumber = StringUtils.deletePlusIfNeeded(mPhoneNumber);

        mSendPhoneNumberUseCase.setData(mPhoneNumber);
        addDisposable(mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver()));
    }

    private Action mSendPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver()));
        }
    };

    public void setPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    private class DeleteWalletsObserver extends DefaultCompletableObserver {

        @Override
        protected void onStart() {
            super.onStart();
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);

            getView().showNextViews();
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);

            Log.e("_____", throwable.toString());
            throwable.printStackTrace();
        }
    }

    private class GetPhoneNumberObserver extends DefaultSingleObserver<String> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull final String phoneNumber) {
            if (!isViewAttached()) return;
            getView().showProgress(false);

            if (phoneNumber == null || phoneNumber.isEmpty())
                return;
            if (!StringUtils.isPhoneNumberValid(phoneNumber))
                return;
            getView().showPhoneNumber(phoneNumber);
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);

            Log.e("_____", throwable.toString());
            throwable.printStackTrace();
        }
    }

    private class SendPhoneNumberObserver extends DefaultSingleObserver<PhoneNumberResponse> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull final PhoneNumberResponse phoneNumberResponse) {
            if(!isViewAttached()) return;
            getView().showProgress(false);

            if (phoneNumberResponse.isSent()) {
                mRegistrationPrefHelper.storePhoneNumber(mPhoneNumber);
                getView().showNextScreen();
            } else {
                getView().showServerErrorPhoneNumber();
            }
        }

        @Override
        public void onError(@NonNull final Throwable e) {
            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);

            Log.e("_____", e.toString());
            e.printStackTrace();
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mSendPhoneNumberAction));
        }
    }
}