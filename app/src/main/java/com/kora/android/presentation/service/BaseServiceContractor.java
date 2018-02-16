package com.kora.android.presentation.service;

import android.app.PendingIntent;

import com.kora.android.di.component.ServiceComponent;

public interface BaseServiceContractor<P extends BaseServicePresenter>  {

    P getPresenter();

    void injectToComponent(final ServiceComponent serviceComponent);

    void showNotification(final int id,
                          final String title,
                          final String text,
                          final boolean cancelable,
                          final PendingIntent onClick);

    void showError(final int id,
                   final String title,
                   final String text,
                   final boolean cancelable,
                   final PendingIntent onClick,
                   final PendingIntent ok);

    void showErrorWithRetry(final int id,
                            final String title,
                            final String text,
                            final boolean cancelable,
                            final PendingIntent onClick,
                            final PendingIntent action,
                            final PendingIntent cancel);

    void cancelNotification(final int id);
}
