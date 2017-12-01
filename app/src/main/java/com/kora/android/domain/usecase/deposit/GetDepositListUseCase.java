package com.kora.android.domain.usecase.deposit;

import com.kora.android.common.Keys;
import com.kora.android.data.repository.DepositRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.ui.main.fragments.deposit.filter.DepositFilterModel;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetDepositListUseCase extends AsyncUseCase {

    private final DepositRepository mDepositRepository;

    private DepositFilterModel mDepositFilterModel;
    private int mSkip = 0;

    @Inject
    public GetDepositListUseCase(final DepositRepository depositRepository) {
        mDepositRepository = depositRepository;
    }

    public void setData(final DepositFilterModel depositFilterModel, final int skip) {
        mDepositFilterModel = depositFilterModel;
        mSkip = skip;
    }

    @Override
    protected Observable buildObservableTask() {
        return mDepositRepository.getDepositList(mDepositFilterModel, mSkip, Keys.ITEMS_PER_PAGE);
    }
}
