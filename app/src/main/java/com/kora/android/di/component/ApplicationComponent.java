package com.kora.android.di.component;

import android.content.Context;

import com.kora.android.KoraApplication;
import com.kora.android.common.helper.RegistrationPrefHelper;
import com.kora.android.common.helper.SessionPrefHelper;
import com.kora.android.common.helper.ProxyPrefHelper;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.data.network.sercvice.LoginService;
import com.kora.android.data.network.sercvice.RegistrationService;
import com.kora.android.data.repository.LoginRepository;
import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.di.module.ApplicationModule;
import com.kora.android.di.module.Web3jModule;
import com.kora.android.di.module.NetworkModule;
import com.kora.android.di.module.RepositoryModule;
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

    PreferenceHandler preferanceHandler();

    RegistrationPrefHelper registrationPrefHelper();
    SessionPrefHelper authPrefHelper();
    ProxyPrefHelper proxyPrefHelper();

    EtherWalletUtils etherWalletUtils();
    EtherWalletStorage etherWalletStorage();
    Web3jConnection web3jConnection();

    RegistrationService registrationService();
    LoginService loginService();

    RegistrationRepository registrationRepository();
    LoginRepository loginRepository();
}
