package com.kora.android.presentation.service.wallet;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.di.component.ServiceComponent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.service.BaseService;
import com.kora.android.presentation.ui.base.custom.RetryAction;
import com.kora.android.presentation.ui.base.view.BaseActivity;

public class CreateWalletService extends BaseService<CreateWalletPresenter> implements CreateWalletContractor {

    private static final int NOTIFICATION_ID = 11;

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final UserEntity userEntity) {
        final Intent intent = new Intent(baseActivity, CreateWalletService.class);
        intent.putExtra(Keys.Extras.EXTRA_USER, userEntity);
        return intent;
    }

    @Override
    public void injectToComponent(final ServiceComponent serviceComponent) {
        serviceComponent.inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Toast.makeText(this, R.string.wallet_service_start_message, Toast.LENGTH_LONG).show();
        startForeground(1, getNotification());

        final UserEntity userEntity = intent.getParcelableExtra(Keys.Extras.EXTRA_USER);
        getPresenter().setUserEntity(userEntity);
        getPresenter().createWallets();

        return START_STICKY;
    }

    @Override
    public void showError(String message, RetryAction createIdentityAction) {
        showNotification(NOTIFICATION_ID, message, true);
    }

    @Override
    public void showCreateIdentityMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.wallet_service_creating_identity_message), true);
    }

    @Override
    public void showUpdateUserMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.wallet_service_updating_user_message), true);
    }

    @Override
    public void showIncreaseBalanceMessage() {
        showNotification(NOTIFICATION_ID, getString(R.string.wallet_service_increasing_balance_message), true);
    }

    @Override
    public void finishService() {
        Toast.makeText(this, R.string.wallet_service_finished_message, Toast.LENGTH_LONG).show();
        cancelNotification(NOTIFICATION_ID);
        stopForeground(true);
        stopSelf();
    }
}
