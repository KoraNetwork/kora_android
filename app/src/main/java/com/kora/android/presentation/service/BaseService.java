package com.kora.android.presentation.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.support.v4.app.NotificationCompat;

import com.kora.android.KoraApplication;
import com.kora.android.R;
import com.kora.android.di.component.ConfigPersistentComponent;
import com.kora.android.di.component.DaggerConfigPersistentComponent;
import com.kora.android.di.component.ServiceComponent;
import com.kora.android.di.module.ServiceModule;

import javax.inject.Inject;

public abstract class BaseService<P extends BaseServicePresenter> extends Service implements BaseServiceContractor<P> {

    private static final String CHANEL_ID = "setup";

    @Inject
    P mPresenter;

    NotificationManager mNotificationManager;
    Notification mNotification;

    private ServiceComponent mServiceComponent;

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

    @Override
    public void showNotification(int id, String message, boolean cancelable) {
        mNotification = new NotificationCompat.Builder(this, CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(message)
                .setAutoCancel(cancelable)
                .build();

        mNotificationManager.notify(id, mNotification);
    }

    public Notification getNotification() {
        return new NotificationCompat.Builder(this, CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .build();
    }
}
