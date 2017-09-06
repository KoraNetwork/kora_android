package com.kora.android.injection.module;

import android.content.Context;

import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.data.web3j.utils.EtherWalletUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class EtherWalletModule {

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
