package com.kora.android.presentation.ui.common.send_to;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.presentation.ui.common.enter_pin.EnterPinActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.kora.android.common.Keys.Args.TRANSACTION_TYPE;
import static com.kora.android.data.network.Constants.API_BASE_URL;

public class SendMoneyActivity extends ToolbarActivity<SendMoneyPresenter> implements SendMoneyView {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.user_name) TextView mUserName;
    @BindView(R.id.user_phone) TextView mUserPhone;
    @BindView(R.id.his_suffix) TextView mHisSuffixText;
    @BindView(R.id.my_suffix) TextView mMySuffixText;
    @BindView(R.id.user_image) AppCompatImageView mUserImage;
    @BindView(R.id.edit_text_sender_amount) TextInputEditText mSenderAmount;
    @BindView(R.id.edit_layout_amount) TextInputLayout mSenderAmountContainer;
    @BindView(R.id.edit_text_receiver_amount) TextInputEditText mReceiverAmount;
    @BindView(R.id.edit_layout_converted_amount) TextInputLayout mReceiverAmountContainer;
    @BindView(R.id.text_view_send_request) TextView mTvSendRequest;
    @BindView(R.id.edit_text_additional) EditText mEtAdditional;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_send_money;
    }

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitleRes() {
        return R.string.send_money_title;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final UserEntity userEntity,
                                         final TransactionType transactionType) {
        final Intent intent = new Intent(baseActivity, SendMoneyActivity.class);
        intent.putExtra(Keys.Args.USER_ENTITY, userEntity);
        intent.putExtra(TRANSACTION_TYPE, transactionType.toString());
        return intent;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        initArguments(savedInstanceState);

        if (savedInstanceState == null) {
            initArguments();
            getPresenter().getCurrentUser();
            getPresenter().setAsResent();
        } else {
            getPresenter().setReceiver(savedInstanceState.getParcelable(Keys.Args.USER_RECEIVER));
            getPresenter().setSender(savedInstanceState.getParcelable(Keys.Args.USER_SENDER));
        }

        initUI();
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(TRANSACTION_TYPE))
                getPresenter().setTransactionType(bundle.getString(TRANSACTION_TYPE));
        }
        if (getIntent() != null) {
            getPresenter().setTransactionType(getIntent().getStringExtra(TRANSACTION_TYPE));
        }
    }

    private void initArguments() {
        UserEntity user = getIntent().getParcelableExtra(Keys.Args.USER_ENTITY);
        getPresenter().setReceiver(user);
    }

    private void initUI() {
        if (getPresenter().getTransactionType().equals(TransactionType.SEND)) {
            setTitle(getString(R.string.send_money_send_title, getPresenter().getReceiver().getUserName()));
            mTvSendRequest.setText(R.string.send_money_send_button_label);
        } else if (getPresenter().getTransactionType().equals(TransactionType.REQUEST)) {
            setTitle(getString(R.string.send_money_request_title, getPresenter().getReceiver().getUserName()));
            mTvSendRequest.setText(R.string.send_money_request_button_label);
        }
    }

    public void retrieveReceiver(UserEntity user) {

        setTitle(getString(R.string.send_money_send_title, user.getFullName()));
        mUserName.setText(user.getFullName());
        mUserPhone.setText(user.getPhoneNumber());

        Glide.with(this)
                .load(API_BASE_URL + user.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this)
                        .load(R.drawable.ic_user_default))
                .into(mUserImage);

        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + user.getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mReceiverAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mReceiverAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });

    }

    @Override
    public void showConvertedCurrency(Double amount, String currency) {
        mReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", amount));
        mHisSuffixText.setText(currency);
    }

    @Override
    public void emptySenderAmountError() {
        mSenderAmountContainer.setError(getString(R.string.empty_sender_amount_error));
    }

    @Override
    public void emptyReceiverAmountError() {
        mReceiverAmountContainer.setError(getString(R.string.empty_receiver_amount_error));
    }

    @Override
    public void openPinScreen(UserEntity receiver, Double sAmount, Double rAmount) {
        startActivity(EnterPinActivity.getLaunchIntent(this,
                receiver, sAmount, rAmount,
                getPresenter().getTransactionType()));
    }

    @Override
    public void retrieveSender(UserEntity userEntity) {
        mMySuffixText.setText(userEntity.getCurrency());
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + userEntity.getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mSenderAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mSenderAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });
    }

    Handler timer = new Handler();

    @OnTextChanged(R.id.edit_text_sender_amount)
    public void onAmountChanged() {
        mSenderAmountContainer.setError(null);
        mReceiverAmountContainer.setError(null);
        timer.removeCallbacks(converter);
        timer.postDelayed(converter, 500);
    }

    Runnable converter = new Runnable() {
        @Override
        public void run() {
            String val = mSenderAmount.getText().toString().trim();
            if (val.equals("")) mReceiverAmount.setText("");
            else
                getPresenter().convertIfNeed(val);
        }
    };

    @OnClick(R.id.card_view_send_request)
    public void onSendClicked() {
        getPresenter().sendOrRequest(
                mSenderAmount.getText().toString().trim(),
                mReceiverAmount.getText().toString().trim(),
                mEtAdditional.getText().toString().trim());
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(Keys.Args.USER_RECEIVER, getPresenter().getReceiver());
        outState.putParcelable(Keys.Args.USER_SENDER, getPresenter().getSender());
        outState.putString(Keys.Args.SENDER_AMOUNT, mSenderAmount.getText().toString().trim());
        outState.putString(Keys.Args.RECEIVER_AMOUNT, mReceiverAmount.getText().toString().trim());

        outState.putString(TRANSACTION_TYPE, getPresenter().getTransactionType().toString());
    }
}
