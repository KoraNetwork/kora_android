package com.kora.android.domain.usecase.test;

import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public class GetWalletListUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

    private final EtherWalletStorage mEtherWalletStorage;

    @Inject
    public GetWalletListUseCase(EtherWalletStorage etherWalletStorage) {
        this.mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    protected Single buildTask() {
        return Single.just(mEtherWalletStorage.getWalletList());
    }
}