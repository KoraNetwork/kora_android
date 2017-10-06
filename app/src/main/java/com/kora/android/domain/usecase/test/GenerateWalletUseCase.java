package com.kora.android.domain.usecase.test;

import android.content.Context;

import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.data.web3j.model.EtherWallet;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.data.web3j.utils.EtherWalletUtils;

import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

public class GenerateWalletUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

    private final Context mContext;
    private final EtherWalletUtils mEtherWalletUtils;
    private final EtherWalletStorage mEtherWalletStorage;

    private String mPassword;
    private String mPrivateKey;

    @Inject
    public GenerateWalletUseCase(final Context context,
                                 final EtherWalletUtils etherWalletUtils,
                                 final EtherWalletStorage etherWalletStorage) {
        this.mContext = context;
        this.mEtherWalletUtils = etherWalletUtils;
        this.mEtherWalletStorage = etherWalletStorage;
    }

    public void setData(final String password,
                        final String privateKey) {
        mPassword = password;
        mPrivateKey = privateKey;
    }

    @Override
    protected Single buildTask() {
        return Single.just(true).map(a -> {
            String walletFileName;
            if (mPrivateKey == null || mPrivateKey.isEmpty()) {
                // create new wallet
                walletFileName = mEtherWalletUtils.generateNewWalletFile(mPassword, new File(mContext.getFilesDir(), ""), true);
            } else {
                // import wallet
                final ECKeyPair keys = ECKeyPair.create(Hex.decode(mPrivateKey));
                walletFileName = mEtherWalletUtils.generateWalletFile(mPassword, keys, new File(mContext.getFilesDir(), ""), true);
            }
            final EtherWallet etherWallet = new EtherWallet(walletFileName);
            mEtherWalletStorage.addWallet(etherWallet);
            return etherWallet;
        });
    }
}
