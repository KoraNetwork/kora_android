package com.kora.android.data.network.config;

import com.kora.android.common.Keys;
import com.kora.android.common.preferences.PreferenceHandler;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.kora.android.data.network.Constants.API_BASE_URL;
import static com.kora.android.data.network.Constants.API_PATH;
import static com.kora.android.data.network.Constants.CONNECTION_TIMEOUT;
import static com.kora.android.data.network.Constants.HEADER_SESSION_TOKEN;

public class NetworkConfigImpl implements NetworkConfig {

    private final PreferenceHandler mSessionPrefHelper;

    @Inject
    public NetworkConfigImpl(final PreferenceHandler sessionPrefHelper) {
        mSessionPrefHelper = sessionPrefHelper;
    }

    @Override
    public int getConnectionTimeout() {
        return CONNECTION_TIMEOUT;
    }

    private String getAuthTokenHeaderKey() {
        return HEADER_SESSION_TOKEN;
    }

    private String getAuthToken() {
        return mSessionPrefHelper.remindString(Keys.Shared.TOKEN, "");
    }

    @Override
    public String getBaseUrl() {
        return API_BASE_URL + API_PATH;
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
