package com.kora.android.presentation.ui.login;

import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.registration.RegistrationView;

import javax.inject.Inject;

@ConfigPersistent
public class LoginPresenter extends BasePresenter<RegistrationView> {

    @Inject
    public LoginPresenter() {

    }
}
