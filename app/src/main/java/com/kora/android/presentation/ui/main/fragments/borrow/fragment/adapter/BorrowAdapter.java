package com.kora.android.presentation.ui.main.fragments.borrow.fragment.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kora.android.R;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BorrowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Nullable
    private OnItemClickListener mOnItemClickListener;

    private List<BorrowEntity> mBorrows;

    public BorrowAdapter(@Nullable OnItemClickListener onItemClickListener) {
        mBorrows = new ArrayList<>();
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new BorrowViewHolder(itemView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BorrowViewHolder) holder).bind(mBorrows.get(position));
    }

    @Override
    public int getItemCount() {
        return mBorrows.size();
    }

    public void addItems(List<BorrowEntity> borrowEntities) {
        int size = mBorrows.size();
        mBorrows.addAll(borrowEntities);
        notifyItemRangeInserted(size, mBorrows.size());
    }

    public void addItem(int position, BorrowEntity borrowEntity) {
        mBorrows.add(position, borrowEntity);
        notifyItemInserted(position);
    }

    public void setItems(ArrayList<BorrowEntity> borrows) {
        mBorrows.clear();
        mBorrows.addAll(borrows);
        notifyItemRangeInserted(0, mBorrows.size());
    }

    public List<BorrowEntity> getItems() {
        return mBorrows;
    }

    public void clearAll() {
        int size = mBorrows.size();
        mBorrows.clear();
        notifyItemRangeRemoved(0, size);
    }

    public BorrowEntity getItem(int position) {
        if (mBorrows == null || mBorrows.size() == 0) return null;
        return mBorrows.get(position);
    }
}
