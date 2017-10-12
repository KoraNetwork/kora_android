package com.kora.android.di.module;

import android.content.Context;

import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.data.web3j.utils.EtherWalletUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class Web3jModule {

    @Singleton
    @Provides
    public Web3jConnection provideWeb3jConnection() {
        return new Web3jConnection();
    }

    @Singleton
    @Provides
    public EtherWalletUtils provideEtherWalletUtils() {
        return new EtherWalletUtils();
    }

    @Singleton
    @Provides
    public EtherWalletStorage provideEtherWalletStorage(final Context context) {
        return new EtherWalletStorage(context);
    }
}