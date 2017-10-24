package com.kora.android.presentation.ui.common.enter_pin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.kora.android.common.Keys.Args.RECEIVER_AMOUNT;
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

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final UserEntity receiver,
                                         final double senderAmount,
                                         final double receiverAmount,
                                         final TransactionType transactionType) {
        final Intent intent = new Intent(baseActivity, EnterPinActivity.class);
        intent.putExtra(USER_ENTITY, receiver);
        intent.putExtra(SENDER_AMOUNT, senderAmount);
        intent.putExtra(RECEIVER_AMOUNT, receiverAmount);
        intent.putExtra(TRANSACTION_TYPE, transactionType.toString());
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
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(USER_ENTITY))
                getPresenter().setReceiver(bundle.getParcelable(USER_ENTITY));
            if (bundle.containsKey(SENDER_AMOUNT))
                getPresenter().setSenderAmount(bundle.getDouble(SENDER_AMOUNT));
            if (bundle.containsKey(RECEIVER_AMOUNT))
                getPresenter().setReceiverAmount(bundle.getDouble(RECEIVER_AMOUNT));
            if (bundle.containsKey(TRANSACTION_TYPE))
                getPresenter().setTransactionType(bundle.getString(TRANSACTION_TYPE));
        }
        if (getIntent() != null) {
            getPresenter().setReceiver(getIntent().getParcelableExtra(USER_ENTITY));
            getPresenter().setSenderAmount(getIntent().getDoubleExtra(SENDER_AMOUNT, 0));
            getPresenter().setReceiverAmount(getIntent().getDoubleExtra(RECEIVER_AMOUNT, 0));
            getPresenter().setTransactionType(getIntent().getStringExtra(TRANSACTION_TYPE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(USER_ENTITY, getPresenter().getReceiver());
        outState.putDouble(SENDER_AMOUNT, getPresenter().getSenderAmount());
        outState.putDouble(RECEIVER_AMOUNT, getPresenter().getReceiverAmount());
        outState.putString(TRANSACTION_TYPE, getPresenter().getTransactionType().toString());
    }

    @OnTextChanged(R.id.edit_text_pin_first_digit)
    void onChangedPinFirstDigit() {
        mElCreatePinCode.setError(null);
        mEtPinSecondDigit.requestFocus();
    }

    @OnTextChanged(R.id.edit_text_pin_second_digit)
    void onChangedPinSecondDigit() {
        mElCreatePinCode.setError(null);
        mEtPinThirdDigit.requestFocus();
    }

    @OnTextChanged(R.id.edit_text_pin_third_digit)
    void onChangedPinThirdDigit() {
        mElCreatePinCode.setError(null);
        mEtPinFourthDigit.requestFocus();
    }

    @OnTextChanged(R.id.edit_text_pin_fourth_digit)
    void onChangedPinFourthDigit() {
        mElCreatePinCode.setError(null);
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
        getPresenter().startSendTransactionTask(pinCode);
    }

    @Override
    public void showNextScreen() {
        Toast.makeText(this, R.string.enter_pin_transaction_is_sent, Toast.LENGTH_SHORT).show();
        startActivity(MainActivity.getLaunchIntent(this));
    }
}
