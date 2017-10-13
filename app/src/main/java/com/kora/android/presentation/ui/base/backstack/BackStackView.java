package com.kora.android.presentation.ui.base.backstack;

import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.base.view.BaseActivityView;

public interface BackStackView<P extends BasePresenter> extends BaseActivityView<P> {

    void selectHostById(final int hostId);
}
