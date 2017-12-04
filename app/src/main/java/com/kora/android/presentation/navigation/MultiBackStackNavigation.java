package com.kora.android.presentation.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface MultiBackStackNavigation extends Navigation {

    int NO_HOST_ID = -1;

    void restoreState(final Bundle savedState);

    void saveState(final Bundle outState);

    void showFragment(final BaseFragment fragment,
                      final int hostId);

    void showFragment(final BaseFragment fragment,
                      final int hostId,
                      final boolean addToBackStack);
    int hostId();

    BaseFragment getCurrentFragment();

    @Nullable
    BaseFragment getFragmentByHostId(final int hostId);

    boolean handleBack(final BaseView baseView);

    boolean clearBackStack(final int hostId);

    void destroy();
}
