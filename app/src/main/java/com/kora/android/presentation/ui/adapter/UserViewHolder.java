package com.kora.android.presentation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kora.android.R;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.adapter.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.kora.android.data.network.Constants.API_BASE_URL;

public final class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.root_layout) LinearLayout rootLayout;
    @BindView(R.id.user_image) AppCompatImageView userImage;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_phone) TextView userPhone;

    private UserEntity mUserEntity;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(UserEntity user) {
        mUserEntity = user;

        final Context context = itemView.getContext();
        Glide.with(context)
                .load(API_BASE_URL + user.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(context)
                        .load(R.drawable.ic_user_default))
                .into(userImage);

        userName.setText(user.getFullName());
        userPhone.setText(user.getPhoneNumber());
    }

    public void setOnUserClickListener(OnItemClickListener onUserClickListener) {
        rootLayout.setOnClickListener(v -> onUserClickListener.onItemClicked(getAdapterPosition()));
    }
}
