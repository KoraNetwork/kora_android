package com.kora.android.presentation.ui.login;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface LoginView extends BaseView<LoginPresenter> {

    void showEmptyIdentifier();
    void showIncorrectIdentifier();

    void showEmptyPassword();

    void showNextScreen();
}
