package com.kora.android.presentation.ui.registration.step1;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface FirstStepView extends BaseView<FirstStepPresenter> {

    void showPhoneNumber(final String phoneNumber);

    void showEmptyPhoneNumber();
    void showIncorrectPhoneNumber();

    void showNextScreen();
}
