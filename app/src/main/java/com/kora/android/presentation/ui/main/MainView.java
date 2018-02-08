package com.kora.android.presentation.ui.main;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface MainView extends BaseView<MainPresenter> {

    void onUserDataLoaded(final UserEntity userEntity);

    void showEmailConfirmed(final UserEntity userEntity);

    void showLoginScreen();
}