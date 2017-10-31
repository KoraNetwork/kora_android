package com.kora.android.di.module;

import android.content.Context;

import com.kora.android.common.preferences.PreferenceHandler;
import com.kora.android.common.preferences.PreferenceHandlerImpl;
import com.kora.android.data.network.service.AuthService;
import com.kora.android.data.network.service.BorrowService;
import com.kora.android.data.network.service.CurrencyConverterService;
import com.kora.android.data.network.service.RequestService;
import com.kora.android.data.network.service.TransactionService;
import com.kora.android.data.network.service.UserService;
import com.kora.android.data.repository.AuthRepository;
import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.data.repository.CurrencyConverterRepository;
import com.kora.android.data.repository.RequestRepository;
import com.kora.android.data.repository.TransactionRepository;
import com.kora.android.data.repository.UserRepository;
import com.kora.android.data.repository.impl.AuthRepositoryImpl;
import com.kora.android.data.repository.impl.BorrowRepositoryImpl;
import com.kora.android.data.repository.impl.CurrencyConverterRepositoryImpl;
import com.kora.android.data.repository.impl.RequestRepositoryImpl;
import com.kora.android.data.repository.impl.TransactionRepositoryImpl;
import com.kora.android.data.repository.impl.UserRepositoryImpl;
import com.kora.android.data.repository.mapper.BorrowMapper;
import com.kora.android.data.repository.mapper.RequestMapper;
import com.kora.android.data.repository.mapper.TransactionMapper;
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

    @Singleton
    @Provides
    public CurrencyConverterRepository provideCurrencyConverterRepository(final CurrencyConverterService currencyConverterService) {
        return new CurrencyConverterRepositoryImpl(currencyConverterService);
    }

    @Singleton
    @Provides
    public TransactionRepository provideTransactionRepository(final TransactionService transactionService,
                                                              final TransactionMapper transactionMapper) {
        return new TransactionRepositoryImpl(transactionService, transactionMapper);
    }

    @Singleton
    @Provides
    public RequestRepository provideRequestRepository(final RequestService requestService,
                                                      final RequestMapper requestMapper) {
        return new RequestRepositoryImpl(requestService, requestMapper);
    }

    @Singleton
    @Provides
    public BorrowRepository provideBorrowRepository(final BorrowService borrowService,
                                                     final BorrowMapper borrowMapper) {
        return new BorrowRepositoryImpl(borrowService, borrowMapper);
    }
}
