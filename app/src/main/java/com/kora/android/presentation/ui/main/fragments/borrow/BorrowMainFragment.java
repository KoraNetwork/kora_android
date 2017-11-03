package com.kora.android.presentation.ui.main.fragments.borrow;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.borrow.BorrowMoneyActivity;
import com.kora.android.presentation.ui.common.add_contact.GetContactActivity;
import com.kora.android.presentation.ui.main.fragments.borrow.adapter.BorrowPageAdapter;
import com.kora.android.presentation.ui.main.fragments.borrow.fragment.BorrowFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.kora.android.common.Keys.Extras.EXTRA_BORROW;
import static com.kora.android.common.Keys.Extras.EXTRA_USER;

public class BorrowMainFragment extends StackFragment<BorrowMainPresenter> implements BorrowMainView {

    public static final int REQUEST_GET_LENDER = 555;
    public static final int REQUEST_CREATE_BORROW_REQUEST = 552;

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

        mViewPager.setCurrentItem(0, false);
    }

    @OnClick(R.id.floating_button_create_borrow)
    public void onClickCreateBorrow() {
        startActivityForResult(
                GetContactActivity.getLaunchIntent(getBaseActivity(), getString(R.string.borrow_borrow_money)),
                REQUEST_GET_LENDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_GET_LENDER:
                UserEntity entity = data.getParcelableExtra(EXTRA_USER);
                startActivityForResult(
                        BorrowMoneyActivity.getLaunchIntent(getBaseActivity(), entity),
                        REQUEST_CREATE_BORROW_REQUEST);
                break;
            case REQUEST_CREATE_BORROW_REQUEST:
                BorrowEntity borrowRequest = data.getParcelableExtra(EXTRA_BORROW);
                BorrowFragment fragment = (BorrowFragment) mViewPagerAdapter.getItem(BorrowPageAdapter.POSITION_REQUEST);
                fragment.addRequest(borrowRequest);
                break;
        }
    }
}
