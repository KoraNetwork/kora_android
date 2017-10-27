package com.kora.android.presentation.ui.main.fragments.borrow.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.main.fragments.borrow.BorrowMainPresenter;
import com.kora.android.presentation.ui.main.fragments.borrow.BorrowMainView;

import butterknife.BindView;

public class BorrowFragment extends BaseFragment<BorrowMainPresenter> implements BorrowMainView {

    @BindView(R.id.text) TextView mTextView;

    public static BaseFragment getNewInstance(final BorrowType borrowType) {
        final Bundle args = new Bundle();
        args.putSerializable(Keys.Args.ARG_BORROW_TYPE, borrowType);
        final BorrowFragment borrowFragment = new BorrowFragment();
        borrowFragment.setArguments(args);
        return borrowFragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_borrow;
    }

    @Override
    public void injectToComponent(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        initArguments();

    }

    private void initArguments() {
        final Bundle arguments = getArguments();
        if (arguments == null) return;
        BorrowType borrowType = (BorrowType) arguments.getSerializable(Keys.Args.ARG_BORROW_TYPE);
        mTextView.setText(borrowType.name());
    }
}
