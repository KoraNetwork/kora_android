package com.kora.android.presentation.ui.registration.step2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.network.model.response.ConfirmationCodeResponse;
import com.kora.android.data.network.model.response.PhoneNumberResponse;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.registration.SendConfirmationCodeUseCase;
import com.kora.android.domain.usecase.registration.SendPhoneNumberUseCase;
import com.kora.android.presentation.service.IncomingSmsService;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

import static com.kora.android.common.Keys.IncomingSms.INCOMING_SMS_ACTION_RECEIVED;
import static com.kora.android.common.Keys.IncomingSms.INCOMING_SMS_EXTRA_MESSAGE;

public class SecondStepPresenter extends BasePresenter<SecondStepView> {

    private final Context mContext;
    private final BroadcastReceiver mServiceResultReceiver;

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final SendConfirmationCodeUseCase mSendConfirmationCodeUseCase;
    private final SendPhoneNumberUseCase mSendPhoneNumberUseCase;

    private String mConfirmationCode;

    @Inject
    public SecondStepPresenter(final Context context,
                               final RegistrationPrefHelper registrationPrefHelper,
                               final SendConfirmationCodeUseCase sendConfirmationCodeUseCase,
                               final SendPhoneNumberUseCase sendPhoneNumberUseCase) {
        mContext = context;
        mServiceResultReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (intent.hasExtra(INCOMING_SMS_EXTRA_MESSAGE)) {
                    final String message = intent.getStringExtra(INCOMING_SMS_EXTRA_MESSAGE);
                    final String confirmationCode = StringUtils.getCodeFromMessage(message);

                    if(!isViewAttached()) return;
                    getView().showConfirmationCode(confirmationCode);
                }
            }
        };
        mRegistrationPrefHelper = registrationPrefHelper;
        mSendConfirmationCodeUseCase = sendConfirmationCodeUseCase;
        mSendPhoneNumberUseCase = sendPhoneNumberUseCase;
    }

    public void startIncomingSmsService() {
        mContext.startService(new Intent(mContext, IncomingSmsService.class));
        LocalBroadcastManager.getInstance(mContext)
                .registerReceiver(mServiceResultReceiver, new IntentFilter(INCOMING_SMS_ACTION_RECEIVED));
    }

    public void stopIncomingSmsService() {
        if (CommonUtils.isServiceRunning(mContext, IncomingSmsService.class)) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mServiceResultReceiver);
            mContext.stopService(new Intent(mContext, IncomingSmsService.class));
        }
    }

    public void startSendConfirmationCodeTask() {
        if (mConfirmationCode == null || mConfirmationCode.isEmpty()) {
            getView().showEmptyConfirmationCode();
            return;
        }
        if (!StringUtils.isConfirmationCodeValid(mConfirmationCode)) {
            getView().showIncorrectConfirmationCode();
            return;
        }
        final String mPhoneNumber = mRegistrationPrefHelper.getPhoneNumber();

        mSendConfirmationCodeUseCase.setData(mPhoneNumber, mConfirmationCode);
        addDisposable(mSendConfirmationCodeUseCase.execute(new SendConfirmationCodeObserver()));
    }

    private Action mSendConfirmationCodeAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mSendConfirmationCodeUseCase.execute(new SendConfirmationCodeObserver()));
        }
    };

    public void startResendPhoneNumberTask() {
        final String phoneNumber = mRegistrationPrefHelper.getPhoneNumber();
        mSendPhoneNumberUseCase.setData(phoneNumber);
        addDisposable(mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver()));
    }

    private Action mSendPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver()));
        }
    };

    public void setConfirmationCode(String confirmationCode) {
        this.mConfirmationCode = confirmationCode;
    }

    private class SendConfirmationCodeObserver extends DefaultSingleObserver<ConfirmationCodeResponse> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull ConfirmationCodeResponse confirmationCodeResponse) {
            if (!isViewAttached()) return;
            getView().showProgress(false);

            if (confirmationCodeResponse.isConfirmed()) {
                getView().showNextScreen();
            } else {
                getView().showServerErrorConfirmationCode();
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
            getView().showErrorWithRetry(new RetryAction(mSendConfirmationCodeAction));
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
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);

            Log.e("_____", "onError()");
            throwable.printStackTrace();
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mSendPhoneNumberAction));
        }
    }
}
