package com.kora.android.presentation.ui.borrow.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kora.android.R;
import com.kora.android.presentation.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class GuarantorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static final int MAX_SIZE = 3;
    @Nullable
    private OnItemClickListener mOnUserClickListener;

    private List<UserEntity> mUsers;

    public GuarantorsAdapter() {
        mUsers = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guarantor, parent, false);

        return createUserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((GuarantorViewHolder) holder).bind(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void addItem(UserEntity user) {
        if (user == null) return;
        if (mUsers.size() == MAX_SIZE) return;
        mUsers.add(user);
        notifyItemInserted(mUsers.size());
    }

    public void addItems(ArrayList<UserEntity> users) {
        if (users == null) return;
        if (users.size() + mUsers.size() > MAX_SIZE) return;
        int size = mUsers.size();
        mUsers.addAll(users);
        notifyItemRangeInserted(size, mUsers.size());
    }

    public void setOnClickListener(@Nullable OnItemClickListener onUserClickListener) {
        mOnUserClickListener = onUserClickListener;
    }

    private GuarantorViewHolder createUserViewHolder(View view) {
        GuarantorViewHolder viewHolder = new GuarantorViewHolder(view);
        if (mOnUserClickListener != null)
            viewHolder.setOnUserClickListener(mOnUserClickListener);
        return viewHolder;
    }

    public void clearAll() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    public List<UserEntity> getItems() {
        return mUsers;
    }

    public UserEntity getItem(int position) {
        return mUsers.get(position);
    }

    public void removeItem(int position) {
        mUsers.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener extends com.kora.android.presentation.ui.base.adapter.OnItemClickListener {
        void onDeleteItemClicked(int position);
    }
}
