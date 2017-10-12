package com.kora.android.presentation.ui.registration.step3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.step4.FourthStepActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ThirdStepActivity extends BaseActivity<ThirdStepPresenter> implements ThirdStepView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_layout_create_pin_code)
    TextInputLayout mElCreatePinCode;
    @BindView(R.id.edit_text_pin_first_digit)
    TextInputEditText mEtPinFirstDigit;
    @BindView(R.id.edit_text_pin_second_digit)
    TextInputEditText mEtPinSecondDigit;
    @BindView(R.id.edit_text_pin_third_digit)
    TextInputEditText mEtPinThirdDigit;
    @BindView(R.id.edit_text_pin_fourth_digit)
    TextInputEditText mEtPinFourthDigit;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, ThirdStepActivity.class);
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_registration_step_3;
    }


    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_grey);
    }

    @OnTextChanged({
            R.id.edit_text_pin_first_digit,
            R.id.edit_text_pin_second_digit,
            R.id.edit_text_pin_third_digit,
            R.id.edit_text_pin_fourth_digit
    })
    void onChangedPhoneNumber() {
        mElCreatePinCode.setError(null);
    }

    @Override
    public void showEmptyPinCode() {
        mElCreatePinCode.setError(getString(R.string.registration_pin_code_empty));
    }

    @Override
    public void showTooShortPinCode() {
        mElCreatePinCode.setError(getString(R.string.registration_pin_code_too_short));
    }

    @OnClick(R.id.card_view_next)
    public void onClickNext() {
        final String pinCode =
                mEtPinFirstDigit.getText().toString() +
                        mEtPinSecondDigit.getText().toString() +
                        mEtPinThirdDigit.getText().toString() +
                        mEtPinFourthDigit.getText().toString();
        getPresenter().startCreateIdentityTask(pinCode);
    }

    @Override
    public void showNextScreen() {
        startActivity(FourthStepActivity.getLaunchIntent(this));
    }
}
