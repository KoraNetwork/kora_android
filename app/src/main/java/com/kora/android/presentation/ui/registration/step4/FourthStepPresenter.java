package com.kora.android.presentation.ui.registration.step4;

import android.util.Log;

import com.google.gson.Gson;
import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.network.model.response.ErrorResponse;
import com.kora.android.data.network.model.response.UserResponse;
import com.kora.android.domain.base.DefaultSingleObserver;
import com.kora.android.domain.usecase.registration.RegistrationUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.model.User;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class FourthStepPresenter extends BasePresenter<FourthStepView> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final RegistrationUseCase mRegistrationUseCase;

    private User mUser;
    private String mConfirmPassword;

    @Inject
    public FourthStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                               final RegistrationUseCase registrationUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mRegistrationUseCase = registrationUseCase;
        mUser = new User();
    }

    public void setAvatar(final String avatar) {
        mUser.setAvatar(avatar);
    }

    public void setUserName(final String userName) {
        mUser.setUserName(userName);
    }

    public void setLegalName(final String legalName) {
        mUser.setLegalName(legalName);
    }

    public void setEmail(final String email) {
        mUser.setEmail(email);
    }

    public void setDateOfBirth(final String dateOfBirth) {
        mUser.setDateOfBirth(dateOfBirth);
    }

    public void setCurrency(final String currency) {
        mUser.setCurrency(currency);
    }

    public void setPostalCode(final String postalCode) {
        mUser.setPostalCode(postalCode);
    }

    public void setAddress(final String address) {
        mUser.setAddress(address);
    }

    public void setPassword(final String password) {
        mUser.setPassword(password);
    }

    public void setConfirmPassword(final String confirmPassword) {
        mConfirmPassword = confirmPassword;
    }

    public void startRegistrationTask() {
        if (mUser.getUserName() == null || mUser.getUserName().isEmpty()) {
            getView().showEmptyUserName();
            return;
        }
        if (!StringUtils.isUserNameValid(mUser.getUserName())) {
            getView().showIncorrectUserName();
            return;
        }
        if (!StringUtils.isUserNameLongEnough(mUser.getUserName())) {
            getView().showTooShortUserName();
            return;
        }
        if (mUser.getEmail() != null && !mUser.getEmail().isEmpty()) {
            if (!StringUtils.isEmailValid(mUser.getEmail())) {
                getView().showIncorrectEmail();
                return;
            }
        }
        if (mUser.getCurrency() == null || mUser.getCurrency().isEmpty()) {
            getView().showEmptyCurrency();
            return;
        }
        if (mUser.getPassword() == null || mUser.getPassword().isEmpty()) {
            getView().showEmptyPassword();
            return;
        }
        if (!StringUtils.isPasswordLongEnough(mUser.getPassword())) {
            getView().showTooShortPassword();
            return;
        }
        if (mConfirmPassword == null || mConfirmPassword.isEmpty()) {
            getView().showEmptyConfirmPassword();
            return;
        }
        if (!mUser.getPassword().equals(mConfirmPassword)) {
            getView().showIncorrectConfirmPassword();
            return;
        }
        mUser.setPhoneNumber(mRegistrationPrefHelper.getPhoneNumber());
        mUser.setIdentity(mRegistrationPrefHelper.getIdentityAddress());
        mUser.setCreator(mRegistrationPrefHelper.getCreatorAddress());
        mUser.setRecoveryKey(mRegistrationPrefHelper.getRecoveryAddress());
        mUser.setOwner(mRegistrationPrefHelper.getOwnerAddress());

//        mUser.setPhoneNumber("380995551114");
//        mUser.setIdentity("0x5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890".toLowerCase());

        mRegistrationUseCase.setData(mUser);
        addDisposable(mRegistrationUseCase.execute(new RegistrationObserver()));
    }

    private Action mRegistrationAction = new Action() {
        @Override
        public void run() throws Exception {
            addDisposable(mRegistrationUseCase.execute(new RegistrationObserver()));
        }
    };

    private class RegistrationObserver extends DefaultSingleObserver<UserResponse> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onSuccess(@NonNull final UserResponse userResponse) {
            if(!isViewAttached()) return;
            getView().showProgress(false);

            Log.e("_____", userResponse.toString());
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
            getView().showErrorWithRetry(new RetryAction(mRegistrationAction));
        }

        @Override
        public void handleValidationException(final RetrofitException retrofitException) {
            try {
                final String errorResponseString = new String(retrofitException.getResponse().errorBody().bytes(), "UTF-8");
                final ErrorResponse errorResponse = new Gson().fromJson(errorResponseString, ErrorResponse.class);
//                Log.e("_____", errorResponseString);
//                Log.e("_____", errorResponse.toString());
                String message = "";
                if (errorResponse.getInvalidattributes().getUserNameUnique() != null)
                    message += errorResponse.getInvalidattributes().getUserNameUnique().get(0).getMessage();
                if (errorResponse.getInvalidattributes().getEmail() != null)
                    message += errorResponse.getInvalidattributes().getEmail().get(0).getMessage();
                if (errorResponse.getInvalidattributes().getPhone() != null)
                    message += errorResponse.getInvalidattributes().getPhone().get(0).getMessage();
                if (!message.isEmpty())
                    getView().showServerErrorValidation(message);
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}