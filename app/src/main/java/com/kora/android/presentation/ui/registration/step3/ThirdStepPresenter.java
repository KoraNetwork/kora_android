package com.kora.android.presentation.ui.registration.step3;

import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.data.web3j.model.response.IdentityCreatedResponse;
import com.kora.android.domain.base.DefaultWeb3jSubscriber;
import com.kora.android.domain.usecase.identity.CreateIdentityUseCase;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

import static com.kora.android.presentation.ui.registration.step3.ThirdStepActivity.VIEW_MODE_CONFIRM;
import static com.kora.android.presentation.ui.registration.step3.ThirdStepActivity.VIEW_MODE_ENTER;

@ConfigPersistent
public class ThirdStepPresenter  extends BasePresenter<ThirdStepView> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;
    private final CreateIdentityUseCase mCreateIdentityUseCase;

    private String mViewMode;
    private String mPinCode;

    @Inject
    public ThirdStepPresenter(final RegistrationPrefHelper registrationPrefHelper,
                              final CreateIdentityUseCase createIdentityUseCase) {
        mRegistrationPrefHelper = registrationPrefHelper;
        mCreateIdentityUseCase = createIdentityUseCase;
    }

    public void startCreateIdentityTask(final String pinCode) {
        if (pinCode == null || pinCode.isEmpty()) {
            getView().showEmptyPinCode();
            return;
        }
        if (!StringUtils.isPinCodeLongEnough(pinCode)) {
            getView().showTooShortPinCode();
            return;
        }
        switch (mViewMode) {
            case VIEW_MODE_ENTER:
                mPinCode = pinCode;
                getView().showAnotherMode();
                break;
            case VIEW_MODE_CONFIRM:
                if (!mPinCode.equals(pinCode)) {
                    getView().showPinCodeDoesNotMatch();
                    return;
                }
                mCreateIdentityUseCase.setData(pinCode);
                mCreateIdentityUseCase.execute(new CreateIdentityUseCaseObserver());
                break;
        }
    }

    private Action mCreateIdentityAction = new Action() {
        @Override
        public void run() throws Exception {
            mCreateIdentityUseCase.execute(new CreateIdentityUseCaseObserver());
        }
    };

    public String getViewMode() {
        return mViewMode;
    }

    public void setViewMode(String viewMode) {
        mViewMode = viewMode;
    }

    private class CreateIdentityUseCaseObserver extends DefaultWeb3jSubscriber<IdentityCreatedResponse> {

        @Override
        protected void onStart() {
            if(!isViewAttached()) return;
            getView().showProgress(true, false, R.string.registration_creating_identity_wait);
        }

        @Override
        public void onNext(@NonNull final IdentityCreatedResponse identityCreatedResponse) {
            mRegistrationPrefHelper.storeIdentityAddress(identityCreatedResponse.getIdentity());
            mRegistrationPrefHelper.storeCreatorAddress(identityCreatedResponse.getCreator());
            mRegistrationPrefHelper.storeRecoveryAddress(identityCreatedResponse.getRecoveryKey());
            mRegistrationPrefHelper.storeOwnerAddress(identityCreatedResponse.getOwner());

            if(!isViewAttached()) return;
            getView().showNextScreen();
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
        mCreateIdentityUseCase.dispose();
    }
}
