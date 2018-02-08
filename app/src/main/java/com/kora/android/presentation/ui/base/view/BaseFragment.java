package com.kora.android.presentation.ui.base.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kora.android.KoraApplication;
import com.kora.android.R;
import com.kora.android.di.component.ConfigPersistentComponent;
import com.kora.android.di.component.DaggerConfigPersistentComponent;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.di.module.FragmentModule;
import com.kora.android.presentation.navigation.Navigation;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.presenter.Presenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends Presenter> extends Fragment implements BaseFragmentView<P> {

    @Inject
    P mPresenter;

    private FragmentComponent mFragmentComponent;
    private Unbinder mUnbinder;
    protected Context mContext;

    public BaseFragment() {

    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        createFragmentComponent();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutResource(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        injectToComponent(mFragmentComponent);
        mContext = getActivity();
        if (mPresenter != null)
            mPresenter.attachView(this);
        onViewReady(savedInstanceState);
    }

    protected void createFragmentComponent() {
        final ConfigPersistentComponent configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(KoraApplication.get(getActivity()).getComponent())
                .build();
        mFragmentComponent = configPersistentComponent.fragmentComponent(new FragmentModule(this));
    }

    protected abstract void onViewReady(final Bundle savedInstanceState);

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.detachView();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public Navigation getNavigator() {
        return ((BaseActivity) getActivity()).getNavigator();
    }

    @Override
    public void showProgress(boolean showProgress) {
        getBaseActivity().showProgress(showProgress);
    }

    @Override
    public void showProgress(boolean isVisible, boolean cancelable, @StringRes int stringId) {
        getBaseActivity().showProgress(isVisible, cancelable, stringId);
    }

    @Override
    public void showError(String error) {
        getBaseActivity().showError(error);
    }

    @Override
    public void showError(@StringRes final  int stringId) {
        showDialogMessage(R.string.error_title, stringId);
    }

    @Override
    public void showErrorWithRetry(RetryAction retryAction) {
        getBaseActivity().showErrorWithRetry(retryAction);
    }

    @Override
    public void showToastMessage(@NonNull String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastMessage(@StringRes int textId) {
        Toast.makeText(mContext, textId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialogMessage(@StringRes int title,
                                  @StringRes int message) {
        getBaseActivity().showDialogMessage(title, message);
    }

    @Override
    public void showDialogMessage(@StringRes int title,
                                  @StringRes int message,
                                  @StringRes int positiveButtonTextRes,
                                  final DialogInterface.OnClickListener positiveOnClickListener,
                                  @StringRes int negativeButtonTextRes,
                                  final DialogInterface.OnClickListener negativeOnClickListener) {
        getBaseActivity().showDialogMessage(title, message, positiveButtonTextRes, positiveOnClickListener, negativeButtonTextRes, negativeOnClickListener);
    }

    @Override
    public void showDialogMessage(@StringRes int title,
                                  @StringRes int message,
                                  final DialogInterface.OnClickListener onClickListener) {
        getBaseActivity().showDialogMessage(title, message, onClickListener);
    }

    @Override
    public void showDialogMessage(final String title,
                                  final String message,
                                  final DialogInterface.OnClickListener onClickListener) {
        getBaseActivity().showDialogMessage(title, message, onClickListener);
    }
}
