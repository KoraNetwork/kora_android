package com.kora.android.domain.usecase.web3j;

import android.net.Uri;

import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class ImportWalletUseCase extends AsyncUseCase {

    private final Web3jRepository mWeb3jRepository;

    private Uri mWalletFileUri;

    @Inject
    public ImportWalletUseCase(final Web3jRepository web3jRepository) {
        mWeb3jRepository = web3jRepository;
    }

    public void setData(final Uri walletFileUri) {
        mWalletFileUri = walletFileUri;
    }

    @Override
    protected Observable buildObservableTask() {
        return mWeb3jRepository.importWalletFile(mWalletFileUri);
    }
}
