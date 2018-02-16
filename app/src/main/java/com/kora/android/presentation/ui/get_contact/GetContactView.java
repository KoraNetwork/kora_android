package com.kora.android.presentation.ui.get_contact;

import android.support.v4.util.Pair;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface GetContactView extends BaseView<GetContactPresenter> {

    void showUsers(final Pair<List<UserEntity>, List<UserEntity>> pair);
}
