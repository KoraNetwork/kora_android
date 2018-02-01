package com.kora.android.presentation.service.wallet;

import android.support.annotation.StringRes;

import com.kora.android.presentation.service.BaseServiceContractor;

public interface CreateWalletsContractor extends BaseServiceContractor<CreateWalletsPresenter> {

    void showError(final String text);

    void showErrorWithRetry(final String text, final String retryAction);

    void showErrorWithRetry(@StringRes final int stringId, final String retryAction);

    void showCreateKeystoreFileMessage();

    void showCreateIdentityMessage();

    void showIncreaseBalanceMessage();

    void finishService(final boolean success);
}
