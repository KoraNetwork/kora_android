package com.kora.android.presentation.ui.main.fragments.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;

import butterknife.BindView;

public class HomeFragment extends StackFragment<HomePresenter> implements HomeView {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static BaseFragment getNewInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void injectToComponent(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {

    }
}