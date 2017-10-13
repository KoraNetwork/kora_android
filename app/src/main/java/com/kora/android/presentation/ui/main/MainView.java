package com.kora.android.presentation.ui.main;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

public interface MainView extends BaseView<MainPresenter> {

    void showFragmentByPosition(int position);

    void showUserData(UserEntity userEntity);
}