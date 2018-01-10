package com.kora.android.presentation.ui.registration.step3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.step4.FourthStepActivity;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

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
    @BindView(R.id.text_view_create_confirm_pin_code)
    TextView mTvCreateConfirmPin;
    @BindView(R.id.text_view_create_confirm_4_digit_pin_code)
    TextView mTvCreateConfirm4DigitPin;
    @BindView(R.id.button_next_finish)
    Button mBtnNextFinish;
    @BindView(R.id.check_box_visibility)
    CheckBox mCbVisibility;

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

        initUI();
        getPresenter().setViewMode(VIEW_MODE_ENTER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getPresenter().getViewMode().equals(VIEW_MODE_CONFIRM))
            showAnotherMode();
    }

    private void initUI() {
        mEtPinFirstDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mElCreatePinCode.setError(null);
                if (s.toString().length() == 1) {
                    mEtPinFirstDigit.clearFocus();
                    mEtPinSecondDigit.requestFocus();
                }
            }
        });
        mEtPinSecondDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mElCreatePinCode.setError(null);
                if (s.toString().length() == 1) {
                    mEtPinSecondDigit.clearFocus();
                    mEtPinThirdDigit.requestFocus();
                } else if (s.toString().length() == 0) {
                    mEtPinSecondDigit.clearFocus();
                    mEtPinFirstDigit.requestFocus();
                    mEtPinFirstDigit.setSelection(mEtPinFirstDigit.length());
                }
            }
        });
        mEtPinThirdDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mElCreatePinCode.setError(null);
                if (s.toString().length() == 1) {
                    mEtPinThirdDigit.clearFocus();
                    mEtPinFourthDigit.requestFocus();
                } else if (s.toString().length() == 0) {
                    mEtPinThirdDigit.clearFocus();
                    mEtPinSecondDigit.requestFocus();
                    mEtPinSecondDigit.setSelection(mEtPinSecondDigit.length());
                }
            }
        });
        mEtPinFourthDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mElCreatePinCode.setError(null);
                if (s.toString().length() == 1) {
                    mEtPinFourthDigit.clearFocus();
                    ViewUtils.hideKeyboard(ThirdStepActivity.this);
                } else if (s.toString().length() == 0) {
                    mEtPinFourthDigit.clearFocus();
                    mEtPinThirdDigit.requestFocus();
                    mEtPinThirdDigit.setSelection(mEtPinThirdDigit.length());
                }
            }
        });
    }

    @OnCheckedChanged(R.id.check_box_visibility)
    public void onCheckChangedVisibility(boolean isChecked) {
        if (isChecked) {
            mEtPinFirstDigit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEtPinSecondDigit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEtPinThirdDigit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEtPinFourthDigit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            mEtPinFirstDigit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEtPinSecondDigit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEtPinThirdDigit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEtPinFourthDigit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
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

    @OnClick(R.id.button_next_finish)
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
                mBtnNextFinish.setBackground(getResources().getDrawable(R.drawable.green_button_background));
                mBtnNextFinish.setText(R.string.registration_finish);
                getPresenter().setViewMode(VIEW_MODE_CONFIRM);
                break;
            case VIEW_MODE_CONFIRM:
                mTvCreateConfirmPin.setText(R.string.registration_create_pin_code);
                mTvCreateConfirm4DigitPin.setText(R.string.registration_create_4_digit_pin_code);
                mBtnNextFinish.setBackground(getResources().getDrawable(R.drawable.blue_button_background));
                mBtnNextFinish.setText(R.string.registration_next);
                getPresenter().setViewMode(VIEW_MODE_ENTER);
                break;
        }
        mEtPinFirstDigit.clearFocus();
        mEtPinSecondDigit.clearFocus();
        mEtPinThirdDigit.clearFocus();
        mEtPinFourthDigit.clearFocus();
        mCbVisibility.setChecked(false);
        ViewUtils.hideKeyboard(this);
    }

    @Override
    public void showNextScreen() {
        startActivity(FourthStepActivity.getLaunchIntent(this));
    }
}
