package com.kora.android.domain.usecase.test;

import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableCompletableObserver;

public class ExportWalletUseCase extends AsyncUseCase<DisposableCompletableObserver, Completable> {

    private final EtherWalletStorage mEtherWalletStorage;

    private String mWalletFileName;

    @Inject
    public ExportWalletUseCase(EtherWalletStorage etherWalletStorage) {
        this.mEtherWalletStorage = etherWalletStorage;
    }

    public void setData(final String walletFileName) {
        mWalletFileName = walletFileName;
    }

    @Override
    protected Completable buildTask() {
        return Single.just(true).map(a -> {
            mEtherWalletStorage.exportWallet(mWalletFileName);
            return a;
        }).toCompletable();
    }
}
