package com.kora.android.presentation.ui.send.send;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.kora.android.data.network.Constants.API_BASE_URL;

public class SendMoneyActivity extends BaseActivity<SendMoneyPresenter> implements SendMoneyView {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.user_name) TextView mUserName;
    @BindView(R.id.user_phone) TextView mUserPhone;
    @BindView(R.id.his_suffix) TextView mHisSuffixText;
    @BindView(R.id.my_suffix) TextView mMySuffixText;
    @BindView(R.id.user_image) AppCompatImageView mUserImage;
    @BindView(R.id.edit_text_sender_amount) TextInputEditText mSenderAmount;
    @BindView(R.id.edit_text_receiver_amount) TextInputEditText mReceiverAmount;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_send_money;
    }

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final UserEntity userEntity) {
        final Intent intent = new Intent(baseActivity, SendMoneyActivity.class);
        intent.putExtra(Keys.Args.USER_ENTITY, userEntity);
        return intent;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_white);

        if (savedInstanceState == null) {
            initArguments();
            getPresenter().getCurrentUser();
        } else {
            getPresenter().setReceiver(savedInstanceState.getParcelable(Keys.Args.USER_RECEIVER));
            getPresenter().setSender(savedInstanceState.getParcelable(Keys.Args.USER_SENDER));
        }
    }

    private void initArguments() {
        UserEntity user = getIntent().getParcelableExtra(Keys.Args.USER_ENTITY);
        getPresenter().setReceiver(user);
    }

    public void retrieveReceiver(UserEntity user) {

        mToolbar.setTitle(getString(R.string.send_money_send_title, user.getFullName()));
        mUserName.setText(user.getFullName());
        mUserPhone.setText(user.getPhoneNumber());

        Glide.with(this)
                .load(API_BASE_URL + user.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this)
                        .load(R.drawable.ic_user))
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
        mReceiverAmount.setText(getString(R.string.converted_amount_value, amount));
        mHisSuffixText.setText(currency);
    }

    @Override
    public void emptySenderAmountError() {
        mSenderAmount.setError(getString(R.string.empty_sender_amount_error));
    }

    @Override
    public void emptyReceiverAmountError() {
        mSenderAmount.setError(getString(R.string.empty_receiver_amount_error));
    }

    @Override
    public void openPinScreen(UserEntity receiver, Double sAmount, Double rAmount) {

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
        mSenderAmount.setError(null);
        mReceiverAmount.setError(null);
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

    @OnClick(R.id.card_view_send_now)
    public void onSendClicked() {
        getPresenter().send(mSenderAmount.getText().toString().trim(), mReceiverAmount.getText().toString().trim());
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(Keys.Args.USER_RECEIVER, getPresenter().getReceiver());
        outState.putParcelable(Keys.Args.USER_SENDER, getPresenter().getSender());
        outState.putString(Keys.Args.SENDER_AMOUNT, mSenderAmount.getText().toString().trim());
        outState.putString(Keys.Args.RECEIVER_AMOUNT, mReceiverAmount.getText().toString().trim());
    }
}
