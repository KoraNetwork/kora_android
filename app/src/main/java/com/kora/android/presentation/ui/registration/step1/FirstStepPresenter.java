package com.kora.android.presentation.ui.registration.step1;

import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.usecase.registration.SendPhoneNumberUseCase;
import com.kora.android.domain.usecase.wallet.DeleteWalletsUseCase;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_CODE;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_CURRENCY;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_FLAG;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_NAME;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_PHONE_CODE;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_CURRENCY_NAME_FULL;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_ERC_20_TOKEN;

public class FirstStepPresenter extends BasePresenter<FirstStepView> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final DeleteWalletsUseCase mDeleteWalletsUseCase;
    private final SendPhoneNumberUseCase mSendPhoneNumberUseCase;

    private CountryEntity mCountryEntity;
    private String mPhoneNumber;

    @Inject
    public FirstStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                              final DeleteWalletsUseCase deleteWalletsUseCase,
                              final SendPhoneNumberUseCase sendPhoneNumberUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mDeleteWalletsUseCase = deleteWalletsUseCase;
        mSendPhoneNumberUseCase = sendPhoneNumberUseCase;
        mCountryEntity = new CountryEntity()
                .addCountryCode(DEFAULT_COUNTRY_CODE)
                .addName(DEFAULT_COUNTRY_NAME)
                .addCurrency(DEFAULT_COUNTRY_CURRENCY)
                .addCurrencyNameFull(DEFAULT_CURRENCY_NAME_FULL)
                .addERC20Token(DEFAULT_ERC_20_TOKEN)
                .addPhoneCode(DEFAULT_COUNTRY_PHONE_CODE)
                .addFlag(DEFAULT_COUNTRY_FLAG);
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
                StringUtils.deletePlusIfNeeded(mCountryEntity.getPhoneCode()) + mPhoneNumber;

        mSendPhoneNumberUseCase.setData(phoneNumber);
        mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver());
    }

    private Action mSendPhoneNumberAction = new Action() {
        @Override
        public void run() throws Exception {
            mSendPhoneNumberUseCase.execute(new SendPhoneNumberObserver());
        }
    };

    public void setCountry(final CountryEntity countryEntity) {
        mCountryEntity = countryEntity;
    }

    public void setPhoneNumber(final String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    private class SendPhoneNumberObserver extends DefaultInternetSubscriber<Object> {

        @Override
        protected void onStart() {
            if (!isViewAttached()) return;
            getView().showProgress(true);
        }

        @Override
        public void onError(@NonNull final Throwable e) {
            super.onError(e);
            if (!isViewAttached()) return;
            getView().showProgress(false);
        }

        @Override
        public void onComplete() {
            mRegistrationPrefHelper.storeCountry(mCountryEntity);
            final String phoneNumber =
                    StringUtils.deletePlusIfNeeded(mCountryEntity.getPhoneCode()) + mPhoneNumber;
            mRegistrationPrefHelper.storePhoneNumber(phoneNumber);

            if (!isViewAttached()) return;
            getView().showProgress(false);
            getView().showNextScreen();
        }

        @Override
        public void handleUnprocessableEntity(ErrorModel errorModel) {
            if (!isViewAttached()) return;
            getView().showError(errorModel.getError());
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            getView().showErrorWithRetry(new RetryAction(mSendPhoneNumberAction));
        }
    }

    @Override
    public void onDetachView() {
        mDeleteWalletsUseCase.dispose();
        mSendPhoneNumberUseCase.dispose();
    }
}