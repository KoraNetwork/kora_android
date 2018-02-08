package com.kora.android.presentation.ui.main.fragments.borrow.fragment.adapter;

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
import com.kora.android.presentation.enums.BorrowState;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BorrowViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.direction_icon) ImageView mDirectionIcon;
    @BindView(R.id.sender_name) TextView mSenderName;
    @BindView(R.id.receiver_name) TextView mReceiverName;
    @BindView(R.id.date) TextView mDate;
    @BindView(R.id.time) TextView mTime;
    @BindView(R.id.type) TextView mType;
    @BindView(R.id.state) TextView mState;
    @BindView(R.id.amount) TextView mAmount;

    private OnItemClickListener mOnItemClickListener;

    private DecimalFormat mFormatter;

    private Context mContext;

    public BorrowViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mOnItemClickListener = onItemClickListener;
        mFormatter = new DecimalFormat("#,###,###,##0.00");
    }

    public void bind(BorrowEntity borrowEntity) {
        switch (borrowEntity.getDirection()) {
            case FROM:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_red);
                mSenderName.setText(mContext.getString(R.string.transaction_history_from_me));
                mReceiverName.setText(mContext.getString(R.string.transaction_history_to, borrowEntity.getReceiver().getFullName()));
                mAmount.setText(mContext.getString(R.string.transactions_amount, mFormatter.format(borrowEntity.getFromAmount()), borrowEntity.getSender().getCurrency()));
                break;
            case TO:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_green);
                mSenderName.setText(mContext.getString(R.string.transaction_history_from, borrowEntity.getSender().getUserName()));
                mReceiverName.setText(mContext.getString(R.string.transaction_history_to_me));
                mAmount.setText(mContext.getString(R.string.transactions_amount, mFormatter.format(borrowEntity.getToAmount()), borrowEntity.getReceiver().getCurrency()));
                break;
            case GUARANTOR:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_green);
                mSenderName.setText(mContext.getString(R.string.transaction_history_from, borrowEntity.getSender().getUserName()));
                mReceiverName.setText(mContext.getString(R.string.transaction_history_to, borrowEntity.getReceiver().getFullName()));
                mAmount.setText(mContext.getString(R.string.transactions_amount, mFormatter.format(borrowEntity.getFromAmount()), borrowEntity.getSender().getCurrency()));
                break;
        }

        mType.setVisibility(View.GONE);
        mState.setText(getStateString(borrowEntity.getState()));
        mState.setTextColor(ContextCompat.getColor(mContext, getStateColor(borrowEntity.getState())));
        mDate.setText(DateUtils.getFormattedDate("dd.MM.yyyy", borrowEntity.getCreatedAt()));
        mTime.setText(DateUtils.getFormattedDate("hh:mm aa", borrowEntity.getCreatedAt()));
    }

    @StringRes
    private int getStateString(final BorrowState borrowState) {
        switch (borrowState) {
            case ONGOING:
                return R.string.borrow_state_ongoing;
            case AGREED:
                return R.string.borrow_state_agreed;
            case PENDING:
                return R.string.borrow_state_pending;
            case REJECTED:
                return R.string.borrow_state_rejected;
            case EXPIRED:
                return R.string.borrow_state_expired;
            case OVERDUE:
                return R.string.borrow_state_overdue;
        }
        return 0;
    }

    @ColorRes
    private int getStateColor(final BorrowState borrowState) {
        switch (borrowState) {
            case ONGOING:
                return R.color.color_borrow_state_ongoing;
            case AGREED:
                return R.color.color_borrow_state_agreed;
            case REJECTED:
                return R.color.color_borrow_state_rejected;
            case EXPIRED:
                return R.color.color_borrow_state_expired;
            case PENDING:
                return R.color.color_borrow_state_pending;
            case OVERDUE:
                return R.color.color_borrow_state_overdue;
        }
        return 0;
    }

    @OnClick(R.id.root_view)
    public void onRootClicked() {
        if (mOnItemClickListener == null) return;
        mOnItemClickListener.onItemClicked(getAdapterPosition());
    }
}