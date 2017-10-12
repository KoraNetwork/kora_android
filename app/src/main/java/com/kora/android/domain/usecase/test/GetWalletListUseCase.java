package com.kora.android.domain.usecase.test;

import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetWalletListUseCase extends AsyncUseCase {

    private final EtherWalletStorage mEtherWalletStorage;

    @Inject
    public GetWalletListUseCase(EtherWalletStorage etherWalletStorage) {
        this.mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.just(mEtherWalletStorage.getWalletList());
    }
}