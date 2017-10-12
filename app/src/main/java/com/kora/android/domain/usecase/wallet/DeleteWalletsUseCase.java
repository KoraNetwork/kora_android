package com.kora.android.domain.usecase.wallet;

import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

public class DeleteWalletsUseCase extends AsyncUseCase {

    private final EtherWalletStorage mEtherWalletStorage;

    @Inject
    public DeleteWalletsUseCase(final EtherWalletStorage etherWalletStorage) {
        mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.just(true).map(a -> {
            mEtherWalletStorage.deleteWallets();
            return true;
        });
    }
}

