package com.kora.android.domain.usecase.deposit;

import com.kora.android.data.repository.DepositRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class DeleteDepositUseCase extends AsyncUseCase {

    private final DepositRepository mDepositRepository;

    private String mDepositId;
    private double mFromAmount;
    private double mToAmount;
    private List<String> mRawTransactions;

    @Inject
    public DeleteDepositUseCase(final DepositRepository depositRepository) {
        mDepositRepository = depositRepository;
    }

    public void setData(final String depositId,
                        final double fromAmount,
                        final double toAmount,
                        final List<String> rawTransactions) {
        mDepositId = depositId;
        mFromAmount = fromAmount;
        mToAmount = toAmount;
        mRawTransactions = rawTransactions;
    }

    @Override
    protected Observable buildObservableTask() {
        return mDepositRepository.deleteDeposit(
                mDepositId,
                mFromAmount,
                mToAmount,
                mRawTransactions
        );
    }
}
