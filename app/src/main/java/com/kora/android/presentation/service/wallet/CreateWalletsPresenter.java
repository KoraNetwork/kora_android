package com.kora.android.presentation.service.wallet;

import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.data.network.exception.RetrofitException;
import com.kora.android.data.web3j.model.response.IdentityCreatedResponse;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.DefaultInternetSubscriber;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.web3j.IncreaseBalanceUseCase;
import com.kora.android.domain.usecase.web3j.CreateIdentityUseCase;
import com.kora.android.domain.usecase.user.ConvertAmountUseCase;
import com.kora.android.domain.usecase.user.UpdateUserUseCase;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.service.BaseServicePresenter;
import com.kora.android.presentation.ui.base.custom.RetryAction;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

import static com.kora.android.common.Keys.CURRENCY_USD;
import static com.kora.android.common.Keys.DEFAULT_USD_BALANCE;

@ConfigPersistent
public class CreateWalletsPresenter extends BaseServicePresenter<CreateWalletsContractor> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final CreateIdentityUseCase mCreateIdentityUseCase;
    private final UpdateUserUseCase mUpdateUserUseCase;
    private final ConvertAmountUseCase mConvertAmountUseCase;
    private final IncreaseBalanceUseCase mIncreaseBalanceUseCase;

    private UserEntity mUserEntity;

    @Inject
    public CreateWalletsPresenter(final RegistrationPrefHelper registrationPrefHelper,
                                  final UpdateUserUseCase updateUserUseCase,
                                  final CreateIdentityUseCase createIdentityUseCase,
                                  final ConvertAmountUseCase convertAmountUseCase,
                                  final IncreaseBalanceUseCase increaseBalanceUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mCreateIdentityUseCase = createIdentityUseCase;
        mUpdateUserUseCase = updateUserUseCase;
        mConvertAmountUseCase = convertAmountUseCase;
        mIncreaseBalanceUseCase = increaseBalanceUseCase;
    }

    public void createWallets() {
        final String pinCode = mRegistrationPrefHelper.getPinCode();

        mCreateIdentityUseCase.setData(pinCode);
        mCreateIdentityUseCase.execute(new CreateIdentitySubscriber());
    }

    public void setUserEntity(final UserEntity userEntity) {
        mUserEntity = userEntity;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

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
            Log.e("_____", throwable.toString());
            throwable.printStackTrace();
        }

        @Override
        public void handleNetworkError(final Throwable throwable) {
            if (!isServiceAttached()) return;
            getService().showError(R.string.web3j_error_message_network_problems, new RetryAction(mCreateIdentityAction));
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isServiceAttached()) return;
            getService().showError(message, new RetryAction(mCreateIdentityAction));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

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
        public void onNext(final UserEntity userEntity) {
            if (!userEntity.getCurrency().equals(CURRENCY_USD)) {
                mConvertAmountUseCase.setData(DEFAULT_USD_BALANCE, CURRENCY_USD, userEntity.getCurrency());
                mConvertAmountUseCase.execute(new ConvertAmountSubscriber());
            } else {
                mIncreaseBalanceUseCase.setData(userEntity, DEFAULT_USD_BALANCE);
                mIncreaseBalanceUseCase.execute(new IncreaseBalanceSubscriber());
            }
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
            getService().showError(retrofitException.getMessage(), new RetryAction(mUpdateUserAction));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private Action mConvertAmountAction = new Action() {
        @Override
        public void run() throws Exception {
            mConvertAmountUseCase.execute(new ConvertAmountSubscriber());
        }
    };

    private class ConvertAmountSubscriber extends DefaultInternetSubscriber<Double> {

        @Override
        public void onNext(final Double amount) {
            mIncreaseBalanceUseCase.setData(mUserEntity, amount);
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
            getService().showError(retrofitException.getMessage(), new RetryAction(mConvertAmountAction));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

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
            mRegistrationPrefHelper.clear();
            getService().finishService();
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
            getService().showError(R.string.web3j_error_message_network_problems, new RetryAction(mCreateIdentityAction));
        }

        @Override
        public void handleWeb3jError(final String message) {
            if (!isServiceAttached()) return;
            getService().showError(message, new RetryAction(mIncreaseBalanceAction));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDetachService() {
        mCreateIdentityUseCase.dispose();
        mUpdateUserUseCase.dispose();
        mConvertAmountUseCase.dispose();
        mIncreaseBalanceUseCase.dispose();
    }
}
