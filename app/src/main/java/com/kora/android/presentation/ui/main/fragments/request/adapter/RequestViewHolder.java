package com.kora.android.presentation.ui.main.fragments.request.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.presentation.enums.RequestState;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.direction_icon) ImageView mDirectionIcon;
    @BindView(R.id.sender_name) TextView mSenderName;
    @BindView(R.id.request_name) TextView mRequestName;
    @BindView(R.id.date) TextView mDate;
    @BindView(R.id.time) TextView mTime;
    @BindView(R.id.amount) TextView mAmount;
    @BindView(R.id.status) TextView mRequestStatus;

    private OnItemClickListener mOnItemClickListener;
    private Context mContext;

    private DecimalFormat mFormatter;

    public RequestViewHolder(View itemView, @Nullable OnItemClickListener onItemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mOnItemClickListener = onItemClickListener;
        mFormatter = new DecimalFormat("#,###,###,##0.00");
    }

    public void bind(RequestEntity requestEntity) {
        switch (requestEntity.getDirection()) {
            case FROM:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_red);
                mSenderName.setText(R.string.request_money_from_my_label);
                mAmount.setText(mContext.getString(R.string.transactions_amount, mFormatter.format(requestEntity.getFromAmount()), requestEntity.getFrom().getCurrency()));
                mRequestName.setText(mContext.getString(R.string.request_money_ask_money_label, requestEntity.getTo().getFullName()));
                break;
            case TO:
                mDirectionIcon.setImageResource(R.drawable.ic_arrow_green);
                mSenderName.setText(mContext.getString(R.string.request_money_from_label, requestEntity.getFrom().getFullName()));
                mAmount.setText(mContext.getString(R.string.transactions_amount, mFormatter.format(requestEntity.getToAmount()), requestEntity.getTo().getCurrency()));
                mRequestName.setText(R.string.request_money_ask_to_me);
                break;
        }
        showStatus(requestEntity.getState());

        mDate.setText(DateUtils.getFormattedDate("dd.MM.yyyy", requestEntity.getCreatedAt()));
        mTime.setText(DateUtils.getFormattedDate("hh:mm aa", requestEntity.getCreatedAt()));
    }

    private void showStatus(RequestState state) {
        switch (state) {
            case INPROGRESS:
                mRequestStatus.setText(R.string.request_money_status_in_progress);
                mRequestStatus.setTextColor(mContext.getResources().getColor(R.color.color_request_state_in_progress));
                break;

            case REJECTED:
                mRequestStatus.setText(R.string.request_money_status_rejected);
                mRequestStatus.setTextColor(mContext.getResources().getColor(R.color.color_request_state_rejected));
                break;
        }
    }

    @OnClick(R.id.root_view)
    public void onItemClicked() {
        if (mOnItemClickListener == null) return;
        mOnItemClickListener.onItemClicked(getAdapterPosition());
    }
}
