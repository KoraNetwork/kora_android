package com.kora.android.presentation.ui.main;

import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kora.android.R;
import com.kora.android.common.utils.CommonUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.DepositWithdrawRole;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.service.BaseService;
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

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.frame_layout) FrameLayout mFrameLayout;

    private int mSelectedItemPosition = TAB_HOME_POSITION;

    private ImageView mUserAvatar;
    private TextView mUserName;
    private TextView mUserEmail;

    private Bundle mSavedInstanceState;
    private HomeFragment mHomeFragment;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return getLaunchIntent(baseActivity, TAB_HOME_POSITION);
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final int tabId) {
        final Intent intent = new Intent(baseActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_CURRENT_TAB, tabId);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseService baseService) {
        final Intent intent = new Intent(baseService, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_CURRENT_TAB, TAB_HOME_POSITION);
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
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        getPresenter().loadUserData(false);
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void onUserDataLoaded(final UserEntity userEntity) {
        setupNavigationView();

        initArguments(mSavedInstanceState);

        startWalletsService(userEntity);

        showUserData(userEntity);
    }

    private void setupNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        final NavigationMenuView navigationMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
        navigationMenuView.addItemDecoration(new DividerItemDecoration(this, R.drawable.list_divider_transparent));

        final View headerView = mNavigationView.getHeaderView(0);
        mUserAvatar = headerView.findViewById(R.id.avatar_image);
        mUserName = headerView.findViewById(R.id.user_name);
        mUserEmail = headerView.findViewById(R.id.user_email);
        mUserAvatar.setOnClickListener(v -> {
            if (mSelectedItemPosition != TAB_USER_PROFILE_POSITION)
                getNavigator().showFragment(rootTabFragment(TAB_USER_PROFILE_POSITION), TAB_USER_PROFILE_POSITION);
            selectHostById(TAB_USER_PROFILE_POSITION);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    private void initArguments(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return;
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_CURRENT_TAB)) {
                final int tabId = getIntent().getIntExtra(EXTRA_CURRENT_TAB, TAB_HOME_POSITION);
                getNavigator().showFragment(rootTabFragment(tabId), tabId);
                selectHostById(tabId);
            }
        }
    }

    private void startWalletsService(final UserEntity userEntity) {
        if (userEntity.hasIdentity() && userEntity.isMoneySent()) return;
        if (CommonUtils.isServiceRunning(this, CreateWalletsService.class)) return;
        final Intent intent = CreateWalletsService.getLaunchIntent(this);
        startService(intent);
    }

    public void showUserData(final UserEntity userEntity) {
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

    @NonNull
    private BaseFragment rootTabFragment(final int tabId) {
        switch (tabId) {
            case TAB_HOME_POSITION:
                mHomeFragment = (HomeFragment) HomeFragment.getNewInstance();
                return mHomeFragment;
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
            case R.id.nav_user_profile:
                position = TAB_USER_PROFILE_POSITION;
                break;

            case R.id.nav_send_money:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
                position = TAB_SEND_MONEY_POSITION;
                break;
            case R.id.nav_requests_money:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
                position = TAB_REQUEST_MONEY_POSITION;
                break;
            case R.id.nav_borrow_money:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
                position = TAB_BORROW_MONEY_POSITION;
                break;
            case R.id.nav_deposit:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
                position = TAB_DEPOSIT_POSITION;
                break;
            case R.id.nav_withdrawal:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
                position = TAB_WITHDRAWAL_POSITION;
                break;
            case R.id.nav_transactions_history:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
                position = TAB_TRANSACTIONS_HISTORY_POSITION;
                break;
            case R.id.nav_send_a_feedback:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
                position = TAB_SEND_A_FEEDBACK_POSITION;
                break;
            case R.id.nav_agent_deposit:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
                position = TAB_AGENT_DEPOSIT_POSITION;
                break;
            case R.id.nav_agent_withdrawal:
                if (!getPresenter().getUserEntity().isEmailConfirmed()) {
                    showEmailConfirmationDialog();
                    return false;
                }
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
        if (position != mSelectedItemPosition) {
            getNavigator().showFragment(rootTabFragment(position), position);
        }
        mSelectedItemPosition = position;

        return true;
    }

    public void showEmailConfirmationDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_email_confirmation);
        final TextView tvPositive = alertDialog.findViewById(R.id.text_view_positive);
        if (tvPositive != null)
            tvPositive.setOnClickListener(v -> {
                alertDialog.dismiss();
                loadUserDataFromNetwork();
            });
        final TextView tvNegative = alertDialog.findViewById(R.id.text_view_negative);
        if (tvNegative != null)
            tvNegative.setOnClickListener(v ->
                    alertDialog.dismiss());
    }

    public void loadUserDataFromNetwork() {
        getPresenter().loadUserData(true);
    }

    @Override
    public void showEmailConfirmed(final UserEntity userEntity) {
        if (!userEntity.isEmailConfirmed()) {
            showEmailConfirmationDialog();
        } else {
            if (mHomeFragment != null)
                mHomeFragment.onEmailConfirmed(userEntity);
        }
    }

    @Override
    public void onClick(final View view) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
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
}