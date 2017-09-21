package com.kora.android.presentation.ui.registration.step_1;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface FirstStepView extends BaseView<FirstStepPresenter> {

    void showEmptyPhoneNumber();
    void showIncorrectPhoneNumber();
}
