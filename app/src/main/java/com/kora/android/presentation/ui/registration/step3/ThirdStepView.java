package com.kora.android.presentation.ui.registration.step3;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface ThirdStepView extends BaseView<ThirdStepPresenter> {

    void showEmptyPinCode();
    void showTooShortPinCode();

    void showAnotherMode();

    void showPinCodeDoesNotMatch();

    void showNextScreen();
}
