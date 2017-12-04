package com.kora.android.domain.usecase.deposit;

import com.kora.android.common.Keys;
import com.kora.android.data.repository.DepositWithdrawRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.ui.main.fragments.deposit_withdraw.filter.DepositWithdrawFilterModel;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetDepositListUseCase extends AsyncUseCase {

    private final DepositWithdrawRepository mDepositWithdrawRepository;

    private DepositWithdrawFilterModel mDepositWithdrawFilterModel;
    private int mSkip;
    private boolean mIsDeposit;

    @Inject
    public GetDepositListUseCase(final DepositWithdrawRepository depositWithdrawRepository) {
        mDepositWithdrawRepository = depositWithdrawRepository;
    }

    public void setData(final DepositWithdrawFilterModel depositWithdrawFilterModel,
                        final int skip,
                        final boolean isDeposit) {
        mDepositWithdrawFilterModel = depositWithdrawFilterModel;
        mSkip = skip;
        mIsDeposit = isDeposit;
    }

    @Override
    protected Observable buildObservableTask() {
        return mDepositWithdrawRepository.getDepositList(
                mDepositWithdrawFilterModel,
                mSkip,
                Keys.ITEMS_PER_PAGE,
                mIsDeposit
        );
    }
}
