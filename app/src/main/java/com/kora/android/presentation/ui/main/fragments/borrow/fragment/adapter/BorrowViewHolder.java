package com.kora.android.presentation.ui.main.fragments.borrow.fragment.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.direction_icon) ImageView mDirectionIcon;
    @BindView(R.id.sender_name) TextView mSenderName;
    @BindView(R.id.receiver_name) TextView mReceiverName;
    @BindView(R.id.date) TextView mDate;
    @BindView(R.id.type) TextView mType;
    @BindView(R.id.amount) TextView mAmount;

    private OnItemClickListener mOnItemClickListener;

    private Context mContext;

    public BorrowViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mOnItemClickListener = onItemClickListener;
    }

    public void bind(BorrowEntity borrowEntity) {
        switch (borrowEntity.getDirection()) {
            case FROM:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_red);
                mSenderName.setText(mContext.getString(R.string.transaction_history_from_me));
                mReceiverName.setText(mContext.getString(R.string.transaction_history_to, borrowEntity.getReceiver().getUserName()));
                mAmount.setText(mContext.getString(R.string.transactions_amount, borrowEntity.getFromAmount(), borrowEntity.getSender().getCurrency()));
                break;
            case TO:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_gr);
                mSenderName.setText(mContext.getString(R.string.transaction_history_from, borrowEntity.getSender().getUserName()));
                mReceiverName.setText(mContext.getString(R.string.transaction_history_to_me));
                mAmount.setText(mContext.getString(R.string.transactions_amount, borrowEntity.getToAmount(), borrowEntity.getReceiver().getCurrency()));
                break;
        }

//        mType.setText(getTypeResource(borrowEntity.getTransactionType()));
        mDate.setText(DateUtils.getFormattedDate("dd.MM.yyyy", borrowEntity.getCreatedAt()));
    }

    @StringRes
    private int getTypeResource(TransactionType transactionType) {
        switch (transactionType) {
            case BORROW:
                return R.string.transaction_history_type_borrow;
            case SEND:
                return R.string.transaction_history_type_send;
            case REQUEST:
                return R.string.transaction_history_type_request;
            case DEPOSIT:
                return R.string.transaction_history_type_deposit;
        }
        return 0;
    }
}