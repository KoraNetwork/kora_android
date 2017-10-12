package com.kora.android.di.module;

import com.kora.android.data.repository.mapper.RegistrationMapper;
import com.kora.android.data.network.sercvice.RegistrationService;
import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.data.repository.RegistrationRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Singleton
    @Provides
    public RegistrationRepository provideRegistrationRepository(final RegistrationService registrationService,
                                                                final RegistrationMapper registrationMapper) {
        return new RegistrationRepositoryImpl(registrationService, registrationMapper);
    }
}
