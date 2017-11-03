package com.kora.android.presentation.ui.registration.step3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.step4.FourthStepActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ThirdStepActivity extends BaseActivity<ThirdStepPresenter> implements ThirdStepView {

    public static final String VIEW_MODE_ENTER = "VIEW_MODE_ENTER";
    public static final String VIEW_MODE_CONFIRM = "VIEW_MODE_CONFIRM";

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
    @BindView(R.id.card_view_next_finish)
    CardView mCvNextFinish;
    @BindView(R.id.text_view_create_confirm_pin_code)
    TextView mTvCreateConfirmPin;
    @BindView(R.id.text_view_create_confirm_4_digit_pin_code)
    TextView mTvCreateConfirm4DigitPin;
    @BindView(R.id.text_view_next_finish)
    TextView mTvNextFinish;

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

        getPresenter().setViewMode(VIEW_MODE_ENTER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getPresenter().getViewMode().equals(VIEW_MODE_CONFIRM))
            showAnotherMode();
    }

    @OnTextChanged(R.id.edit_text_pin_first_digit)
    void onChangedPinFirstDigit(final Editable editable) {
        mElCreatePinCode.setError(null);
        if (editable.toString().length() == 1)
            mEtPinSecondDigit.requestFocus();
    }

    @OnTextChanged(R.id.edit_text_pin_second_digit)
    void onChangedPinSecondDigit(final Editable editable) {
        mElCreatePinCode.setError(null);
        if (editable.toString().length() == 1)
            mEtPinThirdDigit.requestFocus();
    }

    @OnTextChanged(R.id.edit_text_pin_third_digit)
    void onChangedPinThirdDigit(final Editable editable) {
        mElCreatePinCode.setError(null);
        if (editable.toString().length() == 1)
            mEtPinFourthDigit.requestFocus();
    }

    @OnTextChanged(R.id.edit_text_pin_fourth_digit)
    void onChangedPinFourthDigit() {
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

    @Override
    public void showPinCodeDoesNotMatch() {
        mElCreatePinCode.setError(getString(R.string.registration_pin_code_does_not_match));
    }

    @OnClick(R.id.card_view_next_finish)
    public void onClickNext() {
        final String pinCode =
                        mEtPinFirstDigit.getText().toString().trim() +
                        mEtPinSecondDigit.getText().toString().trim() +
                        mEtPinThirdDigit.getText().toString().trim() +
                        mEtPinFourthDigit.getText().toString().trim();
        getPresenter().startCreateIdentityTask(pinCode);
    }

    @Override
    public void onBackPressed() {
        if (getPresenter().getViewMode().equals(VIEW_MODE_CONFIRM)) {
            showAnotherMode();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showAnotherMode() {
        mEtPinFirstDigit.getText().clear();
        mEtPinSecondDigit.getText().clear();
        mEtPinThirdDigit.getText().clear();
        mEtPinFourthDigit.getText().clear();
        switch (getPresenter().getViewMode()) {
            case VIEW_MODE_ENTER:
                mTvCreateConfirmPin.setText(R.string.registration_confirm_pin_code);
                mTvCreateConfirm4DigitPin.setText(R.string.registration_confirm_4_digit_pin_code);
                mCvNextFinish.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_button_background_green_1));
                mTvNextFinish.setText(R.string.registration_finish);
                getPresenter().setViewMode(VIEW_MODE_CONFIRM);
                break;
            case VIEW_MODE_CONFIRM:
                mTvCreateConfirmPin.setText(R.string.registration_create_pin_code);
                mTvCreateConfirm4DigitPin.setText(R.string.registration_create_4_digit_pin_code);
                mCvNextFinish.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_button_background_blue));
                mTvNextFinish.setText(R.string.registration_next);
                getPresenter().setViewMode(VIEW_MODE_ENTER);
                break;
        }
        mEtPinFirstDigit.clearFocus();
        mEtPinSecondDigit.clearFocus();
        mEtPinThirdDigit.clearFocus();
        mEtPinFourthDigit.clearFocus();
        ViewUtils.hideKeyboard(this);
    }

    @Override
    public void showNextScreen() {
        startActivity(FourthStepActivity.getLaunchIntent(this));
    }
}
