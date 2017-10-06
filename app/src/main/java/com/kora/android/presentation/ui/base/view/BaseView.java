package com.kora.android.presentation.ui.base.view;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.Presenter;

public interface BaseView<P extends Presenter> {

    @LayoutRes
    int getLayoutResource();

    P getPresenter();

    void showProgress(final boolean visible);

    void showProgress(final boolean isVisible,
                      final boolean cancelable,
                      @StringRes final  int stringId);

    void showErrorWithRetry(final RetryAction retryAction);
}
