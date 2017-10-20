package com.kora.android.presentation.ui.registration.step4;

import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.balance.IncreaseBalanceUseCase;
import com.kora.android.domain.usecase.registration.RegisterUseCase;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class FourthStepPresenter extends BasePresenter<FourthStepView> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final RegisterUseCase mRegisterUseCase;
    private final IncreaseBalanceUseCase mIncreaseBalanceUseCase;

    private UserEntity mUserEntity;
    private String mConfirmPassword;

    @Inject
    public FourthStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                               final IncreaseBalanceUseCase increaseBalanceUseCase,
                               final RegisterUseCase registerUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mIncreaseBalanceUseCase = increaseBalanceUseCase;
        mRegisterUseCase = registerUseCase;
        mUserEntity = new UserEntity();
    }

    public void startGetCountryTask() {
        final CountryEntity countryEntity = mRegistrationPrefHelper.getCountry();
        setCurrency(countryEntity.getCurrency());
        setCountryCode(countryEntity.getCountryCode());
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

    public void setCountryCode(final String countryCode) {
        mUserEntity.setCountryCode(countryCode);
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
        if (mUserEntity.getEmail() == null || mUserEntity.getEmail().isEmpty()) {
            getView().showEmptyEmail();
            return;
        }
        if (!StringUtils.isEmailValid(mUserEntity.getEmail())) {
            getView().showIncorrectEmail();
            return;
        }
        if (!DateUtils.isDateValid(mUserEntity.getDateOfBirth())) {
            getView().showIncorrectDate();
            return;
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

//        mUserEntity.setPhoneNumber("380995339692");
//        mUserEntity.setIdentity("0x5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890".toLowerCase());

        mRegisterUseCase.setData(mUserEntity);
        mRegisterUseCase.execute(new RegistrationObserver());
    }

    private Action mRegistrationAction = new Action() {
        @Override
        public void run() throws Exception {
            mRegisterUseCase.execute(new RegistrationObserver());
        }
    };

    private class RegistrationObserver extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true, false, R.string.registration_increase_account_wait);
            Log.e("_____", "START");
        }

        @Override
        public void onNext(@NonNull final UserEntity userEntity) {
            if(!isViewAttached()) return;
            startIncreaseBalanceTask(userEntity);
//            getView().showNextScreen();
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

//        @Override
//        public void onComplete() {
//            if(!isViewAttached()) return;
//            getView().showProgress(false);
//        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if(!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isViewAttached()) return;
            getView().showErrorWithRetry(new RetryAction(mRegistrationAction));
        }
    }

    public void startIncreaseBalanceTask(final UserEntity userEntity) {
        mIncreaseBalanceUseCase.setData(userEntity);
        mIncreaseBalanceUseCase.execute(new IncreaseBalanceSubscriber());
    }

    private class IncreaseBalanceSubscriber extends DefaultWeb3jSubscriber<List<String>> {

//        @Override
//        protected void onStart() {
//            if(!isViewAttached()) return;
//            getView().showProgress(true);
//        }

        @Override
        public void onNext(@NonNull final List<String> transactionHashList) {
            if(!isViewAttached()) return;
            getView().showNextScreen();

            Log.e("_____", transactionHashList.toString());
        }

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);

            Log.e("_____", throwable.toString());
            throwable.printStackTrace();
        }

        @Override
        public void handleWeb3jError(final String message) {
            if(!isViewAttached()) return;
            getView().showError(message);
        }
    }

    @Override
    public void onDetachView() {
        mRegisterUseCase.dispose();
        mIncreaseBalanceUseCase.dispose();
    }
}