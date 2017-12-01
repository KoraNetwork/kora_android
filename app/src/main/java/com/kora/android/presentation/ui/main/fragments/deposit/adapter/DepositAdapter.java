package com.kora.android.presentation.ui.main.fragments.deposit.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kora.android.R;
import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;
import com.kora.android.presentation.ui.main.fragments.request.adapter.RequestViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DepositAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Nullable
    private OnItemClickListener mOnItemClickListener;
    private List<DepositEntity> mDepositList;

    public DepositAdapter(@Nullable final OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        mDepositList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new DepositViewHolder(itemView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((DepositViewHolder) holder).bind(mDepositList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDepositList.size();
    }

    public void addItems(List<DepositEntity> depositList) {
        int size = mDepositList.size();
        mDepositList.addAll(depositList);
        notifyItemRangeInserted(size, mDepositList.size());
    }

    public List<DepositEntity> getItems() {
        return mDepositList;
    }

    public DepositEntity getItemByPosition(final int position) {
        return mDepositList.get(position);
    }

    public void clearAll() {
        final int size = mDepositList.size();
        mDepositList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void changeItemState(final DepositEntity depositEntity) {
        for (DepositEntity entity : mDepositList) {
            if (Objects.equals(depositEntity.getId(), entity.getId())) {
                final int position = mDepositList.indexOf(entity);
                entity.setState(depositEntity.getState());
                notifyItemChanged(position);
                return;
            }
        }
    }

    public void addItem(final DepositEntity depositEntity) {
        mDepositList.add(0, depositEntity);
        notifyItemInserted(0);
    }
}
