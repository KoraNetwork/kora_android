package com.kora.android.domain.usecase.deposit;

import com.kora.android.data.repository.DepositRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class AddDepositUseCase extends AsyncUseCase {

    private final DepositRepository mDepositRepository;

    private String mTo;
    private double mFromAmount;
    private double mToAmount;
    private int mInterestRate;

    @Inject
    public AddDepositUseCase(final DepositRepository depositRepository) {
        mDepositRepository = depositRepository;
    }

    public void setData(final String to,
                        final double fromAmount,
                        final double toAmount,
                        final int interestRate) {
        mTo = to;
        mFromAmount = fromAmount;
        mToAmount = toAmount;
        mInterestRate = interestRate;
    }

    @Override
    protected Observable buildObservableTask() {
        return mDepositRepository.addDeposit(
                mTo,
                mFromAmount,
                mToAmount,
                mInterestRate
        );
    }
}
