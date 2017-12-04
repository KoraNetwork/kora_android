package com.kora.android.presentation.ui.main.fragments.deposit_withdraw;

import com.kora.android.presentation.model.DepositWithdrawEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface DepositWithdrawView extends BaseView<DepositWithdrawPresenter> {

    void showDepositWithdrawList(final List<DepositWithdrawEntity> depositWithdrawEntityList);

    void enableAndShowRefreshIndicator(final boolean enableIndicator, final boolean showIndicator);

    void openFilterDialog();
}
