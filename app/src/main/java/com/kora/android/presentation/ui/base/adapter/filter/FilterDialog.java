package com.kora.android.presentation.ui.base.adapter.filter;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class FilterDialog<T extends FilterModel> extends BottomSheetDialogFragment {

    @Nullable
    protected OnFilterListener<T> mOnFilterListener;

    Unbinder mUnbinder;

    public void show(FragmentManager manager) {
        show(manager, this.getClass().getName());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        initArguments(savedInstanceState);
        return dialog;
    }

    protected abstract void initArguments(Bundle savedInstanceState);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {

        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        dialog.setContentView(contentView);

        mUnbinder = ButterKnife.bind(this, contentView);

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            BottomSheetBehavior b = (BottomSheetBehavior) behavior;
            b.setSkipCollapsed(true);

            layoutParams.setBehavior(b);
        }

        onViewReady();
    }

    protected abstract void onViewReady();

    public void setOnFilterListener(@Nullable OnFilterListener<T> onFilterListener) {
        mOnFilterListener = onFilterListener;
    }

    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
