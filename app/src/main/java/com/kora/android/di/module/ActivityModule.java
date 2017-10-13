package com.kora.android.di.module;

import com.kora.android.common.exceptions.NotImplementedException;
import com.kora.android.di.annotation.PerActivity;
import com.kora.android.presentation.navigation.MultiBackStackNavigationImpl;
import com.kora.android.presentation.navigation.Navigation;
import com.kora.android.presentation.navigation.NavigationImpl;
import com.kora.android.presentation.ui.base.backstack.BackStackView;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final BaseActivity mActivity;

    public ActivityModule(final BaseActivity activity) {
        mActivity = activity;
    }

    @Provides
    BaseActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @Named("simple")
    Navigation provideNavigation(final BaseActivity baseActivity) {
        return new NavigationImpl(baseActivity);
    }

    @PerActivity
    @Provides
    @Named("multistack")
    Navigation provideMultiStackNavigation(@Named("simple")final Navigation navigation,
                                           final BaseActivity baseActivity) {
        if (!(baseActivity instanceof BackStackView))
            throw new NotImplementedException("To use multibackstack navigator, your component should extend BackStackView", BackStackView.class);
        return new MultiBackStackNavigationImpl(navigation, baseActivity, (BackStackView) baseActivity);
    }
}