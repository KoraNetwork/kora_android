package com.kora.android.presentation.ui.send.enter_pin;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface EnterPinView extends BaseView<EnterPinPresenter> {

    void showEmptyPinCode();
    void showTooShortPinCode();

    void showNextScreen();
}
