package com.kora.android.common.helper;

import android.content.Context;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.kora.android.common.Keys.SessionHelperKeys.SESSION_HELPER_FILE_NAME;
import static com.kora.android.common.Keys.SessionHelperKeys.SESSION_HELPER_PASSWORD;
import static com.kora.android.common.Keys.SessionHelperKeys.SESSION_HELPER_SESSION_TOKEN;

@Singleton
public class SessionPrefHelper {

    private final EncryptedPreferences mEncryptedPreferences;

    @Inject
    public SessionPrefHelper(final Context context) {
        mEncryptedPreferences = new EncryptedPreferences.Builder(context)
                .withPreferenceName(SESSION_HELPER_FILE_NAME)
                .withEncryptionPassword(SESSION_HELPER_PASSWORD)
                .build();
    }

    public void storeSessionToken(final String sessionToken) {
        mEncryptedPreferences.edit().putString(SESSION_HELPER_SESSION_TOKEN, sessionToken).apply();
    }

    public String getSessionToken() {
        return mEncryptedPreferences.getString(SESSION_HELPER_SESSION_TOKEN, "");
    }

    public void clear() {
        mEncryptedPreferences.edit().clear().apply();
    }
}
