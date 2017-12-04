package com.kora.android.presentation.ui.main.fragments.deposit_withdraw.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kora.android.R;
import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DepositWithdrawAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Nullable
    private OnItemClickListener mOnItemClickListener;
    private List<DepositWithdrawEntity> mDepositWithdrawList;

    public DepositWithdrawAdapter(@Nullable final OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        mDepositWithdrawList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new DepositWithdrawViewHolder(itemView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((DepositWithdrawViewHolder) holder).bind(mDepositWithdrawList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDepositWithdrawList.size();
    }

    public void addItems(final List<DepositWithdrawEntity> depositWithdrawList) {
        int size = mDepositWithdrawList.size();
        mDepositWithdrawList.addAll(depositWithdrawList);
        notifyItemRangeInserted(size, mDepositWithdrawList.size());
    }

    public List<DepositWithdrawEntity> getItems() {
        return mDepositWithdrawList;
    }

    public DepositWithdrawEntity getItemByPosition(final int position) {
        return mDepositWithdrawList.get(position);
    }

    public void clearAll() {
        final int size = mDepositWithdrawList.size();
        mDepositWithdrawList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void changeItemState(final DepositWithdrawEntity depositWithdrawEntity) {
        for (DepositWithdrawEntity entity : mDepositWithdrawList) {
            if (Objects.equals(depositWithdrawEntity.getId(), entity.getId())) {
                final int position = mDepositWithdrawList.indexOf(entity);
                entity.setState(depositWithdrawEntity.getState());
                notifyItemChanged(position);
                return;
            }
        }
    }

    public void addItem(final DepositWithdrawEntity depositWithdrawEntity) {
        mDepositWithdrawList.add(0, depositWithdrawEntity);
        notifyItemInserted(0);
    }
}
