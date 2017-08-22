package com.kora.android.presentation.ui.splash;

import com.kora.android.data.local.AuthPrefHelper;
import com.kora.android.domain.base.DefaultCompletableObserver;
import com.kora.android.domain.usecase.splash.SplashUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class SplashPresenter extends BasePresenter<SplashView> {

    private final AuthPrefHelper mAuthPrefHelper;
    private final SplashUseCase mSplashUseCase;

    @Inject
    public SplashPresenter(final SplashUseCase splashUseCase,
                           final AuthPrefHelper authPrefHelper) {
        mAuthPrefHelper = authPrefHelper;
        mSplashUseCase = splashUseCase;
    }

    public void startSplashTask() {
        addDisposable(mSplashUseCase.execute(new SplashSubscriber()));
    }

    private class SplashSubscriber extends DefaultCompletableObserver {

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showNextScreen(mAuthPrefHelper.getSessionToken().isEmpty());
        }
    }
}