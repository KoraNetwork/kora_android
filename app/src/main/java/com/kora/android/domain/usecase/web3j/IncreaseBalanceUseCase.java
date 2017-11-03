package com.kora.android.domain.usecase.web3j;

import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class IncreaseBalanceUseCase extends AsyncUseCase {

    private final Web3jRepository mWeb3jRepository;

    private UserEntity mUserEntity;
    private double mAmount;

    @Inject
    public IncreaseBalanceUseCase(final Web3jRepository web3jRepository) {
        mWeb3jRepository = web3jRepository;
    }

    public void setData(final UserEntity userEntity,
                        final double amount) {
        mUserEntity = userEntity;
        mAmount = amount;
    }

    @Override
    protected Observable buildObservableTask() {
        return mWeb3jRepository.increaseBalance(mUserEntity, mAmount);
    }
}
