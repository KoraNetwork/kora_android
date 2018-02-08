package com.kora.android.presentation.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kora.android.R;
import com.kora.android.presentation.model.TransactionEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Nullable
    private OnItemClickListener mOnItemClickListener;

    private List<TransactionEntity> mTransactions;

    public TransactionAdapter(@Nullable OnItemClickListener onItemClickListener) {
        mTransactions = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(itemView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TransactionViewHolder) holder).bind(mTransactions.get(position));
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    public void addItems(List<TransactionEntity> transactions) {
        int size = mTransactions.size();
        mTransactions.addAll(transactions);
        notifyItemRangeInserted(size, mTransactions.size());
    }

    public void setItems(List<TransactionEntity> transactions) {
        clearAll();
        addItems(transactions);
    }

    public void clearAll() {
        int size = mTransactions.size();
        mTransactions.clear();
        notifyItemRangeRemoved(0, size);
    }

    public List<TransactionEntity> getItems() {
        return mTransactions;
    }
}
