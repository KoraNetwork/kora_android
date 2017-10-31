package com.kora.android.di.module;

import android.content.Context;

import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.common.preferences.PreferenceHandlerImpl;
import com.kora.android.data.network.service.AuthService;
import com.kora.android.data.network.service.CurrencyConverterService;
import com.kora.android.data.network.service.RequestService;
import com.kora.android.data.network.service.TransactionService;
import com.kora.android.data.network.service.UserService;
import com.kora.android.data.repository.AuthRepository;
import com.kora.android.data.repository.CurrencyConverterRepository;
import com.kora.android.data.repository.RequestRepository;
import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.data.repository.UserRepository;
import com.kora.android.data.repository.impl.AuthRepositoryImpl;
import com.kora.android.data.repository.impl.CurrencyConverterRepositoryImpl;
import com.kora.android.data.repository.impl.RequestRepositoryImpl;
import com.kora.android.data.repository.impl.TransactionRepositoryImpl;
import com.kora.android.data.repository.impl.UserRepositoryImpl;
import com.kora.android.data.repository.mapper.RequestMapper;
import com.kora.android.data.repository.mapper.TransactionMapper;
import com.kora.android.data.repository.mapper.UserMapper;
import com.kora.android.data.web3j.connection.Web3jConnection;
import com.kora.android.data.web3j.storage.EtherWalletStorage;

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

    @Singleton
    @Provides
    public CurrencyConverterRepository provideCurrencyConverterRepository(final CurrencyConverterService currencyConverterService) {
        return new CurrencyConverterRepositoryImpl(currencyConverterService);
    }

    @Singleton
    @Provides
    public TransactionRepository provideTransactionRepository(final Context context,
                                                              final PreferenceHandler preferenceHandler,
                                                              final Web3jConnection web3jConnection,
                                                              final EtherWalletStorage etherWalletStorage,
                                                              final TransactionService transactionService,
                                                              final TransactionMapper transactionMapper) {
        return new TransactionRepositoryImpl(
                context,
                preferenceHandler,
                web3jConnection,
                etherWalletStorage,
                transactionService,
                transactionMapper
        );
    }

    @Singleton
    @Provides
    public RequestRepository provideRequestRepository(final RequestService requestService,
                                                      final RequestMapper requestMapper) {
        return new RequestRepositoryImpl(requestService, requestMapper);
    }
}
