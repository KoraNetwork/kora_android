package com.kora.android.presentation.ui.common.recent;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface RecentView extends BaseView<RecentPresenter> {

    void showUserList(List<UserEntity> users);
}
