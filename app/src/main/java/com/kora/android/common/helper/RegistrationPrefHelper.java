package com.kora.android.common.helper;

import android.content.Context;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_FILE_NAME;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_PASSWORD;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_PHONE_NUMBER;

@Singleton
public class RegistrationPrefHelper {

    private final EncryptedPreferences mEncryptedPreferences;

    @Inject
    public RegistrationPrefHelper(final Context context) {
        mEncryptedPreferences = new EncryptedPreferences.Builder(context)
                .withPreferenceName(REGISTRATION_HELPER_FILE_NAME)
                .withEncryptionPassword(REGISTRATION_HELPER_PASSWORD)
                .build();
    }

    public void storePhoneNumber(final String phoneNumber) {
        mEncryptedPreferences.edit().putString(REGISTRATION_HELPER_PHONE_NUMBER, phoneNumber).apply();
    }

    public String getPhoneNumber() {
        return mEncryptedPreferences.getString(REGISTRATION_HELPER_PHONE_NUMBER, "");
    }

    public void clear() {
        mEncryptedPreferences.edit().clear().apply();
    }
}
