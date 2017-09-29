package com.kora.android.presentation.ui.base.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.kora.android.KoraApplication;
import com.kora.android.R;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.injection.component.ConfigPersistentComponent;
import com.kora.android.injection.component.DaggerConfigPersistentComponent;
import com.kora.android.injection.module.ActivityModule;
import com.kora.android.presentation.ui.base.custom.MultiDialog;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseActivityView<P> {

    @Inject
    P mPresenter;

    private ActivityComponent mActivityComponent;
    private ProgressDialog mProgressDialog;
    private MultiDialog mMultiDialog;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        mUnbinder = ButterKnife.bind(this);

        createActivityComponent();
        injectToComponent(mActivityComponent);

        if (mPresenter != null)
            mPresenter.attachView(this);

        onViewReady(savedInstanceState);
    }

    protected void createActivityComponent() {
        final ConfigPersistentComponent configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(KoraApplication.get(this).getComponent())
                .build();
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    protected abstract void onViewReady(final Bundle savedInstanceState);

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null)
            mUnbinder.unbind();
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showProgress(boolean showProgress) {
        if (showProgress) {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog);
                mProgressDialog.setMessage(getString(R.string.dialog_loading));
                mProgressDialog.show();
            } else {
                mProgressDialog.show();
            }
        } else if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void showErrorWithRetry(RetryAction retryAction) {
        showProgress(false);

        if (mMultiDialog != null && mMultiDialog.isShowing()) return;

        mMultiDialog = new MultiDialog.DialogBuilder()
                .setTitle(R.string.dialog_retry_alert_title)
                .setMessage(R.string.dialog_retry_message)
                .setPositiveButton(R.string.dialog_retry_negative_btn, (dialogInterface, i) ->
                        dialogInterface.dismiss())
                .setNegativeButton(R.string.dialog_retry_positive_btn, (dialogInterface, i) -> {
                    retryAction.onClick(null);
                    dialogInterface.dismiss();
                })
                .build(this);
        mMultiDialog.show();
    }

    @Override
    public void showToastMessage(@StringRes int textId) {
        Toast.makeText(this, textId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMessage(@NonNull String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialogMessage(@StringRes int title,
                                  @StringRes int message) {

        if (mMultiDialog != null && mMultiDialog.isShowing()) return;
        mMultiDialog = new MultiDialog.DialogBuilder()
                .setTitle(title)
                .setMessage(message)
                .setCloseButton(R.string.dialog_action_ok, (dialogInterface, i) ->
                        dialogInterface.cancel())
                .build(this);
        mMultiDialog.show();
    }

    @Override
    public void showDialogMessage(@StringRes int title,
                                  @StringRes int message,
                                  @StringRes int positiveButtonTextRes,
                                  final DialogInterface.OnClickListener positiveOnClickListener,
                                  @StringRes int negativeButtonTextRes,
                                  final DialogInterface.OnClickListener negativeOnClickListener) {

        if (mMultiDialog != null && mMultiDialog.isShowing()) return;
        mMultiDialog = new MultiDialog.DialogBuilder()
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonTextRes, positiveOnClickListener)
                .setNegativeButton(negativeButtonTextRes, negativeOnClickListener)
                .build(this);
        mMultiDialog.show();
    }

    @Override
    public void showDialogMessage(@StringRes int title,
                                  @StringRes int message,
                                  final DialogInterface.OnClickListener onClickListener) {

        if (mMultiDialog != null && mMultiDialog.isShowing()) return;
        mMultiDialog = new MultiDialog.DialogBuilder()
                .setTitle(title)
                .setMessage(message)
                .setCloseButton(R.string.dialog_action_ok, onClickListener)
                .build(this);
        mMultiDialog.show();
    }

    @Override
    public void showDialogMessage(final String title,
                                  final String message,
                                  final DialogInterface.OnClickListener onClickListener) {

        if (mMultiDialog != null && mMultiDialog.isShowing()) return;
        mMultiDialog = new MultiDialog.DialogBuilder()
                .setTitle(title)
                .setMessage(message)
                .setCloseButton(R.string.dialog_action_ok, onClickListener)
                .build(this);
        mMultiDialog.show();
    }

    public void setToolbar(final Toolbar toolbar, boolean asHomeEnabled) {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(asHomeEnabled);
    }

    public void setToolbar(final Toolbar toolbar, @DrawableRes int resId) {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(resId);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @IdRes
    protected int getFragmentContainer() {
        return 0;
    }

    public FragmentManager getBaseFragmentManager() {
        return super.getSupportFragmentManager();
    }

    @Override
    public void switchFragment(final BaseFragment fragment, boolean addToBackStack) {
//        FragmentTransaction fragmentTransaction = getBaseFragmentManager().beginTransaction();
//
//        if (addToBackStack) {
//            fragmentTransaction.addToBackStack(fragment.getClass().getName());
//        }
//        fragmentTransaction.replace(getFragmentContainer(), fragment, fragment.getClass().getName());
//
//        fragmentTransaction.commit();
//        getBaseFragmentManager().executePendingTransactions();
//        ViewUtil.hideKeyboard(this);

        final int fragmentContainerId = getFragmentContainer();

        if (fragmentContainerId == 0)
            throw new RuntimeException("Layout resource id should be provided. Check the method description");

        final FragmentManager fragmentManager = getSupportFragmentManager();

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentManager.getFragments() == null || fragmentManager.getFragments().size() == 0) {
            fragmentTransaction.replace(fragmentContainerId, fragment)
                    .commit();
        } else {
            if (addToBackStack) {
                fragmentTransaction
                        .replace(fragmentContainerId, fragment, fragment.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            } else {
                fragmentTransaction
                        .replace(fragmentContainerId, fragment, fragment.getClass().getSimpleName())
                        .commit();
            }

        }
    }

    @Override
    public void addFragment(BaseFragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getBaseFragmentManager().beginTransaction();

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }
        fragmentTransaction.add(getFragmentContainer(), fragment, fragment.getClass().getName());

        fragmentTransaction.commit();
        getBaseFragmentManager().executePendingTransactions();
//        ViewUtil.hideKeyboard(this);
    }

    public void clearBackStack() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void removeFragmentByTag(String tag) {
        Fragment fragment = getBaseFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            getBaseFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}