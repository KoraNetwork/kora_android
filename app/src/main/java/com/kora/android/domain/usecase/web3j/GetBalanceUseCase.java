package com.kora.android.domain.usecase.web3j;

import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetBalanceUseCase extends AsyncUseCase {

    private final Web3jRepository mWeb3jRepository;

    private String mProxyAddress;
    private String mSmartContractAddress;

    @Inject
    public GetBalanceUseCase(final Web3jRepository web3jRepository) {
        mWeb3jRepository = web3jRepository;
    }

    public void setData(final String proxyAddress,
                        final String smartContractAddress) {
        mProxyAddress = proxyAddress;
        mSmartContractAddress = smartContractAddress;
    }

    @Override
    protected Observable buildObservableTask() {
        return mWeb3jRepository.getBalance(mProxyAddress, mSmartContractAddress);
    }
}
