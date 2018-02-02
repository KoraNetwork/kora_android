package com.kora.android.domain.usecase.deposit;

import com.kora.android.data.repository.DepositWithdrawRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class DeleteDepositUseCase extends AsyncUseCase {

    private final DepositWithdrawRepository mDepositWithdrawRepository;

    private String mDepositWithdrawId;
    private double mFromAmount;
    private double mToAmount;
    private String mRawTransaction;
    private boolean mIsDeposit;

    @Inject
    public DeleteDepositUseCase(final DepositWithdrawRepository depositWithdrawRepository) {
        mDepositWithdrawRepository = depositWithdrawRepository;
    }

    public void setData(final String depositWithdrawId,
                        final double fromAmount,
                        final double toAmount,
                        final String rawTransaction,
                        final boolean isDeposit) {
        mDepositWithdrawId = depositWithdrawId;
        mFromAmount = fromAmount;
        mToAmount = toAmount;
        mRawTransaction = rawTransaction;
        mIsDeposit = isDeposit;

    }

    @Override
    protected Observable buildObservableTask() {
        return mDepositWithdrawRepository.deleteDeposit(
                mDepositWithdrawId,
                mFromAmount,
                mToAmount,
                mRawTransaction,
                mIsDeposit
        );
    }
}
