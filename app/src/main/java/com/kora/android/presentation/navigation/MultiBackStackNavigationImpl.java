package com.kora.android.presentation.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.util.Log;

import com.kora.android.di.annotation.PerActivity;
import com.kora.android.presentation.navigation.backstack.BackStackEntry;
import com.kora.android.presentation.navigation.backstack.BackStackManager;
import com.kora.android.presentation.ui.base.backstack.BackStackView;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.base.view.BaseView;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class MultiBackStackNavigationImpl implements MultiBackStackNavigation {

    private static final String STATE_BACK_STACK_MANAGER = "back_stack_manager";
    private static final String STATE_HOST_ID = "host_id";
    private final BackStackView mBackStackView;
    private final BaseActivity mBaseActivity;
    private final Navigation mDefaultNavigation;

    private BackStackManager mBackStackManager;
    private BaseFragment mCurrentFragment;
    private int mCurrentHostId = NO_HOST_ID;

    @Inject
    public MultiBackStackNavigationImpl(final @Named("simple") Navigation navigation,
                                        final BaseActivity context,
                                        final BackStackView backStackView) {
        mBaseActivity = context;
        mDefaultNavigation = navigation;
        mBackStackView = backStackView;
        mBackStackManager = new BackStackManager();
    }

    @Override
    public void restoreState(final Bundle savedState) {
        mCurrentHostId = savedState.getInt(STATE_HOST_ID);
        mBackStackManager.restoreState(savedState.getParcelable(STATE_BACK_STACK_MANAGER));
        mCurrentFragment = (BaseFragment) mBaseActivity.getSupportFragmentManager()
                .findFragmentById(mBaseActivity.getFragmentContainer());
        mBackStackView.selectHostById(mCurrentHostId);
    }

    @Override
    public void saveState(final Bundle outState) {
        outState.putParcelable(STATE_BACK_STACK_MANAGER, mBackStackManager.saveState());
        outState.putInt(STATE_HOST_ID, mCurrentHostId);
    }

    @Override
    public int hostId() {
        return mCurrentHostId;
    }

    @Override
    public BaseFragment getCurrentFragment() {
        return (BaseFragment) mBaseActivity.getSupportFragmentManager()
                .findFragmentById(mBaseActivity.getFragmentContainer());
    }

    @Nullable
    @Override
    public BaseFragment getFragmentByHostId(final int hostId) {
        return (BaseFragment) mBaseActivity.getSupportFragmentManager()
                .findFragmentById(hostId);
    }

    protected boolean pushFragmentToBackStack(final int hostId,
                                              @NonNull final BaseFragment fragment) {
        try {
            BackStackEntry entry = BackStackEntry.create(mBaseActivity.getSupportFragmentManager(), fragment);
            mBackStackManager.push(hostId, entry);
            return true;
        } catch (Exception e) {
            Log.e("MultiBackStack", "Failed to addItems fragment to back stack", e);
            return false;
        }
    }

    @Nullable
    protected Fragment popFragmentFromBackStack(final int hostId) {
        BackStackEntry entry = mBackStackManager.pop(hostId);
        return entry != null ? entry.toFragment(mBaseActivity) : null;
    }

    @Nullable
    protected Pair<Integer, Fragment> popFragmentFromBackStack() {
        Pair<Integer, BackStackEntry> pair = mBackStackManager.pop();
        return pair != null ? Pair.create(pair.first, pair.second.toFragment(mBaseActivity)) : null;
    }

    /**
     * @return false if back stack is missing.
     */
    @Override
    public boolean clearBackStack(final int hostId) {
        return mBackStackManager.clear(hostId);
    }

    private void replaceFragment(@NonNull final Fragment fragment) {
        final FragmentManager fm = mBaseActivity.getSupportFragmentManager();
        final FragmentTransaction tr = fm.beginTransaction();
        tr.replace(mBaseActivity.getFragmentContainer(), fragment);
        tr.commitAllowingStateLoss();
        mCurrentFragment = (BaseFragment) fragment;
    }

    @Override
    public void showFragment(final BaseFragment fragment) {
        showFragment(fragment, mCurrentHostId);
    }

    @Override
    public void startActivity(final Intent launchIntent) {
        mDefaultNavigation.startActivity(launchIntent);
    }

    @Override
    public void startActivity(final Intent launchIntent, final ActivityOptionsCompat activityOptions) {
        mDefaultNavigation.startActivity(launchIntent, activityOptions);
    }

    @Override
    public void startActivity(final Intent launchIntent, final boolean finishCurrent) {
        mDefaultNavigation.startActivity(launchIntent, finishCurrent);
    }

    @Override
    public void startActivityForResult(final Intent launchIntent, final int requestCode) {
        mDefaultNavigation.startActivityForResult(launchIntent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent launchIntent, ActivityOptionsCompat activityOptions, int requestCode) {
        mDefaultNavigation.startActivityForResult(launchIntent, activityOptions, requestCode);
    }

    @Override
    public void startActivityForResult(final BaseView baseView, final Intent launchIntent, final int requestCode) {
        mDefaultNavigation.startActivityForResult(baseView, launchIntent, requestCode);
    }

    @Override
    public void startActivityForResult(BaseView baseView, Intent launchIntent, ActivityOptionsCompat activityOptions, int requestCode) {

    }

    @Override
    public void showFragment(final BaseFragment fragment, final int hostId) {
        showFragment(fragment, hostId, true);
    }

    @Override
    public void showFragment(final BaseFragment fragment, final int hostId, final boolean addToBackStack) {
        if (mCurrentFragment != null) {
            pushFragmentToBackStack(mCurrentHostId, mCurrentFragment);
        }
        mCurrentHostId = hostId;
        popFragmentFromBackStack(mCurrentHostId);
        replaceFragment(fragment);
    }

    /**
     * @param baseView
     * @return true if back event handled, if false returned the activiy/fragment
     * should handle the back press
     */
    @Override
    public boolean handleBack(final BaseView baseView) {
         final Pair<Integer, Fragment> pair = popFragmentFromBackStack();
        if (pair != null) {
            backTo(pair.first, pair.second);
            return true;
        } else {
            return false;
        }
    }

    private void backTo(final int hostId, @NonNull final Fragment fragment) {
        if (hostId != mCurrentHostId) {
            mCurrentHostId = hostId;
            mBackStackView.selectHostById(mCurrentHostId);
        }
        replaceFragment(fragment);
        mBaseActivity.getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void destroy() {
        mBackStackManager = null;
        mCurrentFragment = null;
    }
}
