package com.kora.android.presentation.ui.base.view;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.presenter.Presenter;

public interface BaseActivityView<P extends Presenter> extends BaseView<P> {

    void injectToComponent(final ActivityComponent activityComponent);



    void showToastMessage(@StringRes int textId);

    void showToastMessage(@NonNull final String text);

    void showDialogMessage(@StringRes int title,
                           final String message);

    void showDialogMessage(@StringRes int title,
                           @StringRes int message);

    void showDialogMessage(@StringRes int title,
                           @StringRes int message,
                           @StringRes int positiveButtonTextRes,
                           final Dialog.OnClickListener positiveOnClickListener,
                           @StringRes int negativeButtonTextRes,
                           final Dialog.OnClickListener negativeOnClickListener);

    void showDialogMessage(@StringRes int title,
                           @StringRes int message,
                           final Dialog.OnClickListener onClickListener);

    void showDialogMessage(final String title,
                           final String message,
                           final Dialog.OnClickListener onClickListener);



    void finishActivity();

    void switchFragment(final BaseFragment fragment, boolean addToBackStack);

    void addFragment(final BaseFragment fragment, boolean addToBackStack);

    void removeFragmentByTag(String tag);

    void clearBackStack();
}
