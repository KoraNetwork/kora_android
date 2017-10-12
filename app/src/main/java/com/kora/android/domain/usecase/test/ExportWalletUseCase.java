package com.kora.android.domain.usecase.test;

import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

public class ExportWalletUseCase extends AsyncUseCase {

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
    protected Observable buildObservableTask() {
        return Observable.just(true).map(a -> {
            mEtherWalletStorage.exportWallet(mWalletFileName);
            return a;
        });
    }
}
