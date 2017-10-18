package com.kora.android.di.module;

import android.content.Context;

import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.common.preferences.PreferenceHandlerImpl;
import com.kora.android.data.network.service.AuthService;
import com.kora.android.data.network.service.UserService;
import com.kora.android.data.repository.AuthRepository;
import com.kora.android.data.repository.UserRepository;
import com.kora.android.data.repository.impl.AuthRepositoryImpl;
import com.kora.android.data.repository.impl.UserRepositoryImpl;
import com.kora.android.data.repository.mapper.UserMapper;

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
    public AuthRepository provideAuthRepository(final PreferenceHandler preferenceHandler,
                                                        final AuthService authService,
                                                        final UserMapper userMapper) {
        return new AuthRepositoryImpl(authService, preferenceHandler, userMapper);
    }

    @Singleton
    @Provides
    public UserRepository provideUserRepository(final UserService userService,
                                                final UserMapper userMapper,
                                                final PreferenceHandler preferenceHandler) {
        return new UserRepositoryImpl(userService, userMapper, preferenceHandler);
    }
}
