package com.kora.android.presentation.service.wallet;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.ServiceComponent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.service.BaseService;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.view.BaseActivity;

public class CreateWalletService extends BaseService<CreateWalletPresenter> implements CreateWalletContractor {

    private static final int NOTIFICATION_ID = 11;

    public static Intent getLaunchIntent(final BaseActivity baseActivity, UserEntity userEntity) {
        final Intent intent = new Intent(baseActivity, CreateWalletService.class);
        intent.putExtra(Keys.Extras.EXTRA_USER, userEntity);
        return intent;
    }

    @Override
    public void injectToComponent(ServiceComponent serviceComponent) {
        serviceComponent.inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, getNotification());
        UserEntity user = intent.getParcelableExtra(Keys.Extras.EXTRA_USER);
        getPresenter().createWallet(user);

        return START_STICKY;
    }

    @Override
    public void showError(String message, RetryAction createIdentityAction) {
        showNotification(NOTIFICATION_ID, message, true);
    }

    @Override
    public void showCreateIdentityMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.registration_notification_creating_identity_message), false);
    }

    @Override
    public void showCreatedIdentityMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.registration_notification_created_identity_message), true);
    }

    @Override
    public void showUpdateUserMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.registration_notification_updating_user_message), false);
    }

    @Override
    public void showUpdatedUserMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.registration_notification_updated_user_message), true);
    }

    @Override
    public void showIncreaseBalanceMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.registration_notification_increasing_balance_message), false);
    }

    @Override
    public void showIncreasedBalanceMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.registration_notification_increased_balance_message), true);
    }

    @Override
    public void finishService() {
        showNotification(NOTIFICATION_ID, getString(R.string.registration_notification_service_finished_message), true);
        stopForeground(false);
        stopSelf();
    }
}
