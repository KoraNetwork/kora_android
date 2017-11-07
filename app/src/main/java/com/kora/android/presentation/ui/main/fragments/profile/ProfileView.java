package com.kora.android.presentation.ui.main.fragments.profile;

import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.io.File;

public interface ProfileView extends BaseView<ProfilePresenter> {
    void retrieveUserData(UserEntity userEntity);

    void showEmptyUserName();

    void showIncorrectUserName();

    void showEmptyEmail();

    void showIncorrectEmail();

    void showIncorrectDate();

    void onUserUpdated();

    void showExportWalletDialog();

    void showExportedWalletMessage();

    void showImportedWalletMessage();
}
