package com.kora.android.presentation.ui.main.fragments.request;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kora.android.R;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class RequestFragment extends StackFragment<RequestPresenter> implements RequestView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static BaseFragment getNewInstance() {
        return new RequestFragment();
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_request;
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {

    }

    @OnClick(R.id.floating_button_create_request)
    public void onClickCreateRequest() {

    }
}
