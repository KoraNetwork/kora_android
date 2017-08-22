package com.kora.android.presentation.ui.main;

import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainView> {

    @Inject
    public MainPresenter() {

    }
}
