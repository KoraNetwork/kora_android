package com.kora.android.domain.usecase.deposit;

import com.kora.android.data.repository.DepositWithdrawRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class UpdateDepositUseCase extends AsyncUseCase {

    private final DepositWithdrawRepository mDepositWithdrawRepository;

    private String mDepositWithdrawId;
    private boolean mIsDeposit;

    @Inject
    public UpdateDepositUseCase(final DepositWithdrawRepository depositWithdrawRepository) {
        mDepositWithdrawRepository = depositWithdrawRepository;
    }

    public void setData(final String depositWithdrawId,
                        final boolean isDeposit) {
        mDepositWithdrawId = depositWithdrawId;
        mIsDeposit = isDeposit;
    }

    @Override
    protected Observable buildObservableTask() {
        return mDepositWithdrawRepository.updateDeposit(mDepositWithdrawId, mIsDeposit);
    }
}
