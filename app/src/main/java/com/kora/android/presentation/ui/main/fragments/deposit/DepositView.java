package com.kora.android.presentation.ui.main.fragments.deposit;

import com.kora.android.presentation.model.DepositEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface DepositView extends BaseView<DepositPresenter> {

    void showDepositList(final List<DepositEntity> depositEntityList);

    void enableAndShowRefreshIndicator(final boolean enableIndicator, final boolean showIndicator);

    void openFilterDialog();
}
