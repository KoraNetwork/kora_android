package com.kora.android.presentation.ui.forgot_password.step2;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ForgotPassword2Activity extends ToolbarActivity<ForgotPassword2Presenter> implements ForgotPassword2View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.edit_layout_new_pass)
    TextInputLayout mElNewPassword;
    @BindView(R.id.edit_layout_confirm_pass)
    TextInputLayout mElConfirmPassword;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, ForgotPassword2Activity.class);
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_forgot_password_2;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitleRes() {
        return -1;
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.ic_back_grey;
    }

    @OnTextChanged(R.id.edit_text_new_pass)
    void onChangedPassword(final CharSequence password) {
        mElNewPassword.setError(null);
        getPresenter().setNewPassword(password.toString().trim());
    }

    @Override
    public void showEmptyNewPassword() {
        mElNewPassword.setError(getString(R.string.forgot_pass_new_pass_empty));
    }

    @Override
    public void showIncorrectNewPassword() {
        mElNewPassword.setError(getString(R.string.forgot_pass_new_pass_incorrect));
    }

    @OnTextChanged(R.id.edit_text_confirm_pass)
    void onChangedConfirmPassword(final CharSequence confirmPassword) {
        mElConfirmPassword.setError(null);
        getPresenter().setConfirmPassword(confirmPassword.toString().trim());
    }

    @Override
    public void showEmptyConfirmPassword() {
        mElConfirmPassword.setError(getString(R.string.forgot_pass_confirm_pass_empty));
    }

    @Override
    public void showIncorrectConfirmPassword() {
        mElConfirmPassword.setError(getString(R.string.forgot_pass_confirm_pass_incorrect));
    }

    @OnClick(R.id.button_confirm)
    public void onClickConfirm() {
        getPresenter().confirmPassword();
    }

    @Override
    public void showNext() {
        Toast.makeText(this, "Message is sent", Toast.LENGTH_SHORT).show();
    }
}
