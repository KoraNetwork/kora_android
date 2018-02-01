package com.kora.android.domain.usecase.web3j;

import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class CreateWalletsUseCase extends AsyncUseCase {

    private final Web3jRepository mWeb3jRepository;

    private String mPinCode;

    @Inject
    public CreateWalletsUseCase(final Web3jRepository web3jRepository) {
        mWeb3jRepository = web3jRepository;
    }

    public void setData(final String pinCode) {
        mPinCode = pinCode;
    }

    @Override
    protected Observable buildObservableTask() {
        return mWeb3jRepository.createWallets(mPinCode);
    }
}
