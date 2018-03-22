package com.kora.android.presentation.service.wallet;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.kora.android.R;
import com.kora.android.di.component.ServiceComponent;
import com.kora.android.presentation.service.BaseService;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.main.MainActivity;

public class CreateWalletsService extends BaseService<CreateWalletsPresenter> implements CreateWalletsContractor {

    private static final int NOTIFICATION_ID = 111;
    private static final String ACTION_TRY_AGAIN = "com.kora.android.action_try_again";
    private static final String ACTION_CANCEL = "com.kora.android.action_cancel";
    public static final String ARG_CREATE_WALLETS = "com.kora.android.extra_create_wallets";
    public static final String ARG_CREATE_IDENTITY = "com.kora.android.extra_create_identity";
    public static final String ARG_INCREASE_BALANCE = "com.kora.android.extra_increase_balance";

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, CreateWalletsService.class);
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
        startForeground(NOTIFICATION_ID, getNotification());
        registerReceiver(mTryAgainReceiver, new IntentFilter(ACTION_TRY_AGAIN));
        registerReceiver(mCancelReceiver, new IntentFilter(ACTION_CANCEL));
        getPresenter().createWallets();
        return START_STICKY;
    }

    private PendingIntent getMainActivityIntent() {
        return PendingIntent.getActivity(
                this,
                111,
                MainActivity.getLaunchIntent(this),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    @Override
    public void showError(final String text) {
        Toast.makeText(this, R.string.wallet_service_error_message, Toast.LENGTH_LONG).show();
        final PendingIntent ok = PendingIntent.getBroadcast(
                this,
                222,
                new Intent(ACTION_CANCEL),
                PendingIntent.FLAG_CANCEL_CURRENT);
        showError(
                NOTIFICATION_ID,
                getString(R.string.wallet_service_notification_error_title),
                text,
                true,
                getMainActivityIntent(),
                ok);
    }

    @Override
    public void showErrorWithRetry(final String retryActionArg) {
        Toast.makeText(this, R.string.wallet_service_error_message, Toast.LENGTH_LONG).show();
        final Intent intent = new Intent(ACTION_TRY_AGAIN);
        final Bundle bundle = new Bundle();
        bundle.putString(retryActionArg, retryActionArg);
        intent.putExtras(bundle);
        final PendingIntent tryAgain = PendingIntent.getBroadcast(
                this,
                333,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        final PendingIntent cancel = PendingIntent.getBroadcast(
                this,
                444,
                new Intent(ACTION_CANCEL),
                PendingIntent.FLAG_CANCEL_CURRENT);
        showErrorWithRetry(
                NOTIFICATION_ID,
                getString(R.string.wallet_service_notification_error_title),
                getString(R.string.wallet_service_notification_error_message),
                true,
                getMainActivityIntent(),
                tryAgain,
                cancel);
    }

    @Override
    public void showCreateKeystoreFileMessage() {
        Toast.makeText(this, R.string.wallet_service_creating_keystore_file_message, Toast.LENGTH_LONG).show();
        showNotification(
                NOTIFICATION_ID,
                getString(R.string.wallet_service_notification_title),
                getString(R.string.wallet_service_creating_keystore_file_message),
                true,
                getMainActivityIntent());
    }

    @Override
    public void showCreateIdentityMessage() {
        Toast.makeText(this, R.string.wallet_service_creating_identity_message, Toast.LENGTH_LONG).show();
        showNotification(
                NOTIFICATION_ID,
                getString(R.string.wallet_service_notification_title),
                getString(R.string.wallet_service_creating_identity_message),
                true,
                getMainActivityIntent());
    }

    @Override
    public void showIncreaseBalanceMessage() {
        Toast.makeText(this, R.string.wallet_service_increasing_balance_message, Toast.LENGTH_LONG).show();
        showNotification(
                NOTIFICATION_ID,
                getString(R.string.wallet_service_notification_title),
                getString(R.string.wallet_service_increasing_balance_message),
                true,
                getMainActivityIntent());
    }

    @Override
    public void finishService(final boolean success) {
        if (success) {
            Toast.makeText(this, R.string.wallet_service_finished_success_message, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.wallet_service_finished_failure_message, Toast.LENGTH_LONG).show();
        }
        cancelNotification(NOTIFICATION_ID);
        unregisterReceiver(mTryAgainReceiver);
        unregisterReceiver(mCancelReceiver);
        stopForeground(true);
        stopSelf();
    }

    private final BroadcastReceiver mTryAgainReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final Bundle bundle = intent.getExtras();
            if (bundle == null) return;
            if (bundle.containsKey(ARG_CREATE_WALLETS)) {
                getPresenter().retryCreateWallets();
            } else if (bundle.containsKey(ARG_CREATE_IDENTITY)) {
                getPresenter().retryCreateIdentity();
            } else if (bundle.containsKey(ARG_INCREASE_BALANCE)) {
                getPresenter().retryIncreaseBalance();
            }
        }
    };

    private final BroadcastReceiver mCancelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            finishService(false);
        }
    };
}
