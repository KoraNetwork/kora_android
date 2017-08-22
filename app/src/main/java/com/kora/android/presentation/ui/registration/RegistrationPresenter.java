package com.kora.android.presentation.ui.registration;

import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
class RegistrationPresenter extends BasePresenter<RegistrationView> {

    @Inject
    public RegistrationPresenter() {

    }
}
