package com.kora.android.presentation.ui.splash;

import com.kora.android.common.helper.SessionPrefHelper;
import com.kora.android.domain.base.DefaultCompletableObserver;
import com.kora.android.domain.usecase.splash.SplashUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class SplashPresenter extends BasePresenter<SplashView> {

    private final SessionPrefHelper mSessionPrefHelper;
    private final SplashUseCase mSplashUseCase;

    @Inject
    public SplashPresenter(final SplashUseCase splashUseCase,
                           final SessionPrefHelper sessionPrefHelper) {
        mSessionPrefHelper = sessionPrefHelper;
        mSplashUseCase = splashUseCase;
    }

    public void startSplashTask() {
        addDisposable(mSplashUseCase.execute(new SplashSubscriber()));
    }

    private class SplashSubscriber extends DefaultCompletableObserver {

        @Override
        public void onComplete() {
            if (!isViewAttached()) return;
            getView().showNextScreen(mSessionPrefHelper.getSessionToken().isEmpty());
        }
    }
}