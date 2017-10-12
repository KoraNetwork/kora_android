package com.kora.android;

import android.app.Application;
import android.content.Context;

import com.kora.android.di.component.ApplicationComponent;
import com.kora.android.di.component.DaggerApplicationComponent;
import com.kora.android.di.module.ApplicationModule;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

public class KoraApplication extends Application {

    protected ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        RxPaparazzo.register(this);

        getComponent().inject(this);
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
