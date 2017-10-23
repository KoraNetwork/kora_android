package com.kora.android.presentation.ui.main.fragments.transactions.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.adapter.OnItemClickListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public final class CashViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    private OnItemClickListener mOnItemClickListener;

    @BindView(R.id.direction_icon) ImageView mDirectionIcon;
    @BindView(R.id.cash_type) TextView mCashType;
    @BindView(R.id.cash_amount) TextView mCashAmount;

    private final Unbinder mUnbinder;
    private Context mContext;

    public CashViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
        super(itemView);
        mUnbinder = ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mOnItemClickListener = onItemClickListener;
    }

    public void bind(TransactionEntity transactionEntity) {
        mDirectionIcon.setImageResource(R.drawable.ic_arrow_red);
        mCashType.setText(mContext.getString(R.string.transaction_history_cash_out));
        mCashAmount.setText(String.format(Locale.ENGLISH, "%1$.2f %2$s", transactionEntity.getFromAmount(), transactionEntity.getSender().getCurrency()));

    }
}
