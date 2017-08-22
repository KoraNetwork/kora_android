package com.kora.android.data.network.config;

import java.util.List;

import okhttp3.Interceptor;

public interface NetworkConfig {

    int getConnectionTimeout();

    String getBaseUrl();

    List<Interceptor> getInterceptors();
}