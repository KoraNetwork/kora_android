package com.kora.android.presentation.ui.registration.step4;

import android.util.Log;

import com.google.gson.Gson;
import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.helper.SessionPrefHelper;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.network.model.response.ErrorResponseRegistration;
import com.kora.android.data.network.model.response.RegistrationResponse;
import com.kora.android.domain.base.DefaultCompletableObserver;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.registration.RegisterUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class FourthStepPresenter extends BasePresenter<FourthStepView> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final SessionPrefHelper mSessionPrefHelper;
    private final RegisterUseCase mRegisterUseCase;

    private UserEntity mUserEntity;
    private String mConfirmPassword;

    @Inject
    public FourthStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                               final SessionPrefHelper sessionPrefHelper,
                               final RegisterUseCase registerUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mSessionPrefHelper = sessionPrefHelper;
        mRegisterUseCase = registerUseCase;
        mUserEntity = new UserEntity();
    }

    public void startGetCountryTask() {
        final CountryEntity countryEntity = mRegistrationPrefHelper.getCountry();
        if (countryEntity != null)
            getView().showCurrency(countryEntity);
    }

    public void setAvatar(final String avatar) {
        mUserEntity.setAvatar(avatar);
    }

    public void setUserName(final String userName) {
        mUserEntity.setUserName(userName);
    }

    public void setLegalName(final String legalName) {
        mUserEntity.setLegalName(legalName);
    }

    public void setEmail(final String email) {
        mUserEntity.setEmail(email);
    }

    public void setDateOfBirth(final String dateOfBirth) {
        mUserEntity.setDateOfBirth(dateOfBirth);
    }

    public void setCurrency(final String currency) {
        mUserEntity.setCurrency(currency);
    }

    public void setPostalCode(final String postalCode) {
        mUserEntity.setPostalCode(postalCode);
    }

    public void setAddress(final String address) {
        mUserEntity.setAddress(address);
    }

    public void setPassword(final String password) {
        mUserEntity.setPassword(password);
    }

    public void setConfirmPassword(final String confirmPassword) {
        mConfirmPassword = confirmPassword;
    }

    public void startRegistrationTask() {
        if (mUserEntity.getUserName() == null || mUserEntity.getUserName().isEmpty()) {
            getView().showEmptyUserName();
            return;
        }
        if (!StringUtils.isUserNameValid(mUserEntity.getUserName())) {
            getView().showIncorrectUserName();
            return;
        }
        if (!StringUtils.isUserNameLongEnough(mUserEntity.getUserName())) {
            getView().showTooShortUserName();
            return;
        }
        if (mUserEntity.getEmail() != null && !mUserEntity.getEmail().isEmpty()) {
            if (!StringUtils.isEmailValid(mUserEntity.getEmail())) {
                getView().showIncorrectEmail();
                return;
            }
        }
        if (mUserEntity.getPassword() == null || mUserEntity.getPassword().isEmpty()) {
            getView().showEmptyPassword();
            return;
        }
        if (!StringUtils.isPasswordLongEnough(mUserEntity.getPassword())) {
            getView().showTooShortPassword();
            return;
        }
        if (mConfirmPassword == null || mConfirmPassword.isEmpty()) {
            getView().showEmptyConfirmPassword();
            return;
        }
        if (!mUserEntity.getPassword().equals(mConfirmPassword)) {
            getView().showIncorrectConfirmPassword();
            return;
        }
        mUserEntity.setPhoneNumber(mRegistrationPrefHelper.getPhoneNumber());
        mUserEntity.setIdentity(mRegistrationPrefHelper.getIdentityAddress());
        mUserEntity.setCreator(mRegistrationPrefHelper.getCreatorAddress());
        mUserEntity.setRecoveryKey(mRegistrationPrefHelper.getRecoveryAddress());
        mUserEntity.setOwner(mRegistrationPrefHelper.getOwnerAddress());

//        mUserEntity.setPhoneNumber("380995551114");
//        mUserEntity.setIdentity("0x5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890".toLowerCase());

        mRegisterUseCase.setData(mUserEntity);
        addDisposable(mRegisterUseCase.execute(new RegisterObserver()));
    }

    private Action mRegisterAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mRegisterUseCase.execute(new RegisterObserver()));
        }
    };

    private class RegisterObserver extends DefaultCompletableObserver {

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
            getView().showErrorWithRetry(new RetryAction(mRegisterAction));
        }

        @Override
        public void handleValidationException(final RetrofitException retrofitException) {
            try {
                final String errorResponseString = new String(retrofitException.getResponse().errorBody().bytes(), "UTF-8");
                final ErrorResponseRegistration errorResponseRegistration = new Gson().fromJson(errorResponseString, ErrorResponseRegistration.class);
//                Log.e("_____", errorResponseString);
//                Log.e("_____", errorResponseRegistration.toString());
                String message = "";
                if (errorResponseRegistration.getInvalidattributes().getUserNameUnique() != null)
                    message += errorResponseRegistration.getInvalidattributes().getUserNameUnique().get(0).getMessage();
                if (errorResponseRegistration.getInvalidattributes().getEmail() != null)
                    message += errorResponseRegistration.getInvalidattributes().getEmail().get(0).getMessage();
                if (errorResponseRegistration.getInvalidattributes().getPhone() != null)
                    message += errorResponseRegistration.getInvalidattributes().getPhone().get(0).getMessage();
                if (!message.isEmpty())
                    getView().showServerError(message);
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}