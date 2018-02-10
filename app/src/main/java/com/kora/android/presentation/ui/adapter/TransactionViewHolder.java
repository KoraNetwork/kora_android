package com.kora.android.presentation.ui.adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.presentation.enums.TransactionState;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.direction_icon) ImageView mDirectionIcon;
    @BindView(R.id.sender_name) TextView mSenderName;
    @BindView(R.id.receiver_name) TextView mReceiverName;
    @BindView(R.id.date) TextView mDate;
    @BindView(R.id.time) TextView mTime;
    @BindView(R.id.type) TextView mType;
    @BindView(R.id.state) TextView mState;
    @BindView(R.id.amount) TextView mAmount;

    private OnItemClickListener mOnItemClickListener;

    private Context mContext;
    private Unbinder mUnbinder;

    private DecimalFormat mFormatter;


    public TransactionViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
        super(itemView);
        mUnbinder = ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mOnItemClickListener = onItemClickListener;
        mFormatter = new DecimalFormat("#,###,###,##0.00");
    }

    public void bind(TransactionEntity transactionEntity) {
        switch (transactionEntity.getDirection()) {
            case FROM:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_red);
                mSenderName.setText(mContext.getString(R.string.transaction_history_from_me));
                mReceiverName.setText(mContext.getString(R.string.transaction_history_to, transactionEntity.getReceiver().getUserName()));
                mAmount.setText(mContext.getString(R.string.transactions_amount, mFormatter.format(transactionEntity.getFromAmount()), transactionEntity.getSender().getCurrency()));
                break;
            case TO:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_green);
                mSenderName.setText(mContext.getString(R.string.transaction_history_from, transactionEntity.getSender().getUserName()));
                mReceiverName.setText(mContext.getString(R.string.transaction_history_to_me));
                mAmount.setText(mContext.getString(R.string.transactions_amount, mFormatter.format(transactionEntity.getToAmount()), transactionEntity.getReceiver().getCurrency()));
                break;
        }

        mType.setText(getTypeString(transactionEntity.getTransactionType()));
        mState.setVisibility(View.VISIBLE);
        mState.setText(getStateString(transactionEntity.getTransactionState()));
        mState.setTextColor(ContextCompat.getColor(mContext, getStateColor(transactionEntity.getTransactionState())));
        mDate.setText(DateUtils.getFormattedDate("dd.MM.yyyy", transactionEntity.getUpdatedAt()));
        mTime.setText(DateUtils.getFormattedDate("hh:mm aa", transactionEntity.getUpdatedAt()));
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

    @OnClick(R.id.root_view)
    public void onItemClicked() {
        if (mOnItemClickListener == null) return;
        mOnItemClickListener.onItemClicked(getAdapterPosition());
    }
}
