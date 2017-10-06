package com.kora.android.domain.usecase.wallet;

import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableCompletableObserver;

public class DeleteWalletsUseCase extends AsyncUseCase<DisposableCompletableObserver, Completable> {

    private final EtherWalletStorage mEtherWalletStorage;

    @Inject
    public DeleteWalletsUseCase(final EtherWalletStorage etherWalletStorage) {
        mEtherWalletStorage = etherWalletStorage;
    }

    @Override
    protected Completable buildTask() {
        return Single.just(true).map(a -> {
            mEtherWalletStorage.deleteWallets();
            return true;
        }).toCompletable();
    }
}

