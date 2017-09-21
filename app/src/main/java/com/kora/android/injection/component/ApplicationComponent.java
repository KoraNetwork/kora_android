package com.kora.android.injection.component;

import android.content.Context;

import com.kora.android.KoraApplication;
import com.kora.android.common.helper.AuthPrefHelper;
import com.kora.android.common.helper.ProxyPrefHelper;
import com.kora.android.data.network.sercvice.RegistrationService;
import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.injection.module.ApplicationModule;
import com.kora.android.injection.module.Web3jModule;
import com.kora.android.injection.module.NetworkModule;
import com.kora.android.injection.module.RepositoryModule;
import com.kora.android.data.web3j.storage.EtherWalletStorage;
import com.kora.android.data.web3j.utils.EtherWalletUtils;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        Web3jModule.class
})
public interface ApplicationComponent {

    void inject(final KoraApplication application);

    Context context();

    AuthPrefHelper authPrefHelper();
    ProxyPrefHelper proxyPrefHelper();

    EtherWalletUtils etherWalletUtils();
    EtherWalletStorage etherWalletStorage();
    Web3jConnection web3jConnection();

    RegistrationService someService();
    RegistrationRepository someRepository();
}
