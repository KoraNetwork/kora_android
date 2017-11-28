package com.kora.android.presentation.ui.coming_soon;

import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.presentation.enums.ComingSoonType;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

@ConfigPersistent
public class ComingSoonPresenter extends BasePresenter<ComingSoonView> {

    private ComingSoonType mComingSoonType;

    @Inject
    public ComingSoonPresenter() {

    }

    public ComingSoonType getComingSoonType() {
        return mComingSoonType;
    }

    public void setComingSoonType(final ComingSoonType comingSoonType) {
        mComingSoonType = comingSoonType;
    }
}
