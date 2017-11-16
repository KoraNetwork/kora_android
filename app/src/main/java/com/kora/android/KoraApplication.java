package com.kora.android;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.kora.android.di.component.ApplicationComponent;
import com.kora.android.di.component.DaggerApplicationComponent;
import com.kora.android.di.module.ApplicationModule;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import io.fabric.sdk.android.Fabric;

public class KoraApplication extends Application {

    private static KoraApplication mInstance;

    protected ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        getComponent().inject(this);

        RxPaparazzo.register(this);

//        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
//        }
    }

    public static KoraApplication getContext() {
        return mInstance;
    }

    public static KoraApplication get(final Context context) {
        return (KoraApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }
}
