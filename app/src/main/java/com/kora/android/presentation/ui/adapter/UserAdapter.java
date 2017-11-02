package com.kora.android.presentation.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kora.android.R;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MAX_COUNT_OF_HEADERS = 2;
    private static final int MAX_SIZE_OF_RECENT = 10;
    @Nullable
    private OnItemClickListener mOnUserClickListener;

    private List mUsers;

    public UserAdapter() {
        mUsers = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewType == R.layout.item_contact)
            return createUserViewHolder(itemView);
        else
            return new SectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mUsers.get(position) instanceof UserEntity)
            ((UserViewHolder) holder).bind((UserEntity) mUsers.get(position));
        else {
            ((SectionViewHolder) holder).bind((UserEntity.Section) mUsers.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mUsers.get(position) instanceof UserEntity) {
            return R.layout.item_contact;
        }
        return R.layout.item_user_section;
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void addUser(Object o) {
        mUsers.add(o);
        notifyItemInserted(mUsers.size());
    }

    public void addUsers(List<UserEntity> users) {
        int size = mUsers.size();
        mUsers.addAll(users);
        notifyItemRangeInserted(size, users.size());
    }

    public void setOnUserClickListener(@Nullable OnItemClickListener onUserClickListener) {
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

//    private HashMap<String, Integer> calculateIndexesForName(List<UserEntity> items) {
//        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
//        for (int i = 0; i < items.size(); i++) {
//            UserEntity user = items.get(i);
//            String name = user.getLegalName() == null || user.getLegalName().length() == 0
//                    ? user.getUserName() == null || user.getUserName().length() == 0
//                    ? "##" : user.getUserName() : user.getLegalName();
//            String index = name.substring(0, 1);
//            index = index.toUpperCase();
//
//            if (!mapIndex.containsKey(index)) {
//                mapIndex.put(index, i);
//            }
//        }
//        return mapIndex;
//    }

    public List<UserEntity> getItems() {
        return mUsers;
    }

    public UserEntity getItem(int position) {
        return (UserEntity) mUsers.get(position);
    }

    public int getRawItemsCount() {
        if (mUsers.size() == 0) return 0;

        int size = MAX_SIZE_OF_RECENT + MAX_COUNT_OF_HEADERS;

        int sectionCount = 0;
        for (int position = 0; position <= size && position < mUsers.size(); position++) {
            if (mUsers.get(position) instanceof UserEntity.Section) {
                sectionCount++;
            }
            if (sectionCount == MAX_COUNT_OF_HEADERS) {
                return mUsers.size() - (position + 1);
            }
        }

        return mUsers.size() - sectionCount;
    }

}
