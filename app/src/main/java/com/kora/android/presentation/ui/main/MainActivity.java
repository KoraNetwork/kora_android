package com.kora.android.presentation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.DepositWithdrawRole;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.service.wallet.CreateWalletsService;
import com.kora.android.presentation.ui.base.backstack.BackStackActivity;
import com.kora.android.presentation.ui.base.custom.MultiDialog;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.login.LoginActivity;
import com.kora.android.presentation.ui.main.fragments.borrow.BorrowMainFragment;
import com.kora.android.presentation.ui.main.fragments.deposit_withdraw.DepositWithdrawFragment;
import com.kora.android.presentation.ui.main.fragments.home.HomeFragment;
import com.kora.android.presentation.ui.main.fragments.profile.ProfileFragment;
import com.kora.android.presentation.ui.main.fragments.request.RequestFragment;
import com.kora.android.presentation.ui.main.fragments.send.SendFragment;
import com.kora.android.presentation.ui.main.fragments.send_feedback.SendFeedbackFragment;
import com.kora.android.presentation.ui.main.fragments.transactions.TransactionsFragment;
import com.kora.android.views.DividerItemDecoration;

import butterknife.BindView;

import static com.kora.android.common.Keys.Extras.EXTRA_CURRENT_TAB;
import static com.kora.android.data.network.Constants.API_BASE_URL;

