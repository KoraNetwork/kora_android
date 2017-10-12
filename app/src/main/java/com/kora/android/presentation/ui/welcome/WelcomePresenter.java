package com.kora.android.presentation.ui.welcome;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class WelcomePresenter  extends BasePresenter<WelcomeView> {

    @Inject
    public WelcomePresenter() {

    }
}
