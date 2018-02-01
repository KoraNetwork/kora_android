package com.kora.android.presentation.service.wallet;

import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.data.network.config.ErrorModel;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.web3j.model.response.CreateWalletsResponse;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.user.CreateIdentityUseCase;
import com.kora.android.domain.usecase.web3j.CreateWalletsUseCase;
import com.kora.android.domain.usecase.user.IncreaseBalanceUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.service.BaseServicePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

@ConfigPersistent
public class CreateWalletsPresenter extends BaseServicePresenter<CreateWalletsContractor> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final CreateWalletsUseCase mCreateWalletsUseCase;
    private final CreateIdentityUseCase mCreateIdentityUseCase;
    private final IncreaseBalanceUseCase mIncreaseBalanceUseCase;

    @Inject
    public CreateWalletsPresenter(final RegistrationPrefHelper registrationPrefHelper,
                                  final CreateWalletsUseCase createWalletsUseCase,
                                  final CreateIdentityUseCase createIdentityUseCase,
                                  final IncreaseBalanceUseCase increaseBalanceUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mCreateWalletsUseCase = createWalletsUseCase;
        mCreateIdentityUseCase = createIdentityUseCase;
        mIncreaseBalanceUseCase = increaseBalanceUseCase;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void createWallets() {
        mCreateWalletsUseCase.setData(mRegistrationPrefHelper.getPinCode());
        mCreateWalletsUseCase.execute(new CreateWalletsSubscriber());
    }

    public void retryCreateWallets() {
        mCreateWalletsUseCase.execute(new CreateWalletsSubscriber());
    }

    private class CreateWalletsSubscriber extends DefaultWeb3jSubscriber<CreateWalletsResponse> {

        @Override
        protected void onStart() {
            if (!isServiceAttached()) return;
            getService().showCreateKeystoreFileMessage();
        }

        @Override
        public void onNext(@NonNull final CreateWalletsResponse createWalletsResponse) {
            mCreateIdentityUseCase.setData(
                    createWalletsResponse.getOwner(),
                    createWalletsResponse.getRecovery());
            mCreateIdentityUseCase.execute(new CreateIdentitySubscriber());
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();
        }

        @Override
        public void handleNetworkError(final Throwable throwable) {
            if (!isServiceAttached()) return;
            getService().showErrorWithRetry(
                    R.string.web3j_error_message_network_problems,
                    CreateWalletsService.ARG_CREATE_WALLETS);
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isServiceAttached()) return;
            getService().showErrorWithRetry(
                    message,
                    CreateWalletsService.ARG_CREATE_WALLETS);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void retryCreateIdentity() {
        mCreateIdentityUseCase.execute(new CreateIdentitySubscriber());
    }

    private class CreateIdentitySubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isServiceAttached()) return;
            getService().showCreateIdentityMessage();
        }

        @Override
        public void onNext(final UserEntity userEntity) {
            mIncreaseBalanceUseCase.execute(new IncreaseBalanceSubscriber());
        }

        @Override
        public void onError(final Throwable throwable) {
            super.onError(throwable);
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isServiceAttached()) return;
            getService().showErrorWithRetry(
                    R.string.web3j_error_message_no_network,
                    CreateWalletsService.ARG_CREATE_IDENTITY);
        }

        @Override
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            if (!isServiceAttached()) return;
            getService().showError(errorModel.getError());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void retryIncreaseBalance() {
        mIncreaseBalanceUseCase.execute(new IncreaseBalanceSubscriber());
    }

    private class IncreaseBalanceSubscriber extends DefaultInternetSubscriber<Object> {

        @Override
        protected void onStart() {
            if (!isServiceAttached()) return;
            getService().showIncreaseBalanceMessage();
        }

        @Override
        public void onNext(final Object object) {
            if (!isServiceAttached()) return;
            mRegistrationPrefHelper.clear();
            getService().finishService(true);
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();
        }

        @Override
        public void handleNetworkError(final RetrofitException retrofitException) {
            if (!isServiceAttached()) return;
            getService().showErrorWithRetry(
                    R.string.web3j_error_message_no_network,
                    CreateWalletsService.ARG_INCREASE_BALANCE);
        }

        @Override
        public void handleUnprocessableEntity(final ErrorModel errorModel) {
            if (!isServiceAttached()) return;
            getService().showError(errorModel.getError());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDetachService() {
        mCreateWalletsUseCase.dispose();
        mCreateIdentityUseCase.dispose();
        mIncreaseBalanceUseCase.dispose();
    }
}
