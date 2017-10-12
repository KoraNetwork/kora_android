package com.kora.android.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.kora.android.common.Keys;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceHandlerImpl implements PreferenceHandler {

    private Context mAppContext;
    private String mSharedPrefsName;

    private SharedPreferences mSharedPreferences;
    private ConcurrentMap<String, Object> mData;

    @Inject
    public PreferenceHandlerImpl(final Context context) {
        init(context, Keys.Shared.NAME);
    }

    private void init(Context context, String sharedPrefsName) {
        mAppContext = context.getApplicationContext();
        mSharedPrefsName = sharedPrefsName;

        mSharedPreferences = mAppContext.getSharedPreferences(mSharedPrefsName, Context.MODE_PRIVATE);
        mData = new ConcurrentHashMap<>();
        mData.putAll(mSharedPreferences.getAll());

    }

    private boolean newerThenHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private <T> PreferenceHandler saveAsync(final String key, final T value, final Callback callback) {
        // Put it in memory
        mData.put(key, value);

        // Save it to disk
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                return saveToDisk(key, value);
            }

            @Override
            protected void onPostExecute(Boolean success) {
                // Fire the callback
                if (callback != null) {
                    callback.apply(success);
                }
            }
        };
        if (newerThenHoneycomb())
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else asyncTask.execute();

        return this;
    }

    private boolean saveToDisk(final String key, final Object value) {
        boolean success = false;
        // Save it to disk
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        boolean didPut = true;
        if (value instanceof Float) {
            editor.putFloat(key, (Float) value);

        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);

        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);

        } else if (value instanceof String) {
            editor.putString(key, (String) value);

        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);

        } else if (value instanceof Set) {
            if (newerThenHoneycomb()) {
                editor.putStringSet(key, (Set<String>) value);
            }

        } else {
            didPut = false;
        }

        if (didPut) {
            success = editor.commit();
        }

        return success;
    }

    private <T> T get(String key, Class<T> clazz) {
        Object value = mData.get(key);
        T castedObject = null;
        if (clazz.isInstance(value)) {
            castedObject = clazz.cast(value);
        }
        return castedObject;
    }

    @Override
    public void forgetAll() {
        forgetAll(null);
    }

    @Override
    public void forgetAll(Callback callback) {
        mData.clear();
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                return editor.commit();
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (callback != null) {
                    callback.apply(success);
                }
            }
        };
        if (newerThenHoneycomb())
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else asyncTask.execute();
    }


    @Override
    public void forget(String key) {
        forget(key, null);
    }

    @Override
    public void forget(String key, Callback callback) {
        mData.remove(key);
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.remove(key);
                return editor.commit();
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (callback != null) {
                    callback.apply(success);
                }
            }
        };
        if (newerThenHoneycomb())
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else asyncTask.execute();
    }

    @Override
    public PreferenceHandler rememberFloat(String key, float value) {
        return saveAsync(key, value, null);
    }

    @Override
    public PreferenceHandler rememberInt(String key, int value) {
        return saveAsync(key, value, null);
    }

    @Override
    public PreferenceHandler rememberLong(String key, long value) {
        return saveAsync(key, value, null);
    }

    @Override
    public PreferenceHandler rememberString(String key, String value) {
        return saveAsync(key, value, null);
    }

    @Override
    public PreferenceHandler rememberBoolean(String key, boolean value) {
        return saveAsync(key, value, null);
    }

    @Override
    public PreferenceHandler rememberStringSet(String key, Set<String> value) {
        return saveAsync(key, value, null);
    }

    @Override
    public PreferenceHandler rememberObject(String key, Object value) {
        return saveAsync(key, new Gson().toJson(value), null);
    }

    @Override
    public PreferenceHandler rememberFloat(final String key, final float value, final Callback callback) {
        return saveAsync(key, value, callback);
    }

    @Override
    public PreferenceHandler rememberInt(String key, int value, Callback callback) {
        return saveAsync(key, value, callback);
    }

    @Override
    public PreferenceHandler rememberLong(String key, long value, Callback callback) {
        return saveAsync(key, value, callback);
    }

    @Override
    public PreferenceHandler rememberString(String key, String value, Callback callback) {
        return saveAsync(key, value, callback);
    }

    @Override
    public PreferenceHandler rememberBoolean(String key, boolean value, Callback callback) {
        return saveAsync(key, value, callback);
    }

    @Override
    public PreferenceHandler rememberStringSet(String key, Set<String> value, Callback callback) {
        return saveAsync(key, value, callback);
    }

    @Override
    public PreferenceHandler rememberObject(String key, Object value, Callback callback) {
        return saveAsync(key, new Gson().toJson(value), callback);
    }

    @Override
    public float remindFloat(String key, float fallback) {
        Float value = get(key, Float.class);
        return value != null ? value : fallback;
    }

    @Override
    public int remindInt(String key, int fallback) {
        Integer value = get(key, Integer.class);
        return value != null ? value : fallback;
    }

    @Override
    public long remindLong(String key, long fallback) {
        Long value = get(key, Long.class);
        return value != null ? value : fallback;
    }

    @Override
    public String remindString(String key, String fallback) {
        String value = get(key, String.class);
        return value != null ? value : fallback;
    }

    @Override
    public boolean remindBoolean(String key, boolean fallback) {
        Boolean value = get(key, Boolean.class);
        return value != null ? value : fallback;
    }

    @Override
    public Set<String> remindStringSet(String key, Set<String> fallback) {
        Set<String> value = get(key, Set.class);
        return value != null ? value : fallback;
    }

    @Override
    @Nullable
    public <T> T remindObject(String key, @NonNull Class<T> clazz) {
        return new Gson().fromJson(get(key, String.class), clazz);
    }

    @Override
    public boolean isRemember(String key) {
        return mData.containsKey(key);
    }

}
