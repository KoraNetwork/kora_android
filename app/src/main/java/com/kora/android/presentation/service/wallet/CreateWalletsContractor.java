package com.kora.android.presentation.service.wallet;

import com.kora.android.presentation.service.BaseServiceContractor;

public interface CreateWalletsContractor extends BaseServiceContractor<CreateWalletsPresenter> {

    void showError(final String text);

    void showErrorWithRetry(final String retryActionArg);

    void showCreateKeystoreFileMessage();

    void showCreateIdentityMessage();

    void showIncreaseBalanceMessage();

    void finishService(final boolean success);
}
