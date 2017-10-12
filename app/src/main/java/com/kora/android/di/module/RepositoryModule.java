package com.kora.android.di.module;

import android.content.Context;

import com.kora.android.common.helper.SessionPrefHelper;
import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.common.preferences.PreferenceHandlerImpl;
import com.kora.android.data.network.sercvice.LoginService;
import com.kora.android.data.network.sercvice.RegistrationService;
import com.kora.android.data.repository.LoginRepository;
import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.data.repository.impl.LoginRepositoryImpl;
import com.kora.android.data.repository.impl.RegistrationRepositoryImpl;
import com.kora.android.data.repository.mapper.LoginMapper;
import com.kora.android.data.repository.mapper.RegistrationMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Singleton
    @Provides
    public PreferenceHandler providePreferenceHandler(final Context context) {
        return new PreferenceHandlerImpl(context);
    }

    @Singleton
    @Provides
    public RegistrationRepository provideRegistrationRepository(final PreferenceHandler preferenceHandler,
                                                                final RegistrationService registrationService,
                                                                final RegistrationMapper registrationMapper) {
        return new RegistrationRepositoryImpl(preferenceHandler, registrationService, registrationMapper);
    }

    @Singleton
    @Provides
    public LoginRepository loginRepository(final SessionPrefHelper sessionPrefHelper,
                                           final LoginService loginService,
                                           final LoginMapper loginMapper) {
        return new LoginRepositoryImpl(sessionPrefHelper, loginService, loginMapper);
    }
}
