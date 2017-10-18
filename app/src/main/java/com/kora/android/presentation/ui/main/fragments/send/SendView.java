package com.kora.android.presentation.ui.main.fragments.send;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface SendView extends BaseView<SendPresenter> {

    void showUserList(List<UserEntity> users);

}
