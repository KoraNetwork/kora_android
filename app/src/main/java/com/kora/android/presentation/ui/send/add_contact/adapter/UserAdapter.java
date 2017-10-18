package com.kora.android.presentation.ui.send.add_contact.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kora.android.R;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.presentation.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kora.android.data.network.Constants.API_BASE_URL;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context mContext;
    private final List<UserEntity> mUserEntityList;

    private OnItemClickListener mOnItemClickListener;

    @Inject
    public UserAdapter(final Context context) {
        mContext = context;
        mUserEntityList = new ArrayList<>();
    }

    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.UserViewHolder holder, final int position) {
        final UserEntity userEntity = mUserEntityList.get(position);
        holder.setData(userEntity, position);
    }

    @Override
    public int getItemCount() {
        return mUserEntityList.size();
    }

    public void addAll(final List<UserEntity> userEntityList) {
        mUserEntityList.addAll(userEntityList);
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_container)
        CardView mCvContainer;
        @BindView(R.id.image_view_avatar)
        ImageView mIvAvatar;
        @BindView(R.id.text_view_user_name)
        TextView mTvUserName;
        @BindView(R.id.text_view_phone_number)
        TextView mTvPhoneNumber;

        public UserViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final UserEntity userEntity, final int position) {
            final Context context = mCvContainer.getContext();
            mCvContainer.setOnClickListener(v ->
                    mOnItemClickListener.onClickSelectUser(userEntity, position));
            mTvUserName.setText(userEntity.getUserName());
            final String phoneNumber = StringUtils.addPlusIfNeeded(userEntity.getPhoneNumber());
            mTvPhoneNumber.setText(phoneNumber);
            if (userEntity.getAvatar() == null || userEntity.getAvatar().isEmpty())
                return;
            Glide.with(context)
                    .load(API_BASE_URL + userEntity.getAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .into(mIvAvatar);
        }
    }

    public interface OnItemClickListener {
        void onClickSelectUser(final UserEntity userEntity, final int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}