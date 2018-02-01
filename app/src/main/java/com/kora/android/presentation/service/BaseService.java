package com.kora.android.presentation.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.kora.android.KoraApplication;
import com.kora.android.R;
import com.kora.android.di.component.ConfigPersistentComponent;
import com.kora.android.di.component.DaggerConfigPersistentComponent;
import com.kora.android.di.component.ServiceComponent;
import com.kora.android.di.module.ServiceModule;

import javax.inject.Inject;

public abstract class BaseService<P extends BaseServicePresenter> extends Service implements BaseServiceContractor<P> {

    private static final String CHANNEL_ID = "setup";

    @Inject
    P mPresenter;

    private ServiceComponent mServiceComponent;

    private Notification mNotification;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

        createServiceComponent();
        injectToComponent(mServiceComponent);

        if (mPresenter != null)
            mPresenter.attachService(this);
    }

    protected void createServiceComponent() {
        final ConfigPersistentComponent configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(KoraApplication.get(this).getComponent())
                .build();
        mServiceComponent = configPersistentComponent.serviceComponent(new ServiceModule(this));
    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachService();
        super.onDestroy();
    }

    private NotificationCompat.Builder getBuilder(final String title,
                                                  final String text,
                                                  final boolean cancelable) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_notification_large))
                .setColor(getResources().getColor(R.color.color_notification_icon))
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(cancelable);
    }

    @Override
    public void showNotification(final int id,
                                 final String title,
                                 final String text,
                                 final boolean cancelable) {
        mNotification = getBuilder(title, text, cancelable)
                .build();
        hideSmallIcon(mNotification);
        mNotificationManager.notify(id, mNotification);
    }

    @Override
    public void showError(final int id,
                          final String title,
                          final String text,
                          final boolean cancelable,
                          final PendingIntent ok) {
        mNotification = getBuilder(title, text, cancelable)
                .addAction(R.drawable.ic_ok, "OK", ok)
                .build();
        hideSmallIcon(mNotification);
        mNotificationManager.notify(id, mNotification);
    }

    @Override
    public void showErrorWithRetry(final int id,
                                   final String title,
                                   final String text,
                                   final boolean cancelable,
                                   final PendingIntent retry,
                                   final PendingIntent cancel) {
        mNotification = getBuilder(title, text, cancelable)
                .addAction(R.drawable.ic_try_again, "Try again", retry)
                .addAction(R.drawable.ic_cancel, "Cancel", cancel)
                .build();
        hideSmallIcon(mNotification);
        mNotificationManager.notify(id, mNotification);
    }

    @Override
    public void cancelNotification(final int id) {
        mNotificationManager.cancel(id);
    }

    public Notification getNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .build();
    }

    private void hideSmallIcon(final Notification notification) {
        int smallIconId = this.getResources().getIdentifier(
                "right_icon",
                "id",
                android.R.class.getPackage().getName());
        if (smallIconId != 0) {
            if (notification.contentView != null) {
                notification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);
                notification.bigContentView.setViewVisibility(smallIconId, View.INVISIBLE);
            }
        }
    }
}
