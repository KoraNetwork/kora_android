package com.kora.android.di.module;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.kora.android.data.network.config.NetworkConfigImpl;
import com.kora.android.data.network.factory.RxErrorHandlingCallAdapterFactory;
import com.kora.android.data.network.service.AuthService;
import com.kora.android.data.network.service.BorrowService;
import com.kora.android.data.network.service.CurrencyConvertService;
import com.kora.android.data.network.service.DepositWithdrawService;
import com.kora.android.data.network.service.RequestService;
import com.kora.android.data.network.service.TransactionService;
import com.kora.android.data.network.service.UserService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(final NetworkConfigImpl networkConfigurationImpl) {
        final OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(networkConfigurationImpl.getConnectionTimeout(), TimeUnit.SECONDS)
                .writeTimeout(networkConfigurationImpl.getConnectionTimeout(), TimeUnit.SECONDS);
        for (Interceptor interceptor : networkConfigurationImpl.getInterceptors()) {
            okHttpBuilder.addInterceptor(interceptor);
        }
        return okHttpBuilder.build();
    }

    @Singleton
    @Provides
    Retrofit provideBaseRetrofit(final OkHttpClient httpClient, final NetworkConfigImpl networkConfigurationImpl) {
        return new Retrofit.Builder()
                .baseUrl(networkConfigurationImpl.getBaseUrl())
                .client(httpClient)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    AuthService provideAuthService(final Retrofit retrofit) {
        return retrofit.create(AuthService.class);
    }

    @Singleton
    @Provides
    UserService provideUserService(final Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Singleton
    @Provides
    CurrencyConvertService provideCurrencyConverterService(final Retrofit retrofit) {
        return retrofit.create(CurrencyConvertService.class);
    }

    @Singleton
    @Provides
    TransactionService provideTransactionService(final Retrofit retrofit) {
        return retrofit.create(TransactionService.class);
    }

    @Singleton
    @Provides
    RequestService provideRequestService(final Retrofit retrofit) {
        return retrofit.create(RequestService.class);
    }

    @Singleton
    @Provides
    BorrowService provideBorrowService(final Retrofit retrofit) {
        return retrofit.create(BorrowService.class);
    }

    @Singleton
    @Provides
    DepositWithdrawService provideDepositService(final Retrofit retrofit) {
        return retrofit.create(DepositWithdrawService.class);
    }
}
