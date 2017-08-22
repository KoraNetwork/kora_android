package com.kora.android.presentation.ui.base.presenter;

import com.kora.android.presentation.ui.base.view.BaseView;

public interface Presenter<V extends BaseView> {

    void attachView(final V view);

    V getView();

    boolean isViewAttached();

    void detachView();
}
