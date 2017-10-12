package com.kora.android.presentation.ui.home;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class HomePresenter extends BasePresenter<HomeView> {

    @Inject
    public HomePresenter() {

    }
}
