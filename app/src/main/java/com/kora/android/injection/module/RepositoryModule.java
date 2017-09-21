package com.kora.android.injection.module;

import com.kora.android.common.helper.AuthPrefHelper;
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
                                                                final AuthPrefHelper authPrefHelper) {
        return new RegistrationRepositoryImpl(registrationService, authPrefHelper);
    }
}
