package com.kora.android.presentation.ui.main.fragments.request.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kora.android.R;
import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Nullable
    private OnItemClickListener mOnItemClickListener;

    private List<RequestEntity> mRequests;

    public RequestAdapter(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        mRequests = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(itemView, mOnItemClickListener);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RequestViewHolder) holder).bind(mRequests.get(position));
    }

    @Override
    public int getItemCount() {
        return mRequests.size();
    }

    public void addItems(List<RequestEntity> requests) {
        int size = mRequests.size();
        mRequests.addAll(requests);
        notifyItemRangeInserted(size, mRequests.size());
    }

    public void setItems(ArrayList<RequestEntity> requests) {
        mRequests.clear();
        mRequests.addAll(requests);
        notifyItemRangeChanged(0, mRequests.size());
    }

    public List<RequestEntity> getItems() {
        return mRequests;
    }

    public RequestEntity getItemByPosition(int position) {
        return mRequests.get(position);
    }

    public void clearAll() {
        int size = mRequests.size();
        mRequests.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void changeItemState(RequestEntity request) {
        for (RequestEntity entity : mRequests) {
            if (Objects.equals(request.getId(), entity.getId())) {
                int position = mRequests.indexOf(entity);
                entity.setState(request.getState());
                notifyItemChanged(position);
                return;
            }
        }
    }
}
