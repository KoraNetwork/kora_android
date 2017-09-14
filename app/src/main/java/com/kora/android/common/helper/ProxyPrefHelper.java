package com.kora.android.common.helper;

import android.content.Context;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.kora.android.common.Keys.ProxyHelperKeys.PROXY_HELPER_ADDRESS;
import static com.kora.android.common.Keys.ProxyHelperKeys.PROXY_HELPER_FILE_NAME;
import static com.kora.android.common.Keys.ProxyHelperKeys.PROXY_HELPER_PASSWORD;

@Singleton
public class ProxyPrefHelper {

    private final EncryptedPreferences mEncryptedPreferences;

    @Inject
    public ProxyPrefHelper(final Context context) {
        mEncryptedPreferences = new EncryptedPreferences.Builder(context)
                .withPreferenceName(PROXY_HELPER_FILE_NAME)
                .withEncryptionPassword(PROXY_HELPER_PASSWORD)
                .build();
    }

    public void storeProxyAddress(final String proxyAddress) {
        mEncryptedPreferences.edit().putString(PROXY_HELPER_ADDRESS, proxyAddress).apply();
    }

    public String getProxyAddress() {
        return mEncryptedPreferences.getString(PROXY_HELPER_ADDRESS, "");
    }

    public void clear() {
        mEncryptedPreferences.edit().clear().apply();
    }
}
