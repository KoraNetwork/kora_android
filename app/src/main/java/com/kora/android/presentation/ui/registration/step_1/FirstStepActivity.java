package com.kora.android.presentation.ui.registration.step_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class FirstStepActivity extends BaseActivity<FirstStepPresenter> implements FirstStepView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_layout_phone_number)
    TextInputLayout mPhoneNumber;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        final Intent intent = new Intent(baseActivity, FirstStepActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_step_1;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, true);
    }

    @OnTextChanged(R.id.edit_text_phone_number)
    void onChangedPhoneNumber(final CharSequence phoneNumber) {
        mPhoneNumber.setError(null);
        getPresenter().setPhoneNumber(phoneNumber.toString().trim());
    }

    @OnClick(R.id.card_view_send)
    public void onClickSend() {
        getPresenter().sendSms();
    }

    @Override
    public void showEmptyPhoneNumber() {
        mPhoneNumber.setError(getString(R.string.registration_phone_number_empty));
    }

    @Override
    public void showIncorrectPhoneNumber() {
        mPhoneNumber.setError(getString(R.string.registration_phone_number_incorrect));
    }
}
