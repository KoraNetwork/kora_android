package com.kora.android.di.module;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.kora.android.data.network.config.NetworkConfigImpl;
import com.kora.android.data.network.factory.RxErrorHandlingCallAdapterFactory;
import com.kora.android.data.network.sercvice.LoginService;
import com.kora.android.data.network.sercvice.RegistrationService;

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
    RegistrationService provideRegistrationService(final Retrofit retrofit) {
        return retrofit.create(RegistrationService.class);
    }

    @Singleton
    @Provides
    LoginService loginService(final Retrofit retrofit) {
        return retrofit.create(LoginService.class);
    }
}
