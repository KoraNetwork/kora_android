package com.kora.android.presentation.ui.main.fragments.request;

import com.kora.android.presentation.model.RequestEntity;
import com.kora.android.presentation.ui.base.view.BaseFragmentView;

import java.util.List;

public interface RequestView extends BaseFragmentView<RequestPresenter> {

    void showRequests(List<RequestEntity> requestEntities);

    void enableAndShowRefreshIndicator(boolean b, boolean b1);

    void openFilterDialog();
}
