package com.kora.android.presentation.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kora.android.R;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.views.fastscroll.FastScrollRecyclerViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements FastScrollRecyclerViewInterface {

    @Nullable
    private OnUserClickListener mOnUserClickListener;

    private List<UserEntity> mUsers;
    private HashMap<String, Integer> mIndexes;

    public UserAdapter() {
        mUsers = new ArrayList<>();
        mIndexes = new LinkedHashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, null);
        return createUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((UserViewHolder) holder).bind(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_contact;
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return mIndexes;
    }

    public void addUsers(List<UserEntity> users) {
        int size = mUsers.size();
        mUsers.addAll(users);
        mIndexes = calculateIndexesForName(mUsers);
        notifyItemRangeInserted(size, users.size());
    }

    public void setOnUserClickListener(@Nullable OnUserClickListener onUserClickListener) {
        mOnUserClickListener = onUserClickListener;
    }

    private UserViewHolder createUserViewHolder(View view) {
        UserViewHolder viewHolder = new UserViewHolder(view);
        if (mOnUserClickListener != null)
            viewHolder.setOnUserClickListener(mOnUserClickListener);
        return viewHolder;
    }

    public void clearAll() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    private HashMap<String, Integer> calculateIndexesForName(List<UserEntity> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        for (int i = 0; i < items.size(); i++) {
            UserEntity user = items.get(i);
            String name = user.getLegalName() == null || user.getLegalName().length() == 0
                    ? user.getUserName() == null || user.getUserName().length() == 0
                    ? "##" : user.getUserName() : user.getLegalName();
            String index = name.substring(0, 1);
            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }

    public List<UserEntity> getItems() {
        return mUsers;
    }

    public interface OnUserClickListener {

        void onUserClicked(int position);

    }
}
