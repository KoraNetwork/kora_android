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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.Action;
import com.kora.android.presentation.enums.ActionType;
import com.kora.android.presentation.enums.RequestState;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.presentation.ui.common.enter_pin.EnterPinActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.kora.android.common.Keys.Args.ACTION_TYPE;
import static com.kora.android.common.Keys.Args.REQUEST_ENTITY;
import static com.kora.android.data.network.Constants.API_BASE_URL;

public class RequestDetailsActivity extends ToolbarActivity<RequestDetailsPresenter> implements RequestDetailsView {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.user_name) TextView mUserName;
    @BindView(R.id.user_phone) TextView mUserPhone;
    @BindView(R.id.his_suffix) TextView mHisSuffixText;
    @BindView(R.id.my_suffix) TextView mMySuffixText;
    @BindView(R.id.status) TextView mRequestStatus;
    @BindView(R.id.user_image) AppCompatImageView mUserImage;
    @BindView(R.id.edit_text_sender_amount) TextInputEditText mSenderAmount;
    @BindView(R.id.edit_layout_amount) TextInputLayout mSenderAmountContainer;
    @BindView(R.id.edit_text_receiver_amount) TextInputEditText mReceiverAmount;
    @BindView(R.id.edit_layout_converted_amount) TextInputLayout mReceiverAmountContainer;
    @BindView(R.id.edit_text_additional) EditText mEtAdditional;
    @BindView(R.id.action_button) Button mActionButton;
    @BindView(R.id.reject_button) TextView mRejectButton;

    private ActionType mActionType;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_request_details;
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

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final UserEntity userEntity, final ActionType actionType) {
        final Intent intent = new Intent(baseActivity, RequestDetailsActivity.class);
        intent.putExtra(Keys.Args.USER_ENTITY, userEntity);
        intent.putExtra(ACTION_TYPE, actionType);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final RequestEntity requestEntity) {
        final Intent intent = new Intent(baseActivity, RequestDetailsActivity.class);
        intent.putExtra(REQUEST_ENTITY, requestEntity);
        intent.putExtra(ACTION_TYPE, ActionType.SHOW_REQUEST);
        return intent;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        initArguments(savedInstanceState);

        if (savedInstanceState == null) {
            getPresenter().getCurrentUser();
            getPresenter().setAsResent();
        }

        initUI();
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null && bundle.containsKey(ACTION_TYPE)) {
            mActionType = (ActionType) bundle.getSerializable(ACTION_TYPE);
        }
        if (bundle != null && bundle.containsKey(REQUEST_ENTITY)) {
            RequestEntity request = bundle.getParcelable(REQUEST_ENTITY);
            getPresenter().setRequest(request);
        }
        if (bundle != null && bundle.containsKey(Keys.Args.USER_RECEIVER)) {
            getPresenter().setReceiver(bundle.getParcelable(Keys.Args.USER_RECEIVER));
        }
        if (bundle != null && bundle.containsKey(Keys.Args.USER_SENDER)) {
            getPresenter().setSender(bundle.getParcelable(Keys.Args.USER_SENDER));
        }

        if (getIntent() != null) {
            mActionType = (ActionType) getIntent().getSerializableExtra(ACTION_TYPE);
            getPresenter().setRequest(getIntent().getParcelableExtra(REQUEST_ENTITY));
            getPresenter().setReceiver(getIntent().getParcelableExtra(Keys.Args.USER_ENTITY));
        }
    }

    private void initUI() {
        switch (mActionType) {
            case CREATE_REQUEST:
                setTitle(getString(R.string.send_money_request_title, getPresenter().getReceiver().getUserName()));
                mActionButton.setText(R.string.send_money_request_button_label);
                break;
            case SEND_MONEY:
                setTitle(getString(R.string.send_money_send_title, getPresenter().getReceiver().getUserName()));
                mActionButton.setText(R.string.send_money_send_button_label);
                break;
            case SHOW_REQUEST:
                RequestEntity request = getPresenter().getRequest();
                if (request == null) return;
                switch (request.getDirection()) {
                    case FROM:
                        setTitle(getString(R.string.send_money_request_from, request.getTo().getFullName()));
                        mActionButton.setVisibility(View.GONE);
                        mRequestStatus.setVisibility(View.VISIBLE);
                        mRequestStatus.setText(request.getState().text());
                        mReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", request.getToAmount()));
                        mHisSuffixText.setText(request.getTo().getCurrency());
                        mSenderAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", request.getFromAmount()));

                        if (request.getState() == RequestState.REJECTED) {
                            mRequestStatus.setTextColor(getResources().getColor(R.color.color_text_red));
                        } else {
                            mRequestStatus.setTextColor(getResources().getColor(R.color.color_text_blue));
                        }
                        break;
                    case TO:
                        setTitle(getString(R.string.send_money_request_to, request.getFrom().getFullName()));
                        mSenderAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", request.getToAmount()));
                        mHisSuffixText.setText(request.getFrom().getCurrency());
                        mReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", request.getFromAmount()));

                        if (request.getState() == RequestState.REJECTED) {
                            mRequestStatus.setTextColor(getResources().getColor(R.color.color_text_red));
                            mRequestStatus.setVisibility(View.VISIBLE);
                            mRequestStatus.setText(request.getState().text());
                            mActionButton.setVisibility(View.GONE);
                            mRejectButton.setVisibility(View.GONE);
                        } else {
                            mActionButton.setText(R.string.send_money_confirm_button_label);
                            mRejectButton.setVisibility(View.VISIBLE);
                        }

                        break;
                }

                mEtAdditional.setText(request.getAdditionalNote());
                mSenderAmount.setEnabled(false);
                mEtAdditional.setEnabled(false);
                mReceiverAmount.setEnabled(false);

                break;
        }
    }

    private void showReceiverCurrencyFlag(UserEntity userEntity) {
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + userEntity.getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mReceiverAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mReceiverAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });
    }

    public void retrieveReceiver(UserEntity user) {
        mHisSuffixText.setText(user.getCurrency());

        setTitle(getString(R.string.send_money_send_title, user.getFullName()));
        mUserName.setText(user.getFullName());
        mUserPhone.setText(user.getPhoneNumber());

        Glide.with(this)
                .load(API_BASE_URL + user.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this)
                        .load(R.drawable.ic_user_default))
                .into(mUserImage);

        showReceiverCurrencyFlag(user);

    }

    @Override
    public void showConvertedCurrency(Double amount, String currency) {
        mReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", amount));
//        mHisSuffixText.setText(currency);
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
    public void openPinScreen(UserEntity receiver, Double sAmount, Double rAmount, String requestId) {
        startActivity(EnterPinActivity.getLaunchIntent(this,
                receiver, sAmount, rAmount, mActionType, requestId));
    }

    @Override
    public void onConfirmClicked() {
        Toast.makeText(this, R.string.send_money_confirm_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserRejected(RequestEntity requestEntity) {
        Toast.makeText(this, R.string.send_money_reject_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(Keys.Extras.EXTRA_ACTION, Action.UPDATE);
        intent.putExtra(Keys.Extras.EXTRA_REQUEST_ENTITY, requestEntity);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestSend(RequestEntity requestEntity) {
        Toast.makeText(this, R.string.send_money_request_send_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(Keys.Extras.EXTRA_ACTION, Action.DELETE);
        intent.putExtra(Keys.Extras.EXTRA_REQUEST_ENTITY, requestEntity);
        setResult(RESULT_OK, intent);
        finish();
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

    @OnClick(R.id.action_button)
    public void onActionButtonClicked() {
        switch (mActionType) {
            case CREATE_REQUEST:
                getPresenter().sendRequest(
                        mSenderAmount.getText().toString().trim(),
                        mReceiverAmount.getText().toString().trim(),
                        mEtAdditional.getText().toString().trim());
                break;
            case SEND_MONEY:
                getPresenter().sendMoney(
                        mSenderAmount.getText().toString().trim(),
                        mReceiverAmount.getText().toString().trim(),
                        mEtAdditional.getText().toString().trim());
                break;
            case SHOW_REQUEST:
                getPresenter().onConfirmClicked();
                break;
        }

    }

    @OnClick(R.id.reject_button)
    public void onRejectClicked() {
        getPresenter().onRejectClicked();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(Keys.Args.USER_RECEIVER, getPresenter().getReceiver());
        outState.putParcelable(Keys.Args.USER_SENDER, getPresenter().getSender());
        outState.putString(Keys.Args.SENDER_AMOUNT, mSenderAmount.getText().toString().trim());
        outState.putString(Keys.Args.RECEIVER_AMOUNT, mReceiverAmount.getText().toString().trim());

        outState.putSerializable(ACTION_TYPE, mActionType);
        outState.putParcelable(REQUEST_ENTITY, getPresenter().getRequest());
    }

}
