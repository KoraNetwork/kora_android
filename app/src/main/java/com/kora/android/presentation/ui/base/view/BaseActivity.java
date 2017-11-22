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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.kora.android.KoraApplication;
import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.di.component.ConfigPersistentComponent;
import com.kora.android.di.component.DaggerConfigPersistentComponent;
import com.kora.android.di.module.ActivityModule;
import com.kora.android.presentation.navigation.Navigation;
import com.kora.android.presentation.ui.base.custom.MultiDialog;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.BasePresenter;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseActivityView<P> {

    @Inject
    P mPresenter;

    Navigation mNavigation;

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
    public void showProgress(final boolean isVisible) {
        showProgress(isVisible, true, R.string.dialog_progress_loading);
    }

    @Override
    public void showProgress(final boolean isVisible,
                             final boolean isCancelable,
                             @StringRes final int stringId) {
        if (isVisible) {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                mProgressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog_Fullscreen );
                mProgressDialog.setCancelable(isCancelable);
            }
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.view_progress);
//            final TextView tvMessage = mProgressDialog.findViewById(R.id.text_view_message);
//            tvMessage.setText(stringId);
            final LottieAnimationView avLoading = mProgressDialog.findViewById(R.id.animation_view_loading);
//            avLoading.setImageAssetsFolder("images/");
            avLoading.setAnimation("animation_loading.json");
            avLoading.loop(true);
            avLoading.playAnimation();
            if (isCancelable) {
                final RelativeLayout rlContainer = mProgressDialog.findViewById(R.id.relative_layout_container);
                rlContainer.setOnClickListener(v -> mProgressDialog.dismiss());
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
    public void showError(String error) {
        showDialogMessage(R.string.error_title, error);
    }

    @Override
    public void showError(@StringRes final  int stringId) {
        showDialogMessage(R.string.error_title, stringId);
    }

    @Override
    public void showDialogMessage(@StringRes int title,
                                  final String message) {

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
    public int getFragmentContainer() {
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

    @Inject
    public void setNavigation(@Named("simple") final Navigation navigation) {
        mNavigation = navigation;
    }

    public Navigation getNavigator() {
        return mNavigation;
    }
}