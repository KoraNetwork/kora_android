package com.kora.android.domain.usecase.deposit;

import com.kora.android.data.repository.DepositWithdrawRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class AddDepositWithdrawUseCase extends AsyncUseCase {

    private final DepositWithdrawRepository mDepositWithdrawRepository;

    private String mTo;
    private double mFromAmount;
    private double mToAmount;
    private int mInterestRate;
    private boolean mIsDeposit;

    @Inject
    public AddDepositWithdrawUseCase(final DepositWithdrawRepository depositWithdrawRepository) {
        mDepositWithdrawRepository = depositWithdrawRepository;
    }

    public void setData(final String to,
                        final double fromAmount,
                        final double toAmount,
                        final int interestRate,
                        final boolean isDeposit) {
        mTo = to;
        mFromAmount = fromAmount;
        mToAmount = toAmount;
        mInterestRate = interestRate;
        mIsDeposit = isDeposit;
    }

    @Override
    protected Observable buildObservableTask() {
        return mDepositWithdrawRepository.addDeposit(
                mTo,
                mFromAmount,
                mToAmount,
                mInterestRate,
                mIsDeposit
        );
    }
}
