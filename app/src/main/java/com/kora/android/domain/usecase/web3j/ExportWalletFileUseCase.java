package com.kora.android.domain.usecase.web3j;

import com.kora.android.data.repository.Web3jRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class ExportWalletFileUseCase extends AsyncUseCase {

    private final Web3jRepository mWeb3jRepository;

    private File mWalletFile;

    @Inject
    public ExportWalletFileUseCase(final Web3jRepository web3jRepository) {
        mWeb3jRepository = web3jRepository;
    }

    public void setData(final File walletFile) {
        mWalletFile = walletFile;
    }

    @Override
    protected Observable buildObservableTask() {
        return mWeb3jRepository.exportWalletFile(mWalletFile);
    }
}
