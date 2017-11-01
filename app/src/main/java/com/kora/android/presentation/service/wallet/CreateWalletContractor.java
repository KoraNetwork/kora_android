package com.kora.android.presentation.service.wallet;

import com.kora.android.presentation.service.BaseServiceContractor;
import com.kora.android.presentation.ui.base.custom.RetryAction;

public interface CreateWalletContractor extends BaseServiceContractor<CreateWalletPresenter> {
    void showError(String message, RetryAction createIdentityAction);

    void showCreateIdentityMessage();

    void showCreatedIdentityMessage();

    void showUpdateUserMessage();

    void showUpdatedUserMessage();

    void showIncreaseBalanceMessage();

    void finishService();

    void showIncreasedBalanceMessage();
}
