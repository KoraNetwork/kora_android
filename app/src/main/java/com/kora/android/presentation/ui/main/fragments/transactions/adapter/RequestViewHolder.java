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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public final class RequestViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.direction_icon) ImageView mDirectionIcon;
    @BindView(R.id.sender_name) TextView mSenderName;

    private OnItemClickListener mOnItemClickListener;

    private Context mContext;
    private Unbinder mUnbinder;


    public RequestViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
        super(itemView);
        mUnbinder = ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mOnItemClickListener = onItemClickListener;
    }

    public void bind(TransactionEntity transactionEntity) {
        switch (transactionEntity.getTransactionType()) {
            case SEND:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_red);
                mSenderName.setText(transactionEntity.getReceiver().getUserName());
                break;
            case BORROW:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_gr);
                mSenderName.setText(transactionEntity.getSender().getUserName());
                break;
        }
    }
}
