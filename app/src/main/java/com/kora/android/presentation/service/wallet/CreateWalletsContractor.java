package com.kora.android.presentation.service.wallet;

import android.support.annotation.StringRes;

import com.kora.android.presentation.service.BaseServiceContractor;
import com.kora.android.presentation.ui.base.custom.RetryAction;

public interface CreateWalletsContractor extends BaseServiceContractor<CreateWalletsPresenter> {

    void showError(final String message, final RetryAction retryAction);

    void showError(@StringRes final  int stringId, final RetryAction retryAction);

    void showCreateIdentityMessage();

    void showUpdateUserMessage();

    void showIncreaseBalanceMessage();

    void finishService();
}
