package com.kora.android.common.preferences;

import android.support.annotation.NonNull;

import java.util.Set;

public interface PreferenceHandler {

    void forgetAll();

    void forgetAll(final Callback callback);

    void forget(String key);

    void forget(final String key, final Callback callback);

    PreferenceHandler rememberFloat(final String key, final float value);

    PreferenceHandler rememberInt(String key, int value);

    PreferenceHandler rememberLong(String key, long value);

    PreferenceHandler rememberString(String key, String value);

    PreferenceHandler rememberBoolean(String key, boolean value);

    PreferenceHandler rememberStringSet(String key, Set<String> value);

    PreferenceHandler rememberObject(String key, Object value);

    PreferenceHandler rememberFloat(final String key, final float value, final Callback callback);

    PreferenceHandler rememberInt(String key, int value, Callback callback);

    PreferenceHandler rememberLong(String key, long value, Callback callback);

    PreferenceHandler rememberString(String key, String value, Callback callback);

    PreferenceHandler rememberBoolean(String key, boolean value, Callback callback);

    PreferenceHandler rememberStringSet(String key, Set<String> value, Callback callback);

    PreferenceHandler rememberObject(String key, Object value, Callback callback);

    float remindFloat(String key, float fallback);

    int remindInt(String key, int fallback);

    long remindLong(String key, long fallback);

    String remindString(String key, String fallback);

    boolean remindBoolean(String key, boolean fallback);

    Set<String> remindStringSet(String key, Set<String> fallback);

    <T> T remindObject(String key, @NonNull Class<T> clazz);

    boolean isRemember(String key);

    public interface Callback {
        void apply(boolean successful);
    }

}
