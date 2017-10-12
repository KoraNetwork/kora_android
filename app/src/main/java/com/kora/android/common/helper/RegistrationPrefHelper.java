package com.kora.android.common.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.kora.android.presentation.model.CountryEntity;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_COUNTRY;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_CREATOR_ADDRESS;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_FILE_NAME;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_FILE_PASSWORD;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_PHONE_NUMBER;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_OWNER_ADDRESS;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_RECOVERY_ADDRESS;
import static com.kora.android.common.Keys.RegistrationHelperKeys.REGISTRATION_HELPER_IDENTITY_ADDRESS;

@Singleton
public class RegistrationPrefHelper {

    private final EncryptedPreferences mEncryptedPreferences;

    @Inject
    public RegistrationPrefHelper(final Context context) {
        mEncryptedPreferences = new EncryptedPreferences.Builder(context)
                .withPreferenceName(REGISTRATION_HELPER_FILE_NAME)
                .withEncryptionPassword(REGISTRATION_HELPER_FILE_PASSWORD)
                .build();
    }

    public void storeCountry(final CountryEntity countryEntity) {
        final String countryString = new Gson().toJson(countryEntity);
        mEncryptedPreferences.edit().putString(REGISTRATION_HELPER_COUNTRY, countryString).apply();
    }

    public CountryEntity getCountry() {
        final String countryString = mEncryptedPreferences.getString(REGISTRATION_HELPER_COUNTRY, "");
        return new Gson().fromJson(countryString, CountryEntity.class);
    }

    public void storePhoneNumber(final String phoneNumber) {
        mEncryptedPreferences.edit().putString(REGISTRATION_HELPER_PHONE_NUMBER, phoneNumber).apply();
    }

    public String getPhoneNumber() {
        return mEncryptedPreferences.getString(REGISTRATION_HELPER_PHONE_NUMBER, "");
    }

    public void storeIdentityAddress(final String identityAddress) {
        mEncryptedPreferences.edit().putString(REGISTRATION_HELPER_IDENTITY_ADDRESS, identityAddress).apply();
    }

    public String getIdentityAddress() {
        return mEncryptedPreferences.getString(REGISTRATION_HELPER_IDENTITY_ADDRESS, "");
    }

    public void storeCreatorAddress(final String creatorAddress) {
        mEncryptedPreferences.edit().putString(REGISTRATION_HELPER_CREATOR_ADDRESS, creatorAddress).apply();
    }

    public String getCreatorAddress() {
        return mEncryptedPreferences.getString(REGISTRATION_HELPER_CREATOR_ADDRESS, "");
    }

    public void storeRecoveryAddress(final String recoveryAddress) {
        mEncryptedPreferences.edit().putString(REGISTRATION_HELPER_RECOVERY_ADDRESS, recoveryAddress).apply();
    }

    public String getRecoveryAddress() {
        return mEncryptedPreferences.getString(REGISTRATION_HELPER_RECOVERY_ADDRESS, "");
    }

    public void storeOwnerAddress(final String ownerAddress) {
        mEncryptedPreferences.edit().putString(REGISTRATION_HELPER_OWNER_ADDRESS, ownerAddress).apply();
    }

    public String getOwnerAddress() {
        return mEncryptedPreferences.getString(REGISTRATION_HELPER_OWNER_ADDRESS, "");
    }

    public void clear() {
        mEncryptedPreferences.edit().clear().apply();
    }
}
