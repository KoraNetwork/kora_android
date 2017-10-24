package com.kora.android.presentation.ui.common.add_contact;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface AddContactView extends BaseView<AddContactPresenter> {

    void showUsers(final List<UserEntity> userEntityList, final boolean clearList);
}
