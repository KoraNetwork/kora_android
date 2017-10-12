package com.kora.android.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PreferenceHelper {

    private static final PreferenceHelper INSTANCE = new PreferenceHelper();

    private static final String TAG = PreferenceHelper.class.getSimpleName();

    private static final Object SHARED_PREFS_LOCK = new Object();
    private static String mSharedPrefsName;
    private volatile boolean mWasInitialized = false;
    private volatile Context mAppContext;

    private ConcurrentMap<String, Object> mData;

    private PreferenceHelper() {

    }

    public static synchronized PreferenceHelper init(Context context, String sharedPrefsName) {
        // Defensive checks
        if (context == null || TextUtils.isEmpty(sharedPrefsName)) {
            throw new RuntimeException(
                    "You must provide a valid context and shared prefs name when initializing Memory");
        }

        // Initialize ourselves
        if (!INSTANCE.mWasInitialized) {
            INSTANCE.initWithContext(context, sharedPrefsName);
        }

        return INSTANCE;
    }

    private static PreferenceHelper getInstance() {
        if (!INSTANCE.mWasInitialized) {
            throw new RuntimeException(
                    "PreferenceHelper was not initialized! You must call PreferenceHelper.init() before using this.");
        }
        return INSTANCE;
    }

    public static void forgetAll() {
        forgetAll(null);
    }

    public static void forgetAll(final Callback callback) {
        getInstance().mData.clear();
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                synchronized (SHARED_PREFS_LOCK) {
                    SharedPreferences.Editor editor = getInstance().getSharedPreferences().edit();
                    editor.clear();
                    return editor.commit();
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (callback != null) {
                    callback.apply(success);
                }
            }
        };
        if (getInstance().newerThenHoneycomb())
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else asyncTask.execute();
    }

    public static void forget(String key) {
        forget(key, null);
    }

    public static void forget(final String key, final Callback callback) {
        getInstance().mData.remove(key);
        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                synchronized (SHARED_PREFS_LOCK) {
                    SharedPreferences.Editor editor = getInstance().getSharedPreferences().edit();
                    editor.remove(key);
                    return editor.commit();
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (callback != null) {
                    callback.apply(success);
                }
            }
        };
        if (getInstance().newerThenHoneycomb())
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else asyncTask.execute();
    }

    public static PreferenceHelper rememberFloat(final String key, final float value) {
        return getInstance().saveAsync(key, value, null);
    }

    public static PreferenceHelper rememberInt(String key, int value) {
        return getInstance().saveAsync(key, value, null);
    }

    public static PreferenceHelper rememberLong(String key, long value) {
        return getInstance().saveAsync(key, value, null);
    }

    public static PreferenceHelper rememberString(String key, String value) {
        return getInstance().saveAsync(key, value, null);
    }

    public static PreferenceHelper rememberBoolean(String key, boolean value) {
        return getInstance().saveAsync(key, value, null);
    }

    public static PreferenceHelper rememberStringSet(String key, Set<String> value) {
        return getInstance().saveAsync(key, value, null);
    }

    public static PreferenceHelper rememberObject(String key, Object value) {
        return getInstance().saveAsync(key, new Gson().toJson(value), null);
    }

    public static PreferenceHelper rememberFloat(final String key, final float value, final Callback callback) {
        return getInstance().saveAsync(key, value, callback);
    }

    public static PreferenceHelper rememberInt(String key, int value, Callback callback) {
        return getInstance().saveAsync(key, value, callback);
    }

    public static PreferenceHelper rememberLong(String key, long value, Callback callback) {
        return getInstance().saveAsync(key, value, callback);
    }

    public static PreferenceHelper rememberString(String key, String value, Callback callback) {
        return getInstance().saveAsync(key, value, callback);
    }

    public static PreferenceHelper rememberBoolean(String key, boolean value, Callback callback) {
        return getInstance().saveAsync(key, value, callback);
    }

    public static PreferenceHelper rememberStringSet(String key, Set<String> value, Callback callback) {
        return getInstance().saveAsync(key, value, callback);
    }

    public static PreferenceHelper rememberObject(String key, Object value, Callback callback) {
        return getInstance().saveAsync(key, new Gson().toJson(value), callback);
    }

    public static float remindFloat(String key, float fallback) {
        Float value = getInstance().get(key, Float.class);
        return value != null ? value : fallback;
    }

    public static int remindInt(String key, int fallback) {
        Integer value = getInstance().get(key, Integer.class);
        return value != null ? value : fallback;
    }

    public static long remindLong(String key, long fallback) {
        Long value = getInstance().get(key, Long.class);
        return value != null ? value : fallback;
    }

    public static String remindString(String key, String fallback) {
        String value = getInstance().get(key, String.class);
        return value != null ? value : fallback;
    }

    public static boolean remindBoolean(String key, boolean fallback) {
        Boolean value = getInstance().get(key, Boolean.class);
        return value != null ? value : fallback;
    }

    public static Set<String> remindStringSet(String key, Set<String> fallback) {
        Set<String> value = getInstance().get(key, Set.class);
        return value != null ? value : fallback;
    }

    @Nullable
    public static <T> T remindObject(String key, @NonNull Class<T> clazz) {
        return new Gson().fromJson(getInstance().get(key, String.class), clazz);
    }

    public static boolean isRemember(String key) {
        return getInstance().mData.containsKey(key);
    }

    private void initWithContext(Context context, String sharedPrefsName) {
        // Time ourselves
        long start = SystemClock.uptimeMillis();

        // Set vars
        mAppContext = context.getApplicationContext();
        mSharedPrefsName = sharedPrefsName;

        // Read from shared prefs
        SharedPreferences prefs = getSharedPreferences();
        mData = new ConcurrentHashMap<>();
        mData.putAll(prefs.getAll());
        mWasInitialized = true;

        long delta = SystemClock.uptimeMillis() - start;

    }

    private SharedPreferences getSharedPreferences() {
        return mAppContext.getSharedPreferences(mSharedPrefsName, Context.MODE_PRIVATE);
    }

    private boolean saveToDisk(final String key, final Object value) {
        boolean success = false;
        synchronized (SHARED_PREFS_LOCK) {
            // Save it to disk
            SharedPreferences.Editor editor = getSharedPreferences().edit();
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
        }

        return success;
    }

    private boolean newerThenHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private <T> PreferenceHelper saveAsync(final String key, final T value, final Callback callback) {
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

    private <T> T get(String key, Class<T> clazz) {
        Object value = mData.get(key);
        T castedObject = null;
        if (clazz.isInstance(value)) {
            castedObject = clazz.cast(value);
        }
        return castedObject;
    }

    public interface Callback {
        void apply(boolean successful);
    }
}
