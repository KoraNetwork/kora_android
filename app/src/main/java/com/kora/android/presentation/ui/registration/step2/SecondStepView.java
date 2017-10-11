package com.kora.android.presentation.ui.registration.step2;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface SecondStepView extends BaseView<SecondStepPresenter> {

    void showConfirmationCode(final String confirmationCode);

    void showEmptyConfirmationCode();
    void showIncorrectConfirmationCode();

    void showNextScreen();
    void showServerError();
}
