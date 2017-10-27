package com.kora.android.presentation.ui.main.fragments.borrow;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.main.fragments.borrow.adapter.BorrowPageAdapter;

import butterknife.BindView;
import butterknife.OnClick;

public class BorrowMainFragment extends StackFragment<BorrowMainPresenter> implements BorrowMainView {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewpager) ViewPager mViewPager;

    private BorrowPageAdapter mViewPagerAdapter;

    public static BaseFragment getNewInstance() {
        return new BorrowMainFragment();
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_borrow_main;
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitle() {
        return R.string.borrow_borrow_money;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setupViewPager();
    }

    private void setupViewPager() {
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPagerAdapter = new BorrowPageAdapter(getChildFragmentManager(), getBaseActivity());
        mViewPager.setAdapter(mViewPagerAdapter);

//        boolean forUnsuccessful = getArguments().getBoolean(ARG_FOR_UNSUCCESSFUL, false);
//        mViewPager.setCurrentItem(forUnsuccessful ? 2 : 1, false);
        mViewPager.setCurrentItem(0, false);
    }

    @OnClick(R.id.floating_button_create_borrow)
    public void onClickCreateBorrow() {

    }
}
