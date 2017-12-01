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
import com.kora.android.presentation.enums.DepositState;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.model.DepositEntity;
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

public class DepositDetailsActivity extends ToolbarActivity<DepositDetailsPresenter> implements DepositDetailsView {

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
        final Intent intent = new Intent(baseActivity, DepositDetailsActivity.class);
        intent.putExtra(USER_ENTITY, userEntity);
        intent.putExtra(ACTION_TYPE, actionType);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final DepositEntity depositEntity) {
        final Intent intent = new Intent(baseActivity, DepositDetailsActivity.class);
        intent.putExtra(DEPOSIT_ENTITY, depositEntity);
        intent.putExtra(ACTION_TYPE, ActionType.SHOW_DEPOSIT);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_deposit_details;
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
        if (savedInstanceState == null) {
            getPresenter().getCurrentUser();
        }
        initUI();
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
        switch (getPresenter().getActionType()) {
            case CREATE_DEPOSIT:
                setTitle(R.string.deposit_user_title);
                mBtnAction.setText(R.string.deposit_send_to_agent);
                mEtInterestRate.setText(String.valueOf(getPresenter().getReceiver().getInterestRate()));
            case SHOW_DEPOSIT:
                final DepositEntity depositEntity = getPresenter().getDepositEntity();
                if (depositEntity == null) return;

                mTvState.setVisibility(View.VISIBLE);
                switch (depositEntity.getState()) {
                    case INPROGRESS:
                        mTvState.setText(R.string.deposit_state_in_progress);
                        mTvState.setTextColor(getResources().getColor(R.color.color_state_positive));
                        break;
                    case REJECTED:
                        mTvState.setText(R.string.deposit_state_rejected);
                        mTvState.setTextColor(getResources().getColor(R.color.color_state_negative));
                        break;
                }

                mEtSenderAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", depositEntity.getFromAmount()));
                mEtSenderAmount.setCurrency(depositEntity.getFrom().getCurrency());
                Glide.with(this)
                        .asBitmap()
                        .load(API_BASE_URL + depositEntity.getFrom().getFlag())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                mEtSenderAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                                mEtSenderAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                            }
                        });

                mEtReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", depositEntity.getToAmount()));
                mEtReceiverAmount.setCurrency(depositEntity.getTo().getCurrency());
                Glide.with(this)
                        .asBitmap()
                        .load(API_BASE_URL + depositEntity.getTo().getFlag())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                mEtReceiverAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                                mEtReceiverAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                            }
                        });

                switch (depositEntity.getDirection()) {
                    case FROM:
                        setTitle(getString(R.string.deposit_user_title));

//                        mEtSenderAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", depositEntity.getFromAmount()));
//                        mEtSenderAmount.setCurrency(depositEntity.getFrom().getCurrency());
//
//                        mEtReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", depositEntity.getToAmount()));
//                        mEtReceiverAmount.setCurrency(depositEntity.getTo().getCurrency());

                        mBtnAction.setVisibility(View.GONE);
                        break;
                    case TO:
                        setTitle(getString(R.string.deposit_agent_title));

//                        mEtSenderAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", depositEntity.getToAmount()));
//                        mEtSenderAmount.setCurrency(depositEntity.getTo().getCurrency());
//
//                        mEtReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", depositEntity.getFromAmount()));
//                        mEtReceiverAmount.setCurrency(depositEntity.getFrom().getCurrency());

                        if (depositEntity.getState() == DepositState.REJECTED) {
                            mBtnAction.setVisibility(View.GONE);
                            mBtnReject.setVisibility(View.GONE);
                        } else {
                            mBtnAction.setText(R.string.deposit_send_to_user);
                            mBtnReject.setVisibility(View.VISIBLE);
                        }
                        break;
                }

                mEtInterestRate.setText(String.valueOf(depositEntity.getInterestRate()));

                mEtSenderAmount.setEnabled(false);
                mEtReceiverAmount.setEnabled(false);

                changeAmountContainerOrientation();
                calculateTotals();

                break;
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

        if (getPresenter().getDepositEntity() != null)
            return;

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
    }

    public void retrieveReceiver(final UserEntity receiver) {

        if (getPresenter().getDepositEntity() == null)
            mTvUserRole.setText(R.string.deposit_agent_details);
        else {
            if (getPresenter().getDepositEntity().getDirection() == Direction.FROM) {
                mTvUserRole.setText(R.string.deposit_agent_details);
            } else
                mTvUserRole.setText(R.string.deposit_user_details);
        }

        mTvUserName.setText(receiver.getFullName());
        mTvUserPhone.setText(StringUtils.getFormattedPhoneNumber(receiver.getPhoneNumber(), receiver.getCountryCode()));
        Glide.with(this)
                .load(API_BASE_URL + receiver.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this)
                        .load(R.drawable.ic_user_default))
                .into(mIvUserAvatar);

        if (getPresenter().getDepositEntity() != null)
            return;

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
    public void openPinScreen(final UserEntity receiver,
                              final double fromAmount,
                              final double toAmount,
                              final DepositEntity depositEntity) {
        startActivity(EnterPinActivity.getLaunchIntent(
                this,
                receiver,
                fromAmount,
                toAmount,
                getPresenter().getActionType(),
                depositEntity)
        );
    }

    @Override
    public void onUserRejected(final DepositEntity depositEntity) {
        Toast.makeText(this, R.string.deposit_reject_success, Toast.LENGTH_SHORT).show();
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_ACTION, Action.UPDATE);
        intent.putExtra(EXTRA_DEPOSIT_ENTITY, depositEntity);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDepositSent(final DepositEntity depositEntity) {
        Toast.makeText(this, R.string.deposit_send_success, Toast.LENGTH_SHORT).show();
        final Intent intent = new Intent();
        intent.putExtra(EXTRA_ACTION, Action.DELETE);
        intent.putExtra(EXTRA_DEPOSIT_ENTITY, depositEntity);
        setResult(RESULT_OK, intent);
        finish();
    }

    private Handler mTimer = new Handler();

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

    private Runnable mConverter = () -> {
        double value = mEtSenderAmount.getAmount();
        getPresenter().convertIfNeed(value);
    };

    private void changeAmountContainerOrientation() {
        if (isTextLonger(mEtSenderAmount) || isTextLonger(mEtReceiverAmount)) {
            if (mLlAmountContainer.getOrientation() != LinearLayout.VERTICAL)
                mLlAmountContainer.setOrientation(LinearLayout.VERTICAL);
        } else {
            if (mLlAmountContainer.getOrientation() != LinearLayout.HORIZONTAL)
                mLlAmountContainer.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    @OnClick(R.id.button_action)
    public void onActionButtonClicked() {
        switch (getPresenter().getActionType()) {
            case CREATE_DEPOSIT:
                mElReceiverAmount.setError(null);
                mElSenderAmount.setError(null);
                getPresenter().sendDeposit(
                        mEtSenderAmount.getAmount(),
                        mEtReceiverAmount.getAmount(),
                        Integer.parseInt(mEtInterestRate.getText().toString().trim()));
                break;
            case SHOW_DEPOSIT:
                getPresenter().onConfirmClicked();
                break;
        }
    }

    @OnClick(R.id.button_reject)
    public void onRejectClicked() {
        getPresenter().onRejectClicked();
    }

    private boolean isTextLonger(TextInputEditText editText) {
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
