package com.kora.android.presentation.ui.base.backstack;

import android.os.Bundle;

import com.kora.android.presentation.navigation.MultiBackStackNavigation;
import com.kora.android.presentation.navigation.Navigation;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import javax.inject.Inject;
import javax.inject.Named;

public abstract class BackStackActivity<P extends BasePresenter> extends BaseActivity<P> implements BackStackView<P> {

    @Inject
    @Override
    public void setNavigation(@Named("multistack") final Navigation navigation) {
        super.setNavigation(navigation);
    }

    @Override
    public MultiBackStackNavigation getNavigator() {
        return (MultiBackStackNavigation) super.getNavigator();
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getNavigator().restoreState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        getNavigator().saveState(outState);
    }

    @Override
    public void onDestroy() {
        getNavigator().destroy();
        super.onDestroy();
    }

}
