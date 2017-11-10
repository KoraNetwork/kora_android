package com.kora.android.presentation.ui.registration.step3;

import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import static com.kora.android.presentation.ui.registration.step3.ThirdStepActivity.VIEW_MODE_CONFIRM;
import static com.kora.android.presentation.ui.registration.step3.ThirdStepActivity.VIEW_MODE_ENTER;

@ConfigPersistent
public class ThirdStepPresenter  extends BasePresenter<ThirdStepView> {

    private final RegistrationPrefHelper mRegistrationPrefHelper;

    private String mViewMode;
    private String mPinCode;

    @Inject
    public ThirdStepPresenter(final RegistrationPrefHelper registrationPrefHelper) {
        mRegistrationPrefHelper = registrationPrefHelper;
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
                mRegistrationPrefHelper.storePin(pinCode);
                getView().showNextScreen();
                break;
        }
    }

    public String getViewMode() {
        return mViewMode;
    }

    public void setViewMode(String viewMode) {
        mViewMode = viewMode;
    }
}
