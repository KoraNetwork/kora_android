package com.kora.android.presentation.ui.base.view;

import android.app.Dialog;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.ui.base.presenter.Presenter;

public interface BaseFragmentView<P extends Presenter> extends BaseView<P> {

    void injectToComponent(FragmentComponent fragmentComponent);



    void showToastMessage(@StringRes int textId);

    void showToastMessage(@NonNull final String text);



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

    void showDialogMessage(final String title, final String message,
                           final Dialog.OnClickListener onClickListener);
}
