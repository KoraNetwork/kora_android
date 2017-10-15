package com.kora.android.presentation.ui.main.fragments.transactions;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;

import butterknife.BindView;

public class TransactionsFragment extends StackFragment<TransactionsPresenter> implements TransactionsView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static BaseFragment getNewInstance() {
        return new TransactionsFragment();
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_transactions;
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {

    }
}
