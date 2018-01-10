package com.kora.android.presentation.ui.registration.step4;

import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.registration.RegisterUseCase;
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
    private final RegisterUseCase mRegisterUseCase;

    private UserEntity mUserEntity;
    private String mConfirmPassword;

    @Inject
    public FourthStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                               final RegisterUseCase registerUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
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
        if (mUserEntity.getLegalName() != null && !mUserEntity.getLegalName().isEmpty()) {
            if (!StringUtils.isNameValid(mUserEntity.getLegalName())) {
                getView().showIncorrectFullName();
                return;
            }
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
        if (!StringUtils.isPasswordValid(mUserEntity.getPassword())) {
            getView().showIncorrectPassword();
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
            getView().showProgress(true);
        }

        @Override
        public void onNext(@NonNull final UserEntity userEntity) {
            if(!isViewAttached()) return;
            getView().showNextScreen();
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onComplete() {
            if(!isViewAttached()) return;
            getView().showProgress(false);
        }

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

    @Override
    public void onDetachView() {
        mRegisterUseCase.dispose();
    }
}