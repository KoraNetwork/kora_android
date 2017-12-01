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

//    private static final int REQUEST_TYPE = 0;
//    private static final int DEPOSIT_TYPE = 1;

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
//        switch (viewType) {
//            case REQUEST_TYPE:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
                return new TransactionViewHolder(itemView, mOnItemClickListener);
//            case DEPOSIT_TYPE:
//                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_cash, parent, false);
//                return new CashViewHolder(itemView, mOnItemClickListener);
//        }
//        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof TransactionViewHolder) {
            ((TransactionViewHolder) holder).bind(mTransactions.get(position));
//        } else if (holder instanceof CashViewHolder){
//            ((CashViewHolder) holder).bind(mTransactions.get(position));
//        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        TransactionEntity transaction = mTransactions.get(position);
//        switch (transaction.getTransactionType()) {
//            case BORROWFUND:
//            case BORROWPAYBACK:
//            case SEND:
//            case REQUEST:
//                return REQUEST_TYPE;
//            case DEPOSIT:
//                return DEPOSIT_TYPE;
//            default:
//                return super.getItemViewType(position);
//        }
//    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    public void addItems(List<TransactionEntity> transactions) {
        int size = mTransactions.size();
        mTransactions.addAll(transactions);
        notifyItemRangeInserted(size, mTransactions.size());
    }

    public void setItems(ArrayList<TransactionEntity> transactions) {
        mTransactions.clear();
        mTransactions.addAll(transactions);
        notifyItemRangeInserted(0, mTransactions.size());
    }

    public List<TransactionEntity> getItems() {
        return mTransactions;
    }

    public void clearAll() {
        int size = mTransactions.size();
        mTransactions.clear();
        notifyItemRangeRemoved(0, size);
    }
}
