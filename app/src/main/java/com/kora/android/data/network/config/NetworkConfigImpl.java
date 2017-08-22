package com.kora.android.data.network.config;

import com.kora.android.data.local.AuthPrefHelper;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkConfigImpl implements NetworkConfig {

    private static final String HEADER_SESSION_TOKEN = "Session-Token";
    private static final int CONNECTION_TIMEOUT = 40; // sec

    private final AuthPrefHelper mAuthPrefHelper;

    @Inject
    public NetworkConfigImpl(final AuthPrefHelper authPrefHelper) {
        mAuthPrefHelper = authPrefHelper;
    }

    @Override
    public int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    private String getAuthTokenHeaderKey() {
        return HEADER_SESSION_TOKEN;
    }

    private String getAuthToken() {
        return mAuthPrefHelper.getSessionToken();
    }

    @Override
    public String getBaseUrl() {
        return BaseApiPathConst.API_BASE_URL + BaseApiPathConst.API_PATH;
    }

    @Override
    public List<Interceptor> getInterceptors() {
        return Arrays.asList(getLoggingInterceptor(), getAuthRequestInterceptor());
    }

    private Interceptor getLoggingInterceptor() {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    private Interceptor getAuthRequestInterceptor() {
        return chain -> {
            Request request = chain.request();

            final String authHeaderKey = getAuthTokenHeaderKey();
            final String authHeaderToken = getAuthToken();

            if (authHeaderKey != null && authHeaderToken != null) {
                request = request.newBuilder()
                        .addHeader(authHeaderKey, authHeaderToken)
                        .build();
            }
            return chain.proceed(request);
        };
    }
}
