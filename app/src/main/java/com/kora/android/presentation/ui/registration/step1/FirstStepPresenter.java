package com.kora.android.presentation.ui.registration.step1;

import android.util.Log;

import com.google.gson.Gson;
import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.network.model.response.ErrorResponseTwilio;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.domain.base.DefaultCompletableObserver;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.registration.SendPhoneNumberUseCase;
import com.kora.android.domain.usecase.wallet.DeleteWalletsUseCase;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_CURRENCY;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_FLAG;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_NAME;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_PHONE_CODE;

public class FirstStepPresenter extends BasePresenter<FirstStepView> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final DeleteWalletsUseCase mDeleteWalletsUseCase;
    private final SendPhoneNumberUseCase mSendPhoneNumberUseCase;

    private Country mCountry;
    private String mPhoneNumber;

    @Inject
    public FirstStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                              final DeleteWalletsUseCase deleteWalletsUseCase,
                              final SendPhoneNumberUseCase sendPhoneNumberUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mDeleteWalletsUseCase = deleteWalletsUseCase;
        mSendPhoneNumberUseCase = sendPhoneNumberUseCase;
        mCountry = new Country()
                .addName(DEFAULT_COUNTRY_NAME)
                .addCurrency(DEFAULT_COUNTRY_CURRENCY)
                .addPhoneCode(DEFAULT_COUNTRY_PHONE_CODE)
                .addFlag(DEFAULT_COUNTRY_FLAG);
    }

    public void startDeleteWalletsTask() {
        mRegistrationPrefHelper.clear();
        addDisposable(mDeleteWalletsUseCase.execute(new DeleteWalletsObserver()));
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
        final String phoneNumber =
                StringUtils.deletePlusIfNeeded(mCountry.getPhoneCode()) + mPhoneNumber;

        mSendPhoneNumberUseCase.setData(phoneNumber);
        addDisposable(mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver()));
    }

    private Action mSendPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver()));
        }
    };

    public void setCountry(final Country country) {
        mCountry = country;
    }

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
                mRegistrationPrefHelper.storeCountry(mCountry);
                final String phoneNumber =
                        StringUtils.deletePlusIfNeeded(mCountry.getPhoneCode()) + mPhoneNumber;
                mRegistrationPrefHelper.storePhoneNumber(phoneNumber);
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

        @Override
        public void handleValidationException(final RetrofitException retrofitException) {
            try {
                final String errorResponseString = new String(retrofitException.getResponse().errorBody().bytes(), "UTF-8");
                final ErrorResponseTwilio errorResponseTwilio = new Gson().fromJson(errorResponseString, ErrorResponseTwilio.class);
//                Log.e("_____", errorResponseString);
//                Log.e("_____", errorResponseTwilio.toString());
                getView().showTwilioErrorPhoneNumber(errorResponseTwilio.getMessage());
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}