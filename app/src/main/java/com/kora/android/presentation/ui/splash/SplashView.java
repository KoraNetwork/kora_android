package com.kora.android.presentation.ui.splash;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface SplashView extends BaseView<SplashPresenter> {

    void showNextScreen(boolean isSessionTokenEmpty);
}
