package com.kora.android.presentation.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.main.MainActivity;
import com.kora.android.presentation.ui.registration.step1.FirstStepActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView {

    @BindView(R.id.edit_layout_identifier)
    TextInputLayout mElIdentifier;
    @BindView(R.id.edit_layout_password)
    TextInputLayout mElPassword;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        final Intent intent = new Intent(baseActivity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {

    }

    @OnTextChanged(R.id.edit_text_identifier)
    void onChangedIdentifier(final CharSequence identifier) {
        mElIdentifier.setError(null);
        getPresenter().setIdentifier(identifier.toString().trim());
    }

    @Override
    public void showEmptyIdentifier() {
        mElIdentifier.setError(getString(R.string.login_identifier_empty));
    }

    @Override
    public void showIncorrectIdentifier() {
        mElIdentifier.setError(getString(R.string.login_identifier_incorrect));
    }

    @Override
    public void showTooShortIdentifier() {
        mElIdentifier.setError(getString(R.string.login_identifier_too_short));
    }

    @OnTextChanged(R.id.edit_text_password)
    void onChangedPassword(final CharSequence password) {
        mElPassword.setError(null);
        getPresenter().setPassword(password.toString().trim());
    }

    @Override
    public void showEmptyPassword() {
        mElPassword.setError(getString(R.string.login_password_empty));
    }

    @Override
    public void showTooShortPassword() {
        mElPassword.setError(getString(R.string.login_password_too_short));
    }

    @OnClick(R.id.card_view_login)
    public void onClickLogin() {
        getPresenter().startLoginTask();
    }

    @Override
    public void showNextScreen() {
        startActivity(MainActivity.getLaunchIntent(this));
    }


    @OnClick(R.id.text_view_registration)
    public void onClickRegistration() {
        startActivity(FirstStepActivity.getLaunchIntent(this));
    }
}
