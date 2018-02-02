package com.kora.android.presentation.ui.forgot_password.step1;

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

public class ForgotPassword1Activity extends ToolbarActivity<ForgotPassword1Presenter> implements ForgotPassword1View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.edit_layout_email)
    TextInputLayout mElEmail;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, ForgotPassword1Activity.class);
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_forgot_password_1;
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

    @OnTextChanged(R.id.edit_text_email)
    public void onChangedEmail(final CharSequence email) {
        getPresenter().setEmail(email.toString().trim());
    }

    @Override
    public void showEmptyEmail() {
        mElEmail.setError(getString(R.string.forgot_pass_email_empty));
    }

    @Override
    public void showIncorrectEmail() {
        mElEmail.setError(getString(R.string.forgot_pass_email_incorrect));
    }

    @OnClick(R.id.button_send)
    public void onClickSend() {
        getPresenter().sendEmail();
    }

    @Override
    public void showMessageIsSent() {
        Toast.makeText(this, R.string.forgot_pass_message_is_sent, Toast.LENGTH_LONG).show();
    }
}
