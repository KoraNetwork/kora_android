package com.kora.android.presentation.service.wallet;

import android.util.Log;

import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.web3j.model.response.IdentityCreatedResponse;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.balance.IncreaseBalanceUseCase;
import com.kora.android.domain.usecase.identity.CreateIdentityUseCase;
import com.kora.android.domain.usecase.user.UpdateUserUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.service.BaseServicePresenter;
import com.kora.android.presentation.ui.base.custom.RetryAction;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

@ConfigPersistent
public class CreateWalletPresenter extends BaseServicePresenter<CreateWalletContractor> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final IncreaseBalanceUseCase mIncreaseBalanceUseCase;
    private final CreateIdentityUseCase mCreateIdentityUseCase;
    private final UpdateUserUseCase mUpdateUserUseCase;

    private UserEntity mUserEntity;

    @Inject
    public CreateWalletPresenter(final RegistrationPrefHelper registrationPrefHelper,
                                 final IncreaseBalanceUseCase increaseBalanceUseCase,
                                 final UpdateUserUseCase updateUserUseCase,
                                 final CreateIdentityUseCase createIdentityUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mIncreaseBalanceUseCase = increaseBalanceUseCase;
        mUpdateUserUseCase = updateUserUseCase;
        mCreateIdentityUseCase = createIdentityUseCase;
    }

    public void createWallet(UserEntity user) {
        mUserEntity = user;
        String pinCode = mRegistrationPrefHelper.getPinCode();
        mCreateIdentityUseCase.setData(pinCode);
        mCreateIdentityUseCase.execute(new CreateIdentitySubscriber());
    }

    private Action mCreateIdentityAction = new Action() {
        @Override
        public void run() throws Exception {
            mCreateIdentityUseCase.execute(new CreateIdentitySubscriber());
        }
    };

    private class CreateIdentitySubscriber extends DefaultWeb3jSubscriber<IdentityCreatedResponse> {

        @Override
        protected void onStart() {
            if (!isServiceAttached()) return;
            getService().showCreateIdentityMessage();
        }

        @Override
        public void onNext(@NonNull final IdentityCreatedResponse identityCreatedResponse) {
            mRegistrationPrefHelper.storeIdentityAddress(identityCreatedResponse.getIdentity());
            mRegistrationPrefHelper.storeCreatorAddress(identityCreatedResponse.getCreator());
            mRegistrationPrefHelper.storeRecoveryAddress(identityCreatedResponse.getRecoveryKey());
            mRegistrationPrefHelper.storeOwnerAddress(identityCreatedResponse.getOwner());

            mUserEntity.setIdentity(identityCreatedResponse.getIdentity());
            mUserEntity.setCreator(identityCreatedResponse.getCreator());
            mUserEntity.setRecoveryKey(identityCreatedResponse.getRecoveryKey());
            mUserEntity.setOwner(identityCreatedResponse.getOwner());

            mUpdateUserUseCase.setData(mUserEntity);
            mUpdateUserUseCase.execute(new UpdateUserSubscriber());
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            throwable.printStackTrace();
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isServiceAttached()) return;
            getService().showError(message,  new RetryAction(mCreateIdentityAction));

        }

        @Override
        public void onComplete() {
            if (!isServiceAttached()) return;
            getService().showCreatedIdentityMessage();
        }
    }

    private Action mUpdateUserAction = new Action() {
        @Override
        public void run() throws Exception {
            mUpdateUserUseCase.execute(new UpdateUserSubscriber());
        }
    };

    private class UpdateUserSubscriber extends DefaultInternetSubscriber<UserEntity> {

        @Override
        protected void onStart() {
            if (!isServiceAttached()) return;
            getService().showUpdateUserMessage();
        }

        @Override
        public void onNext(UserEntity userEntity) {
            mIncreaseBalanceUseCase.setData(userEntity);
            mIncreaseBalanceUseCase.execute(new IncreaseBalanceSubscriber());
        }

        @Override
        public void onError(Throwable throwable) {
            super.onError(throwable);
            Log.e(CreateWalletService.class.getCanonicalName(), throwable.getLocalizedMessage());
            throwable.printStackTrace();
        }

        @Override
        public void handleNetworkError(RetrofitException retrofitException) {
            if (!isServiceAttached()) return;
            getService().showError(retrofitException.getMessage(), new RetryAction(mUpdateUserAction));
        }

        @Override
        public void onComplete() {
            if (!isServiceAttached()) return;
            getService().showUpdatedUserMessage();
        }
    }

    private Action mIncreaseBalanceAction = new Action() {
        @Override
        public void run() throws Exception {
            mIncreaseBalanceUseCase.execute(new IncreaseBalanceSubscriber());
        }
    };

    private class IncreaseBalanceSubscriber extends DefaultWeb3jSubscriber<List<String>> {

        @Override
        protected void onStart() {
            if (!isServiceAttached()) return;
            getService().showIncreaseBalanceMessage();
        }

        @Override
        public void onNext(@NonNull final List<String> transactionHashList) {
            if (!isServiceAttached()) return;
            getService().finishService();
        }

        @Override
        public void onError(@NonNull final Throwable throwable) {
            super.onError(throwable);
            Log.e(CreateWalletService.class.getCanonicalName(), throwable.getLocalizedMessage());
            throwable.printStackTrace();
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isServiceAttached()) return;
            getService().showError(message, new RetryAction(mIncreaseBalanceAction));
        }

        @Override
        public void onComplete() {
            if (!isServiceAttached()) return;
            getService().showIncreasedBalanceMessage();
        }
    }

    @Override
    public void onDetachService() {
        mIncreaseBalanceUseCase.dispose();
        mUpdateUserUseCase.dispose();
        mCreateIdentityUseCase.dispose();
    }
}
