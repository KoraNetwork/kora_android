package com.kora.android.presentation.ui.main.fragments.borrow.fragment;

import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface BorrowView extends BaseView<BorrowPresenter> {

    void enableAndShowRefreshIndicator(boolean b, boolean b1);

    void showData(List<BorrowEntity> borrowEntities);

}
