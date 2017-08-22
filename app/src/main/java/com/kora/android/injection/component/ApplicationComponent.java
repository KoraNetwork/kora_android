package com.kora.android.injection.component;

import android.content.Context;

import com.kora.android.KoraApplication;
import com.kora.android.data.local.AuthPrefHelper;
import com.kora.android.injection.module.ApplicationModule;
import com.kora.android.injection.module.NetworkModule;
import com.kora.android.injection.module.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class, RepositoryModule.class})
public interface ApplicationComponent {

    void inject(final KoraApplication application);

    Context context();

    AuthPrefHelper authPrefHelper();

//    SomeService someService();
//    SomeRepository someRepository();
}
