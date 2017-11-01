package com.kora.android.presentation.service;

import com.kora.android.di.component.ServiceComponent;

public interface BaseServiceContractor<P extends BaseServicePresenter>  {

    P getPresenter();

    void injectToComponent(final ServiceComponent serviceComponent);

    void showNotification(int id, String message, boolean cancelable);
}
