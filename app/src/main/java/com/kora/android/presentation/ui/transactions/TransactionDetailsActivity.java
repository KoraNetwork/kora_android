package com.kora.android.presentation.ui.transactions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kora.android.R;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.TransactionState;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.views.currency.CurrencyEditText;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.TRANSACTION_ENTITY;
import static com.kora.android.common.Keys.Args.TRANSACTION_ID;
import static com.kora.android.data.network.Constants.API_BASE_URL;
import static com.kora.android.data.web3j.Constants.RINKKEBY_TX_URL;

public class TransactionDetailsActivity extends ToolbarActivity<TransactionDetailsPresenter> implements TransactionDetailsView,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSrlRefresh;
    @BindView(R.id.ll_root_view)
    LinearLayout mLlRootView;
    @BindView(R.id.text_view_type)
    TextView mTvType;
    @BindView(R.id.text_view_state)
    TextView mTvState;
    @BindView(R.id.image_view_sender_avatar)
    ImageView mIvSenderAvatar;
    @BindView(R.id.text_view_sender_name)
    TextView mTvSenderName;
    @BindView(R.id.text_view_sender_phone_number)
    TextView mTvSenderPhone;
    @BindView(R.id.image_view_receiver_avatar)
    ImageView mIvReceiverAvatar;
    @BindView(R.id.text_view_receiver_name)
    TextView mTvReceiverName;
    @BindView(R.id.text_view_receiver_phone_number)
    TextView mTvReceiverPhone;
    @BindView(R.id.edit_text_sender_amount)
    CurrencyEditText mEtSenderAmount;
    @BindView(R.id.edit_text_receiver_amount)
    CurrencyEditText mEtReceiverAmount;
    @BindView(R.id.text_view_date)
    TextView mTvDate;
    @BindView(R.id.text_view_time)
    TextView mTvTime;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final String transactionId) {
        final Intent intent = new Intent(baseActivity, TransactionDetailsActivity.class);
        intent.putExtra(TRANSACTION_ID, transactionId);
        return intent;
    }
    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_transaction_details;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitleRes() {
        return R.string.transaction_details_title;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        initArguments(savedInstanceState);
        initUI();

        if (getPresenter().getTransactionEntity() != null) {
            showTransaction(getPresenter().getTransactionEntity());
        } else {
            getPresenter().getTransaction();
        }
    }

    private void initArguments(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TRANSACTION_ID)) {
                getPresenter().setTransactionId(savedInstanceState.getString(TRANSACTION_ID));
            }
            if (savedInstanceState.containsKey(TRANSACTION_ENTITY)) {
                getPresenter().setTransactionEntity(savedInstanceState.getParcelable(TRANSACTION_ENTITY));
            }
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra(TRANSACTION_ID)) {
                getPresenter().setTransactionId(getIntent().getStringExtra(TRANSACTION_ID));
            }
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TRANSACTION_ID, getPresenter().getTransactionId());
        outState.putParcelable(TRANSACTION_ENTITY, getPresenter().getTransactionEntity());
    }

    private void initUI() {
        mSrlRefresh.setOnRefreshListener(this);
    }

    @Override
    public void showTransaction(final TransactionEntity transactionEntity) {
        if (transactionEntity == null) return;;
        mLlRootView.setVisibility(View.VISIBLE);

        mTvType.setText(getTypeString(transactionEntity.getTransactionType()));
        mTvState.setText(getStateString(transactionEntity.getTransactionState()));
        mTvState.setTextColor(getResources().getColor(getStateColor(transactionEntity.getTransactionState())));

        Glide.with(this)
                .load(API_BASE_URL + transactionEntity.getSender().getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this)
                        .load(R.drawable.ic_user_default))
                .into(mIvSenderAvatar);
        mTvSenderName.setText(transactionEntity.getSender().getFullName());
        mTvSenderPhone.setText(StringUtils.getFormattedPhoneNumber(
                transactionEntity.getSender().getPhoneNumber(),
                transactionEntity.getSender().getCountryCode()));

        Glide.with(this)
                .load(API_BASE_URL + transactionEntity.getReceiver().getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this)
                        .load(R.drawable.ic_user_default))
                .into(mIvReceiverAvatar);
        mTvReceiverName.setText(transactionEntity.getReceiver().getFullName());
        mTvReceiverPhone.setText(StringUtils.getFormattedPhoneNumber(
                transactionEntity.getReceiver().getPhoneNumber(),
                transactionEntity.getReceiver().getCountryCode()));


        mEtSenderAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", transactionEntity.getFromAmount()));
        mEtSenderAmount.setCurrency(transactionEntity.getSender().getCurrency());
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + transactionEntity.getSender().getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mEtSenderAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                new BitmapDrawable(getResources(), resource), null, null, null);
                        mEtSenderAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });

        mEtReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", transactionEntity.getToAmount()));
        mEtReceiverAmount.setCurrency(transactionEntity.getReceiver().getCurrency());
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + transactionEntity.getReceiver().getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mEtReceiverAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                new BitmapDrawable(getResources(), resource), null, null, null);
                        mEtReceiverAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });

        mTvDate.setText(DateUtils.getFormattedDate("dd.MM.yyyy", transactionEntity.getUpdatedAt()));
        mTvTime.setText(DateUtils.getFormattedDate("hh:mm aa", transactionEntity.getUpdatedAt()));
    }

    @Override
    public void enableAndShowRefreshIndicator(final boolean enableIndicator,
                                              final boolean showIndicator) {
        mSrlRefresh.setEnabled(enableIndicator);
        mSrlRefresh.setRefreshing(showIndicator);
    }

    @Override
    public void onRefresh() {
        getPresenter().getTransaction();
    }

    @OnClick(R.id.ll_transaction_hash)
    public void onClickTransactionHash() {
        if (getPresenter().getTransactionEntity() == null) return;
        final String transactionHash = getPresenter().getTransactionEntity().getTransactionHash().get(0);
        final String url = RINKKEBY_TX_URL + transactionHash;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @StringRes
    private int getTypeString(final TransactionType transactionType) {
        switch (transactionType) {
            case SEND:
                return R.string.transaction_history_type_send;
            case REQUEST:
                return R.string.transaction_history_type_request;
            case BORROWFUND:
                return R.string.transaction_history_type_borrow_fund;
            case BORROWPAYBACK:
                return R.string.transaction_history_type_borrow_pay_back;
            case DEPOSIT:
                return R.string.transaction_history_type_deposit;
            case WITHDRAW:
                return R.string.transaction_history_type_withdraw;
        }
        return 0;
    }

    @StringRes
    private int getStateString(final TransactionState transactionState) {
        switch (transactionState) {
            case PENDING:
                return R.string.transaction_history_state_pending;
            case SUCCESS:
                return R.string.transaction_history_state_success;
            case ERROR:
                return R.string.transaction_history_state_error;
        }
        return 0;
    }

    @ColorRes
    private int getStateColor(final TransactionState transactionState) {
        switch (transactionState) {
            case PENDING:
                return R.color.color_transaction_state_pending;
            case SUCCESS:
                return R.color.color_transaction_state_success;
            case ERROR:
                return R.color.color_transaction_state_error;
        }
        return 0;
    }
}
