package com.kora.android.presentation.ui.common.deposit_withdraw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kora.android.R;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.Action;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.DepositWithdrawState;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.presentation.ui.common.enter_pin.EnterPinActivity;
import com.kora.android.views.currency.CurrencyEditText;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.kora.android.common.Keys.Args.ACTION_TYPE;
import static com.kora.android.common.Keys.Args.DEPOSIT_ENTITY;
import static com.kora.android.common.Keys.Args.USER_ENTITY;
import static com.kora.android.common.Keys.Args.USER_RECEIVER;
import static com.kora.android.common.Keys.Args.USER_SENDER;
import static com.kora.android.common.Keys.Extras.EXTRA_ACTION;
import static com.kora.android.common.Keys.Extras.EXTRA_DEPOSIT_ENTITY;
import static com.kora.android.data.network.Constants.API_BASE_URL;

public class DepositWithdrawDetailsActivity extends ToolbarActivity<DepositWithdrawDetailsPresenter> implements DepositWithdrawDetailsView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.text_view_state)
    TextView mTvState;

    @BindView(R.id.text_view_user_role)
    TextView mTvUserRole;
    @BindView(R.id.text_view_user_name)
    TextView mTvUserName;
    @BindView(R.id.text_view_user_phone)
    TextView mTvUserPhone;
    @BindView(R.id.image_view_user_avatar)
    ImageView mIvUserAvatar;

    @BindView(R.id.edit_layout_interest_rate)
    TextInputLayout mElInterestRate;
    @BindView(R.id.edit_text_interest_rate)
    EditText mEtInterestRate;

    @BindView(R.id.linear_layout_amount_container)
    LinearLayout mLlAmountContainer;
    @BindView(R.id.edit_text_sender_amount)
    CurrencyEditText mEtSenderAmount;
    @BindView(R.id.edit_layout_sender_amount)
    TextInputLayout mElSenderAmount;
    @BindView(R.id.edit_text_receiver_amount)
    CurrencyEditText mEtReceiverAmount;
    @BindView(R.id.edit_layout_receiver_amount)
    TextInputLayout mElReceiverAmount;

    @BindView(R.id.text_view_total_interest)
    TextView mTvTotalInterest;
    @BindView(R.id.text_view_total_amount)
    TextView mTvTotalAmount;

    @BindView(R.id.button_action)
    Button mBtnAction;
    @BindView(R.id.button_reject)
    Button mBtnReject;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final UserEntity userEntity,
                                         final ActionType actionType) {
        final Intent intent = new Intent(baseActivity, DepositWithdrawDetailsActivity.class);
        intent.putExtra(USER_ENTITY, userEntity);
        intent.putExtra(ACTION_TYPE, actionType);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final DepositWithdrawEntity depositWithdrawEntity,
                                         final ActionType actionType) {
        final Intent intent = new Intent(baseActivity, DepositWithdrawDetailsActivity.class);
        intent.putExtra(DEPOSIT_ENTITY, depositWithdrawEntity);
        intent.putExtra(ACTION_TYPE, actionType);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_deposit_withdraw_details;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitleRes() {
        return R.string.deposit_user_title;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        initArguments(savedInstanceState);
        initUI();

        if (getPresenter().getDepositEntity() == null) {
            if (savedInstanceState == null) {
                getPresenter().getCurrentUser();
            }
        }
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(ACTION_TYPE))
                getPresenter().setActionType((ActionType) bundle.getSerializable(ACTION_TYPE));
            if (bundle.containsKey(DEPOSIT_ENTITY))
                getPresenter().setDepositEntity(bundle.getParcelable(DEPOSIT_ENTITY));
            if (bundle.containsKey(USER_RECEIVER))
                getPresenter().setReceiver(bundle.getParcelable(USER_RECEIVER));
            if (bundle.containsKey(USER_SENDER))
                getPresenter().setSender(bundle.getParcelable(USER_SENDER));
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra(ACTION_TYPE))
                getPresenter().setActionType((ActionType) getIntent().getSerializableExtra(ACTION_TYPE));
            if (getIntent().hasExtra(DEPOSIT_ENTITY))
                getPresenter().setDepositEntity(getIntent().getParcelableExtra(DEPOSIT_ENTITY));
            if (getIntent().hasExtra(USER_ENTITY))
                getPresenter().setReceiver(getIntent().getParcelableExtra(USER_ENTITY));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(ACTION_TYPE, getPresenter().getActionType());
        outState.putParcelable(DEPOSIT_ENTITY, getPresenter().getDepositEntity());
        outState.putParcelable(USER_RECEIVER, getPresenter().getReceiver());
        outState.putParcelable(USER_SENDER, getPresenter().getSender());
    }

    private void initUI() {
        setupTitle();
        setupState();
        setupDetails();
        setupInterestRate();
        setupCurrencyAmounts();
        setupButtons();
    }

    private void setupTitle() {
        switch (getPresenter().getActionType()) {
            case CREATE_DEPOSIT:
                setTitle(R.string.deposit_user_title);
                break;
            case SHOW_DEPOSIT:
                if (getPresenter().getDepositEntity().getDirection() == Direction.FROM)
                    setTitle(R.string.deposit_user_title);
                else
                    setTitle(R.string.deposit_agent_title);
                break;
            case CREATE_WITHDRAW:
                setTitle(R.string.withdraw_agent_title);
                break;
            case SHOW_WITHDRAW:
                if (getPresenter().getDepositEntity().getDirection() == Direction.FROM)
                    setTitle(R.string.withdraw_agent_title);
                else
                    setTitle(R.string.withdraw_user_title);
                break;
        }
    }

    private void setupState() {
        switch (getPresenter().getActionType()) {
            case SHOW_DEPOSIT:
            case SHOW_WITHDRAW:
                mTvState.setVisibility(View.VISIBLE);
                switch (getPresenter().getDepositEntity().getState()) {
                    case INPROGRESS:
                        mTvState.setText(R.string.deposit_state_in_progress);
                        mTvState.setTextColor(getResources().getColor(R.color.color_state_positive));
                        break;
                    case REJECTED:
                        mTvState.setText(R.string.deposit_state_rejected);
                        mTvState.setTextColor(getResources().getColor(R.color.color_state_negative));
                        break;
                }
                break;
        }
    }

    private void setupDetails() {
        switch (getPresenter().getActionType()) {
            case CREATE_DEPOSIT:
                mTvUserRole.setText(R.string.deposit_agent_details);
                break;
            case SHOW_DEPOSIT:
                if (getPresenter().getDepositEntity().getDirection() == Direction.FROM) {
                    mTvUserRole.setText(R.string.deposit_agent_details);
                } else
                    mTvUserRole.setText(R.string.deposit_user_details);
                break;
            case CREATE_WITHDRAW:
                mTvUserRole.setText(R.string.deposit_user_details);
                break;
            case SHOW_WITHDRAW:
                if (getPresenter().getDepositEntity().getDirection() == Direction.FROM) {
                    mTvUserRole.setText(R.string.deposit_user_details);
                } else
                    mTvUserRole.setText(R.string.deposit_agent_details);
                break;
        }
    }

    private void setupInterestRate() {
        switch (getPresenter().getActionType()) {
            case CREATE_DEPOSIT:
                mEtInterestRate.setText(String.valueOf(getPresenter().getReceiver().getInterestRate()));
                mEtInterestRate.setEnabled(true);
                break;
            case SHOW_DEPOSIT:
            case SHOW_WITHDRAW:
                mEtInterestRate.setText(String.valueOf(getPresenter().getDepositEntity().getInterestRate()));
                break;
        }
    }

    private void setupCurrencyAmounts() {
        switch (getPresenter().getActionType()) {
            case SHOW_DEPOSIT:
            case SHOW_WITHDRAW:
                final DepositWithdrawEntity depositWithdrawEntity = getPresenter().getDepositEntity();

                mEtSenderAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", depositWithdrawEntity.getFromAmount()));
                mEtSenderAmount.setCurrency(depositWithdrawEntity.getFrom().getCurrency());
                Glide.with(this)
                        .asBitmap()
                        .load(API_BASE_URL + depositWithdrawEntity.getFrom().getFlag())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                mEtSenderAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                                mEtSenderAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                            }
                        });

                mEtReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", depositWithdrawEntity.getToAmount()));
                mEtReceiverAmount.setCurrency(depositWithdrawEntity.getTo().getCurrency());
                Glide.with(this)
                        .asBitmap()
                        .load(API_BASE_URL + depositWithdrawEntity.getTo().getFlag())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                mEtReceiverAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                                mEtReceiverAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                            }
                        });

                mEtSenderAmount.setEnabled(false);
                mEtReceiverAmount.setEnabled(false);

                changeAmountContainerOrientation();
                calculateTotals();
                break;
        }
    }

    private void setupButtons() {
        switch (getPresenter().getActionType()) {
            case CREATE_DEPOSIT:
                mBtnAction.setText(R.string.deposit_send_to_agent);
                break;
            case CREATE_WITHDRAW:
                mBtnAction.setText(R.string.deposit_send_to_user);
                break;
            case SHOW_DEPOSIT:
            case SHOW_WITHDRAW:
                final DepositWithdrawEntity depositWithdrawEntity = getPresenter().getDepositEntity();
                switch (depositWithdrawEntity.getDirection()) {
                    case FROM:
                        mBtnAction.setVisibility(View.GONE);
                        break;
                    case TO:
                        if (depositWithdrawEntity.getState() == DepositWithdrawState.INPROGRESS) {
                            mBtnAction.setText(R.string.deposit_confirm);
                            mBtnReject.setVisibility(View.VISIBLE);
                        } else {
                            mBtnAction.setVisibility(View.GONE);
                        }
                        break;
                }
        }
    }

    private void calculateTotals() {
        if (getPresenter().getDepositEntity() == null) {
            final double amount = mEtSenderAmount.getAmount();
            final String rateString = mEtInterestRate.getText().toString();
            if (amount == 0 || rateString.isEmpty()) {
                mTvTotalInterest.setText("");
                mTvTotalAmount.setText("");
                return;
            }
            try {
                double rate = Double.valueOf(rateString);
                double totalInterest = Math.floor(amount * rate) / 100;
                double totalAmount = amount + totalInterest;
                mTvTotalInterest.setText(getString(R.string.borrow_amount, totalInterest, mEtSenderAmount.getCurrency()));
                mTvTotalAmount.setText(getString(R.string.borrow_amount, totalAmount, mEtSenderAmount.getCurrency()));
            } catch (final Exception ignored) {
            }
        } else {
            final Direction direction = getPresenter().getDepositEntity().getDirection();
            switch (direction) {
                case FROM: {
                    final double amount = mEtSenderAmount.getAmount();
                    final String rateString = mEtInterestRate.getText().toString();
                    if (amount == 0 || rateString.isEmpty()) {
                        mTvTotalInterest.setText("");
                        mTvTotalAmount.setText("");
                        return;
                    }
                    try {
                        final double rate = Double.valueOf(rateString);
                        final double totalInterest = Math.floor(amount * rate) / 100;
                        final double totalAmount = amount + totalInterest;
                        mTvTotalInterest.setText(getString(R.string.deposit_amount, totalInterest, mEtSenderAmount.getCurrency()));
                        mTvTotalAmount.setText(getString(R.string.deposit_amount, totalAmount, mEtSenderAmount.getCurrency()));
                    } catch (final Exception ignored) {
                    }
                    break;
                }
                case TO: {
                    final double amount = mEtReceiverAmount.getAmount();
                    final String rateString = mEtInterestRate.getText().toString();
                    if (amount == 0 || rateString.isEmpty()) {
                        mTvTotalInterest.setText("");
                        mTvTotalAmount.setText("");
                        return;
                    }
                    try {
                        final double rate = Double.valueOf(rateString);
                        final double totalInterest = Math.floor(amount * rate) / 100;
                        final double totalAmount = amount + totalInterest;
                        mTvTotalInterest.setText(getString(R.string.deposit_amount, totalInterest, mEtReceiverAmount.getCurrency()));
                        mTvTotalAmount.setText(getString(R.string.deposit_amount, totalAmount, mEtReceiverAmount.getCurrency()));
                    } catch (final Exception ignored) {
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void retrieveSender(final UserEntity sender) {
        mEtSenderAmount.setCurrency(sender.getCurrency());
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + sender.getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mEtSenderAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mEtSenderAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });

        if (getPresenter().getActionType() == ActionType.CREATE_WITHDRAW) {
            mEtInterestRate.setText(String.valueOf(sender.getInterestRate()));
            mEtInterestRate.setEnabled(true);
        }
    }

    public void retrieveReceiver(final UserEntity receiver) {
        mTvUserName.setText(receiver.getFullName());
        mTvUserPhone.setText(StringUtils.getFormattedPhoneNumber(receiver.getPhoneNumber(), receiver.getCountryCode()));
        Glide.with(this)
                .load(API_BASE_URL + receiver.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this)
                        .load(R.drawable.ic_user_default))
                .into(mIvUserAvatar);

        if (getPresenter().getDepositEntity() == null) {
            mEtReceiverAmount.setCurrency(receiver.getCurrency());
            Glide.with(this)
                    .asBitmap()
                    .load(API_BASE_URL + receiver.getFlag())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            mEtReceiverAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                            mEtReceiverAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                        }
                    });
        }
    }

    @Override
    public void showConvertedCurrency(final Double amount, final String currency) {
        mEtReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", amount));

        changeAmountContainerOrientation();
        calculateTotals();
    }

    @Override
    public void emptySenderAmountError() {
        mElSenderAmount.setError(getString(R.string.deposit_empty_sender_amount_error));
    }

    @Override
    public void emptyReceiverAmountError() {
        mElReceiverAmount.setError(getString(R.string.deposit_empty_receiver_amount_error));
    }

    @Override
    public void showIncorrectInterestRate() {
        mElInterestRate.setError(getString(R.string.deposit_incorrect_interest_rate));
    }

    @Override
    public void openEnterPinScreen(final UserEntity receiver,
                                   final double fromAmount,
                                   final double toAmount,
                                   final DepositWithdrawEntity depositWithdrawEntity) {
        startActivity(EnterPinActivity.getLaunchIntent(
                this,
                receiver,
                fromAmount,
                toAmount,
                getPresenter().getActionType(),
                depositWithdrawEntity)
        );
    }

    @Override
    public void onUserRejected(final DepositWithdrawEntity depositWithdrawEntity) {
        if (getPresenter().getActionType() == ActionType.SHOW_DEPOSIT) {
            Toast.makeText(this, R.string.deposit_reject_success, Toast.LENGTH_SHORT).show();
        } else  if (getPresenter().getActionType() == ActionType.SHOW_WITHDRAW){
            Toast.makeText(this, R.string.withdraw_reject_success, Toast.LENGTH_SHORT).show();
        }
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_ACTION, Action.UPDATE);
        intent.putExtra(EXTRA_DEPOSIT_ENTITY, depositWithdrawEntity);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDepositWithdrawSent(final DepositWithdrawEntity depositWithdrawEntity) {
        if (getPresenter().getActionType() == ActionType.CREATE_DEPOSIT) {
            Toast.makeText(this, R.string.deposit_send_success, Toast.LENGTH_SHORT).show();
        } else if (getPresenter().getActionType() == ActionType.CREATE_WITHDRAW){
            Toast.makeText(this, R.string.withdraw_send_success, Toast.LENGTH_SHORT).show();
        }
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_ACTION, Action.DELETE);
        intent.putExtra(EXTRA_DEPOSIT_ENTITY, depositWithdrawEntity);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnTextChanged(R.id.edit_text_interest_rate)
    public void onInterestRateChanged() {
        mElInterestRate.setError(null);

        calculateTotals();
    }

    private Handler mTimer = new Handler();

    private Runnable mConverter = () -> {
        double value = mEtSenderAmount.getAmount();
        getPresenter().convertIfNeed(value);
    };

    @OnTextChanged(R.id.edit_text_sender_amount)
    public void onAmountChanged() {
        if (getPresenter().getDepositEntity() != null)
            return;

        mElSenderAmount.setError(null);
        mElReceiverAmount.setError(null);
        mTimer.removeCallbacks(mConverter);
        mTimer.postDelayed(mConverter, 500);

        changeAmountContainerOrientation();
    }

    @OnClick(R.id.button_action)
    public void onActionButtonClicked() {
        switch (getPresenter().getActionType()) {
            case CREATE_DEPOSIT:
            case CREATE_WITHDRAW:
                mElReceiverAmount.setError(null);
                mElSenderAmount.setError(null);
                getPresenter().sendDepositWithdraw(
                        mEtSenderAmount.getAmount(),
                        mEtReceiverAmount.getAmount(),
                        mEtInterestRate.getText().toString().trim());
                break;
            case SHOW_DEPOSIT:
            case SHOW_WITHDRAW:
                getPresenter().onConfirmClicked();
                break;
        }
    }

    @OnClick(R.id.button_reject)
    public void onRejectClicked() {
        getPresenter().onRejectClicked();
    }

    private void changeAmountContainerOrientation() {
        if (isTextLonger(mEtSenderAmount) || isTextLonger(mEtReceiverAmount)) {
            if (mLlAmountContainer.getOrientation() != LinearLayout.VERTICAL)
                mLlAmountContainer.setOrientation(LinearLayout.VERTICAL);
        } else {
            if (mLlAmountContainer.getOrientation() != LinearLayout.HORIZONTAL)
                mLlAmountContainer.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    private boolean isTextLonger(final TextInputEditText editText) {
        final Editable text = editText.getText();
        final TextPaint paint = editText.getPaint();
        final float textSize = paint.measureText(text.toString());
        int width = editText.getMeasuredWidth();
        width -= editText.getPaddingRight();
        width -= editText.getPaddingLeft();
        width -= editText.getCompoundDrawablePadding();
        if (getPresenter().getAmountEditTextWidth() > 0 && textSize >= getPresenter().getAmountEditTextWidth())
            return true;
        if (textSize >= width && width > 0) {
            getPresenter().setAmountEditTextWidth(width);
            return true;
        }
        return false;
    }
}
