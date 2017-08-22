package com.kora.android.data.local;

import android.content.Context;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.kora.android.common.Keys.AuthHelperKeys.AUTH_HELPER_FILE_NAME;
import static com.kora.android.common.Keys.AuthHelperKeys.AUTH_HELPER_PASSWORD;
import static com.kora.android.common.Keys.AuthHelperKeys.AUTH_HELPER_SESSION_TOKEN;

@Singleton
public class AuthPrefHelper {

    private final EncryptedPreferences mEncryptedPreferences;

    @Inject
    public AuthPrefHelper(final Context context) {
        mEncryptedPreferences = new EncryptedPreferences.Builder(context)
                .withPreferenceName(AUTH_HELPER_FILE_NAME)
                .withEncryptionPassword(AUTH_HELPER_PASSWORD)
                .build();
    }

    public void storeSessionToken(final String sessionToken) {
        mEncryptedPreferences.edit().putString(AUTH_HELPER_SESSION_TOKEN, sessionToken).apply();
    }

    public String getSessionToken() {
        return mEncryptedPreferences.getString(AUTH_HELPER_SESSION_TOKEN, "");
    }

    public void clear() {
        mEncryptedPreferences.edit().clear().apply();
    }
}
