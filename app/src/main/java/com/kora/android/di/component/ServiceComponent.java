package com.kora.android.di.component;

import com.kora.android.di.annotation.PerService;
import com.kora.android.di.module.ServiceModule;
import com.kora.android.presentation.service.wallet.CreateWalletService;

import dagger.Subcomponent;

@PerService
@Subcomponent(modules = ServiceModule.class)
public interface ServiceComponent {

    void inject(CreateWalletService createWalletService);
}
