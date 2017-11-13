package com.kora.android.presentation.ui.base.custom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MultiDialog {

    final static int INVALID_RES_ID = -1;

    @BindView(R.id.text_title)
    TextView mTitleText;
    @BindView(R.id.text_view_message)
    TextView mMessageText;
    @BindView(R.id.text_btn_ok)
    TextView mCloseButton;
    @BindView(R.id.button_positive)
    TextView mPositiveButton;
    @BindView(R.id.button_negative)
    TextView mNegativeButton;
    @BindView(R.id.btns_container)
    LinearLayout btnsContainer;

    private final View mRootView;
    private final Dialog mDialog;
    private final Unbinder mUnbinder;

    private DialogInterface.OnClickListener mOnPositiveButtonClickListener;
    private DialogInterface.OnClickListener mOnNegativeButtonClickListener;
    private DialogInterface.OnClickListener mOnCloseButtonClickListener;
    private DialogInterface.OnDismissListener mOnDismissListener;

    public MultiDialog(final BaseActivity baseActivity) {
        mDialog = new Dialog(baseActivity);
        mRootView = LayoutInflater.from(baseActivity).inflate(R.layout.view_dialog, null);
        mUnbinder = ButterKnife.bind(this, mRootView);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mRootView);

        mDialog.setCancelable(true);
        mDialog.setOnDismissListener(dialog -> mUnbinder.unbind());
    }

    private void setTitle(final @StringRes int title) {
        if (title == INVALID_RES_ID) return;
        mTitleText.setText(title);
    }

    private void setTitle(final String title) {
        if (title == null) return;
        mTitleText.setText(title);
    }

    private void setMessage(final @StringRes int message) {
        if (message == INVALID_RES_ID) return;
        mMessageText.setText(message);
    }

    private void setMessage(final String message) {
        if (message == null) return;
        mMessageText.setText(message);
    }

    private void setPositiveButtonText(final String positiveButtonText) {
        if (positiveButtonText == null) return;
        mPositiveButton.setText(positiveButtonText);
    }

    private void setNegativeButtonText(final String negativeButtonText) {
        if (negativeButtonText == null) return;
        mNegativeButton.setText(negativeButtonText);
    }

    private void setCloseButtonText(final String closeButtonText) {
        if (closeButtonText == null) return;
        mCloseButton.setText(closeButtonText);
    }

    private void setPositiveButtonText(final @StringRes int positiveButtonTextRes) {
        if (positiveButtonTextRes == INVALID_RES_ID) return;
        mPositiveButton.setText(positiveButtonTextRes);
    }

    private void setNegativeButtonText(final @StringRes int negativeButtonTextRes) {
        if (negativeButtonTextRes == INVALID_RES_ID) return;
        mNegativeButton.setText(negativeButtonTextRes);
    }

    private void setCloseButtonText(final @StringRes int closeButtonTextRes) {
        if (closeButtonTextRes == INVALID_RES_ID) return;
        mCloseButton.setText(closeButtonTextRes);
    }

    private void setOnPositiveButtonClickListener(final DialogInterface.OnClickListener onPositiveButtonClickListener) {
        if (onPositiveButtonClickListener == null) btnsContainer.setVisibility(View.GONE);
        else btnsContainer.setVisibility(View.VISIBLE);
        mCloseButton.setVisibility(View.GONE);
        mOnPositiveButtonClickListener = onPositiveButtonClickListener;
    }

    private void setOnNegativeButtonClickListener(final DialogInterface.OnClickListener onNegativeButtonClickListener) {
        if (onNegativeButtonClickListener == null) btnsContainer.setVisibility(View.GONE);
        else btnsContainer.setVisibility(View.VISIBLE);
        mCloseButton.setVisibility(View.GONE);
        mOnNegativeButtonClickListener = onNegativeButtonClickListener;
    }

    private void setOnCloseButtonClickListener(final DialogInterface.OnClickListener onCloseButtonClickListener) {
        if (onCloseButtonClickListener == null) mCloseButton.setVisibility(View.GONE);
        else mCloseButton.setVisibility(View.VISIBLE);
        if (mOnNegativeButtonClickListener != null) btnsContainer.setVisibility(View.VISIBLE);
        mOnCloseButtonClickListener = onCloseButtonClickListener;
    }

    private void setOnDismissListener(final DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @OnClick(R.id.button_positive)
    void onPositiveButtonClicked() {
        if (mOnPositiveButtonClickListener != null)
            mOnPositiveButtonClickListener.onClick(mDialog, Dialog.BUTTON_POSITIVE);
    }

    @OnClick(R.id.button_negative)
    void onNegativeButtonClicked() {
        if (mOnNegativeButtonClickListener != null)
            mOnNegativeButtonClickListener.onClick(mDialog, Dialog.BUTTON_NEGATIVE);
    }

    @OnClick(R.id.text_btn_ok)
    void onCloseButtonClicked() {
        if (mOnCloseButtonClickListener != null)
            mOnCloseButtonClickListener.onClick(mDialog, Dialog.BUTTON_NEUTRAL);
    }

    public void show() {
        mDialog.create();
        mDialog.show();
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public static class DialogBuilder {

        private int mDrawableResource = INVALID_RES_ID;
        private String mPositiveButtonText;
        private String mCloseButtonText;
        private String mTitleS;
        private String tmMessageS;
        private String mNegativeButtonText;
        private int mTitle = INVALID_RES_ID;
        private int mMessage = INVALID_RES_ID;
        private int mPositiveButtonTextRes = INVALID_RES_ID;
        private int mCloseButtonTextRes = INVALID_RES_ID;
        private int mNegativeButtonTextRes = INVALID_RES_ID;

        private DialogInterface.OnClickListener mNegativeButtonClickListener;
        private DialogInterface.OnClickListener mPositiveButtonClickListener;
        private DialogInterface.OnClickListener mCloseButtonClickListener;
        private DialogInterface.OnDismissListener mOnDismissListener;

        public DialogBuilder() {
        }

        public DialogBuilder setTitle(final @StringRes int title) {
            mTitle = title;
            return this;
        }

        public DialogBuilder setTitle(final String title) {
            this.mTitleS = title;
            return this;
        }

        public DialogBuilder setMessage(final @StringRes int message) {
            mMessage = message;
            return this;
        }

        public DialogBuilder setMessage(final String message) {
            tmMessageS = message;
            return this;
        }

        public DialogBuilder setPositiveButton(final String positiveButtonText,
                                               final Dialog.OnClickListener onClickListener) {
            mPositiveButtonText = positiveButtonText;
            mPositiveButtonClickListener = onClickListener;
            return this;
        }

        public DialogBuilder setNegativeButton(final String negativeButtonText,
                                               final Dialog.OnClickListener onClickListener) {
            mNegativeButtonText = negativeButtonText;
            mNegativeButtonClickListener = onClickListener;
            return this;
        }

        public DialogBuilder setCloseButton(final @StringRes int closeButtonTextRes,
                                            final Dialog.OnClickListener onClickListener) {
            mCloseButtonTextRes = closeButtonTextRes;
            mCloseButtonClickListener = onClickListener;
            return this;
        }

        public DialogBuilder setPositiveButton(final @StringRes int positiveButtonTextRes,
                                               final Dialog.OnClickListener onClickListener) {
            mPositiveButtonTextRes = positiveButtonTextRes;
            mPositiveButtonClickListener = onClickListener;
            return this;
        }

        public DialogBuilder setNegativeButton(final @StringRes int negativeButtonTextRes,
                                               final Dialog.OnClickListener onClickListener) {
            mNegativeButtonTextRes = negativeButtonTextRes;
            mNegativeButtonClickListener = onClickListener;
            return this;
        }

        public DialogBuilder setOnDissmisListener(final Dialog.OnDismissListener onDissmisListener) {
            mOnDismissListener = onDissmisListener;
            return this;
        }

        public MultiDialog build(final BaseActivity baseActivity) {
            final MultiDialog dialog = new MultiDialog(baseActivity);
            dialog.setTitle(mTitle);
            dialog.setTitle(mTitleS);
            dialog.setMessage(mMessage);
            dialog.setMessage(tmMessageS);
            dialog.setOnDismissListener(mOnDismissListener);
            dialog.setCloseButtonText(mCloseButtonText);
            dialog.setPositiveButtonText(mPositiveButtonTextRes);
            dialog.setPositiveButtonText(mPositiveButtonText);
            dialog.setNegativeButtonText(mNegativeButtonTextRes);
            dialog.setNegativeButtonText(mNegativeButtonText);
            dialog.setCloseButtonText(mCloseButtonTextRes);
            dialog.setOnPositiveButtonClickListener(mPositiveButtonClickListener);
            dialog.setOnNegativeButtonClickListener(mNegativeButtonClickListener);
            dialog.setOnCloseButtonClickListener(mCloseButtonClickListener);
            return dialog;
        }
    }

}
