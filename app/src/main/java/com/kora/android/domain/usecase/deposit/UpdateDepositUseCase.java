package com.kora.android.domain.usecase.deposit;

import com.kora.android.data.repository.DepositRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class UpdateDepositUseCase extends AsyncUseCase {

    private final DepositRepository mDepositRepository;

    private String mDepositId;

    @Inject
    public UpdateDepositUseCase(final DepositRepository depositRepository) {
        mDepositRepository = depositRepository;
    }

    public void setData(final String depositId) {
        mDepositId = depositId;
    }

    @Override
    protected Observable buildObservableTask() {
        return mDepositRepository.updateDeposit(mDepositId);
    }
}