public class MainActivity extends BackStackActivity<MainPresenter> implements MainView,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final int TAB_INVALID_POSITION = -1;
    public static final int TAB_HOME_POSITION = 0;
    public static final int TAB_SEND_MONEY_POSITION = 1;
    public static final int TAB_REQUEST_MONEY_POSITION = 2;
    public static final int TAB_BORROW_MONEY_POSITION = 3;
    public static final int TAB_DEPOSIT_POSITION = 4;
    public static final int TAB_WITHDRAWAL_POSITION = 5;
    public static final int TAB_TRANSACTIONS_HISTORY_POSITION = 6;
    public static final int TAB_USER_PROFILE_POSITION = 7;
    public static final int TAB_SEND_A_FEEDBACK_POSITION = 8;
    public static final int HEADER_AGENT_FUNCTION_POSITION = 9;
    public static final int TAB_LOG_OUT = 10;

    public static final int TAB_AGENT_DEPOSIT_POSITION = 11;
    public static final int TAB_AGENT_WITHDRAW_POSITION = 12;
    public static final int TAB_AGENT_DEPOSIT_SUB_MENU_POSITION = 0;
    public static final int TAB_AGENT_WITHDRAW_SUB_MENU_POSITION = 1;

    @BindView(R.id.root_view) CoordinatorLayout mRootView;
    @BindView(R.id.content_layout) LinearLayout mContentLayout;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.left_nav_view_base) NavigationView mNavigationViewBase;
    @BindView(R.id.frame_layout) FrameLayout mFrameLayout;

    private ImageView mUserAvatar;
    private TextView mUserName;
    private TextView mUserEmail;

    private int mSelectedItemPosition = TAB_HOME_POSITION;
    private int mNotificationsCount = 0;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return getLaunchIntent(baseActivity, TAB_HOME_POSITION);
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final int tabId) {
        final Intent intent = new Intent(baseActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_CURRENT_TAB, tabId);
        return intent;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @IdRes
    public int getFragmentContainer() {
        return R.id.frame_layout;
    }

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        getPresenter().importKoraWallet();

        setupDrawer();
        getNavigator().showFragment(rootTabFragment(TAB_HOME_POSITION), TAB_HOME_POSITION);

        final View headerView = mNavigationView.getHeaderView(0);
        mUserAvatar = headerView.findViewById(R.id.avatar_image);
        mUserName = headerView.findViewById(R.id.user_name);
        mUserEmail = headerView.findViewById(R.id.user_email);
        mUserAvatar.setOnClickListener(v -> {
            final MenuItem item = mNavigationView.getMenu().getItem(TAB_USER_PROFILE_POSITION);
            onNavigationItemSelected(item);
        });

        if (savedInstanceState == null) {
            final MenuItem item = mNavigationView.getMenu().getItem(TAB_HOME_POSITION);
            mNavigationView.getMenu().performIdentifierAction(item.getItemId(), TAB_HOME_POSITION);
            item.setChecked(true);
        }

        initArgs();
    }

    private void initArgs() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_CURRENT_TAB)) {
                final int tabId = getIntent().getIntExtra(EXTRA_CURRENT_TAB, 0);
                final MenuItem item = mNavigationView.getMenu().getItem(tabId);
                mNavigationView.getMenu().performIdentifierAction(item.getItemId(), tabId);
                item.setChecked(true);
            }
        }
    }

    private void setupDrawer() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        NavigationMenuView navMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(this, R.drawable.list_divider_transparent));
    }

    @NonNull
    private BaseFragment rootTabFragment(final int tabId) {
        switch (tabId) {
            case TAB_HOME_POSITION:
                return HomeFragment.getNewInstance();
            case TAB_SEND_MONEY_POSITION:
                return SendFragment.getNewInstance();
            case TAB_REQUEST_MONEY_POSITION:
                return RequestFragment.getNewInstance();
            case TAB_BORROW_MONEY_POSITION:
                return BorrowMainFragment.getNewInstance();
            case TAB_DEPOSIT_POSITION:
                return DepositWithdrawFragment.getNewInstance(DepositWithdrawRole.DEPOSIT_USER);
            case TAB_WITHDRAWAL_POSITION:
                return DepositWithdrawFragment.getNewInstance(DepositWithdrawRole.WITHDRAW_USER);
            case TAB_TRANSACTIONS_HISTORY_POSITION:
                return TransactionsFragment.getNewInstance();
            case TAB_USER_PROFILE_POSITION:
                return ProfileFragment.getNewInstance();
            case TAB_SEND_A_FEEDBACK_POSITION:
                return SendFeedbackFragment.getNewInstance();
            case TAB_AGENT_DEPOSIT_POSITION:
                return DepositWithdrawFragment.getNewInstance(DepositWithdrawRole.DEPOSIT_AGENT);
            case TAB_AGENT_WITHDRAW_POSITION:
                return DepositWithdrawFragment.getNewInstance(DepositWithdrawRole.WITHDRAW_AGENT);

            default:
                throw new IllegalArgumentException("No such tab");
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            final boolean handled = getNavigator().handleBack(this);
            if (!handled) super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);

        int position = TAB_HOME_POSITION;

        switch (item.getItemId()) {
            case R.id.nav_home:
                position = TAB_HOME_POSITION;
                break;
            case R.id.nav_send_money:
                position = TAB_SEND_MONEY_POSITION;
                break;
            case R.id.nav_requests_money:
                position = TAB_REQUEST_MONEY_POSITION;
                break;
            case R.id.nav_borrow_money:
                position = TAB_BORROW_MONEY_POSITION;
                break;
            case R.id.nav_deposit:
                position = TAB_DEPOSIT_POSITION;
                break;
            case R.id.nav_withdrawal:
                position = TAB_WITHDRAWAL_POSITION;
                break;
            case R.id.nav_transactions_history:
                position = TAB_TRANSACTIONS_HISTORY_POSITION;
                break;
            case R.id.nav_user_profile:
                position = TAB_USER_PROFILE_POSITION;
                break;
            case R.id.nav_send_a_feedback:
                position = TAB_SEND_A_FEEDBACK_POSITION;
                break;
            case R.id.nav_agent_deposit:
                position = TAB_AGENT_DEPOSIT_POSITION;
                break;
            case R.id.nav_agent_withdrawal:
                position = TAB_AGENT_WITHDRAW_POSITION;
                break;
            case R.id.nav_log_out:
                position = TAB_LOG_OUT;
                new MultiDialog.DialogBuilder()
                        .setTitle(R.string.dialog_logout_title)
                        .setMessage(R.string.dialog_logout_message)
                        .setPositiveButton(R.string.dialog_logout_positive_btn, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            getPresenter().logout();
                        })
                        .setNegativeButton(R.string.dialog_logout_negative_btn, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        })
                        .build(this).show();
                break;
        }
        if (position == TAB_LOG_OUT) {
            return false;
        }
        if (position != mSelectedItemPosition)
            getNavigator().showFragment(rootTabFragment(position), position);

        mSelectedItemPosition = position;

        return true;
    }

    @Override
    public void onClick(final View v) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void showFragmentByPosition(int position) {
        final MenuItem item = mNavigationView.getMenu().getItem(0);
        mNavigationView.getMenu().performIdentifierAction(item.getItemId(), position);
        item.setChecked(true);
    }

    @Override
    public void showUserData(final UserEntity userEntity) {
//        if (!userEntity.hasIdentity()) {
            final Intent launchIntent = CreateWalletsService.getLaunchIntent(this);
            startService(launchIntent);
//        }

        mNavigationView.getMenu().setGroupVisible(R.id.nav_group_agent, userEntity.isAgent());
        if (!userEntity.isAgent()) {
            getNavigator().clearBackStack(TAB_AGENT_DEPOSIT_POSITION);
            getNavigator().clearBackStack(TAB_AGENT_WITHDRAW_POSITION);
        }

        Glide.with(this)
                .load(API_BASE_URL + userEntity.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this).load(R.drawable.ic_user_default))
                .into(mUserAvatar);
        mUserName.setText(userEntity.getLegalName());
        mUserEmail.setText(userEntity.getEmail());
    }

    @Override
    public void showLoginScreen() {
        startActivity(LoginActivity.getLaunchIntent(this));
    }

    @Override
    public void selectHostById(final int hostId) {
        MenuItem currentMenuItem;
        switch (mSelectedItemPosition) {
            case TAB_AGENT_DEPOSIT_POSITION:
                currentMenuItem = getAgentSubMenuItem(TAB_AGENT_DEPOSIT_SUB_MENU_POSITION);
                break;
            case TAB_AGENT_WITHDRAW_POSITION:
                currentMenuItem = getAgentSubMenuItem(TAB_AGENT_WITHDRAW_SUB_MENU_POSITION);
                break;
            default:
                currentMenuItem = mNavigationView.getMenu().getItem(mSelectedItemPosition);
                break;
        }
        currentMenuItem.setChecked(false);

        MenuItem nextMenuItem;
        switch (hostId) {
            case TAB_AGENT_DEPOSIT_POSITION:
                nextMenuItem = getAgentSubMenuItem(TAB_AGENT_DEPOSIT_SUB_MENU_POSITION);
                mSelectedItemPosition = TAB_AGENT_DEPOSIT_POSITION;
                break;
            case TAB_AGENT_WITHDRAW_POSITION:
                nextMenuItem = getAgentSubMenuItem(TAB_AGENT_WITHDRAW_SUB_MENU_POSITION);
                mSelectedItemPosition = TAB_AGENT_WITHDRAW_POSITION;
                break;
            default:
                nextMenuItem = mNavigationView.getMenu().getItem(hostId);
                mSelectedItemPosition = hostId;
                break;
        }
        nextMenuItem.setChecked(true);
    }

    private MenuItem getAgentSubMenuItem(final int subMenuItemPosition) {
        return mNavigationView
                .getMenu()
                .getItem(HEADER_AGENT_FUNCTION_POSITION)
                .getSubMenu()
                .getItem(subMenuItemPosition);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getNavigator().saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getNavigator().restoreState(savedInstanceState);
    }
}