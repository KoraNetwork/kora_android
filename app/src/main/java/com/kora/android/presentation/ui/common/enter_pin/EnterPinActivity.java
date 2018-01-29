package com.kora.android.presentation.ui.common.enter_pin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.Toast;

import com.kora.android.R;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.ACTION_TYPE;
import static com.kora.android.common.Keys.Args.BORROW_ENTITY;
import static com.kora.android.common.Keys.Args.DEPOSIT_ENTITY;
import static com.kora.android.common.Keys.Args.RECEIVER_AMOUNT;
import static com.kora.android.common.Keys.Args.REQUEST_ENTITY;
import static com.kora.android.common.Keys.Args.BORROWER_VALUE;
import static com.kora.android.common.Keys.Args.SENDER_AMOUNT;
import static com.kora.android.common.Keys.Args.TRANSACTION_TYPE;
import static com.kora.android.common.Keys.Args.USER_ENTITY;

public class EnterPinActivity extends BaseActivity<EnterPinPresenter> implements EnterPinView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_layout_enter_pin)
    TextInputLayout mElCreatePinCode;
    @BindView(R.id.edit_text_pin_first_digit)
    TextInputEditText mEtPinFirstDigit;
    @BindView(R.id.edit_text_pin_second_digit)
    TextInputEditText mEtPinSecondDigit;
    @BindView(R.id.edit_text_pin_third_digit)
    TextInputEditText mEtPinThirdDigit;
    @BindView(R.id.edit_text_pin_fourth_digit)
    TextInputEditText mEtPinFourthDigit;
    @BindView(R.id.check_box_visibility)
    CheckBox mCbVisibility;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final UserEntity receiver,
                                         final double senderAmount,
                                         final double receiverAmount,
                                         final ActionType actionType) {
        final Intent intent = new Intent(baseActivity, EnterPinActivity.class);
        intent.putExtra(USER_ENTITY, receiver);
        intent.putExtra(SENDER_AMOUNT, senderAmount);
        intent.putExtra(RECEIVER_AMOUNT, receiverAmount);
        intent.putExtra(ACTION_TYPE, actionType);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final UserEntity receiver,
                                         final double senderAmount,
                                         final double receiverAmount,
                                         final ActionType actionType,
                                         final RequestEntity requestEntity) {
        final Intent intent = new Intent(baseActivity, EnterPinActivity.class);
        intent.putExtra(USER_ENTITY, receiver);
        intent.putExtra(SENDER_AMOUNT, senderAmount);
        intent.putExtra(RECEIVER_AMOUNT, receiverAmount);
        intent.putExtra(ACTION_TYPE, actionType);
        intent.putExtra(REQUEST_ENTITY, requestEntity);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final UserEntity receiver,
                                         final double senderAmount,
                                         final double receiverAmount,
                                         final ActionType actionType,
                                         final DepositWithdrawEntity depositWithdrawEntity) {
        final Intent intent = new Intent(baseActivity, EnterPinActivity.class);
        intent.putExtra(USER_ENTITY, receiver);
        intent.putExtra(SENDER_AMOUNT, senderAmount);
        intent.putExtra(RECEIVER_AMOUNT, receiverAmount);
        intent.putExtra(ACTION_TYPE, actionType);
        intent.putExtra(DEPOSIT_ENTITY, depositWithdrawEntity);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final BorrowEntity borrowEntity,
                                         final ActionType actionType) {
        final Intent intent = new Intent(baseActivity, EnterPinActivity.class);
        intent.putExtra(BORROW_ENTITY, borrowEntity);
        intent.putExtra(ACTION_TYPE, actionType);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final BorrowEntity borrowEntity,
                                         final double borrowerValue,
                                         final ActionType actionType) {
        final Intent intent = new Intent(baseActivity, EnterPinActivity.class);
        intent.putExtra(BORROW_ENTITY, borrowEntity);
        intent.putExtra(BORROWER_VALUE, borrowerValue);
        intent.putExtra(ACTION_TYPE, actionType);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_enter_pin;
    }


    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_white);

        initArguments(savedInstanceState);
        initUI();
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(USER_ENTITY))
                getPresenter().setReceiver(bundle.getParcelable(USER_ENTITY));
            if (bundle.containsKey(SENDER_AMOUNT))
                getPresenter().setSenderAmount(bundle.getDouble(SENDER_AMOUNT));
            if (bundle.containsKey(RECEIVER_AMOUNT))
                getPresenter().setReceiverAmount(bundle.getDouble(RECEIVER_AMOUNT));
            if (bundle.containsKey(ACTION_TYPE))
                getPresenter().setActionType((ActionType) bundle.getSerializable(ACTION_TYPE));
            if (bundle.containsKey(REQUEST_ENTITY))
                getPresenter().setRequestEntity(bundle.getParcelable(REQUEST_ENTITY));
            if (bundle.containsKey(BORROW_ENTITY))
                getPresenter().setBorrowEntity(bundle.getParcelable(BORROW_ENTITY));
            if (bundle.containsKey(BORROWER_VALUE))
                getPresenter().setBorrowerValue(bundle.getDouble(BORROWER_VALUE));
            if (bundle.containsKey(DEPOSIT_ENTITY))
                getPresenter().setDepositEntity(bundle.getParcelable(DEPOSIT_ENTITY));
        }
        if (getIntent() != null) {
            getPresenter().setReceiver(getIntent().getParcelableExtra(USER_ENTITY));
            getPresenter().setSenderAmount(getIntent().getDoubleExtra(SENDER_AMOUNT, 0));
            getPresenter().setReceiverAmount(getIntent().getDoubleExtra(RECEIVER_AMOUNT, 0));
            getPresenter().setActionType((ActionType) getIntent().getSerializableExtra(ACTION_TYPE));
            getPresenter().setRequestEntity(getIntent().getParcelableExtra(REQUEST_ENTITY));
            getPresenter().setBorrowEntity(getIntent().getParcelableExtra(BORROW_ENTITY));
            getPresenter().setBorrowerValue(getIntent().getDoubleExtra(BORROWER_VALUE, 0));
            getPresenter().setDepositEntity(getIntent().getParcelableExtra(DEPOSIT_ENTITY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(USER_ENTITY, getPresenter().getReceiver());
        outState.putDouble(SENDER_AMOUNT, getPresenter().getSenderAmount());
        outState.putDouble(RECEIVER_AMOUNT, getPresenter().getReceiverAmount());
        outState.putSerializable(TRANSACTION_TYPE, getPresenter().getActionType());
        outState.putParcelable(REQUEST_ENTITY, getPresenter().getRequestEntity());
        outState.putParcelable(BORROW_ENTITY, getPresenter().getBorrowEntity());
        outState.putDouble(BORROWER_VALUE, getPresenter().getBorrowerValue());
        outState.putParcelable(DEPOSIT_ENTITY, getPresenter().getDepositEntity());
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
//                    mEtPinSecondDigit.clearFocus();
//                    mEtPinFirstDigit.requestFocus();
//                    mEtPinFirstDigit.setSelection(mEtPinFirstDigit.length());
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
//                    mEtPinThirdDigit.clearFocus();
//                    mEtPinSecondDigit.requestFocus();
//                    mEtPinSecondDigit.setSelection(mEtPinSecondDigit.length());
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
                    ViewUtils.hideKeyboard(EnterPinActivity.this);
                } else if (s.toString().length() == 0) {
//                    mEtPinFourthDigit.clearFocus();
//                    mEtPinThirdDigit.requestFocus();
//                    mEtPinThirdDigit.setSelection(mEtPinThirdDigit.length());
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
        mElCreatePinCode.setError(getString(R.string.enter_pin_pin_empty));
    }

    @Override
    public void showTooShortPinCode() {
        mElCreatePinCode.setError(getString(R.string.enter_pin_pin_too_short));
    }

    @OnClick(R.id.card_view_finish)
    public void onClickFinish() {
        final String pinCode =
                        mEtPinFirstDigit.getText().toString().trim() +
                        mEtPinSecondDigit.getText().toString().trim() +
                        mEtPinThirdDigit.getText().toString().trim() +
                        mEtPinFourthDigit.getText().toString().trim();
        getPresenter().startCreateRawTransactionTask(pinCode);
    }

    @Override
    public void showTransactionScreen() {
        ViewUtils.hideKeyboard(this);
        Toast.makeText(this, R.string.enter_pin_transaction_is_sent, Toast.LENGTH_SHORT).show();
        startActivity(MainActivity.getLaunchIntent(this));
    }

    @Override
    public void showBorrowScreen() {
        ViewUtils.hideKeyboard(this);
        Toast.makeText(this, R.string.enter_pin_borrow_is_sent, Toast.LENGTH_SHORT).show();
        startActivity(MainActivity.getLaunchIntent(this, MainActivity.TAB_BORROW_MONEY_POSITION));
    }
}
