package com.kora.android.presentation.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.kora.android.presentation.receiver.IncomingSmsReceiver;

import static android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION;
import static com.kora.android.common.Keys.IncomingSms.INCOMING_SMS_ACTION_RECEIVED;
import static com.kora.android.common.Keys.IncomingSms.INCOMING_SMS_EXTRA_MESSAGE;

public class IncomingSmsService extends Service implements IncomingSmsReceiver.ReceiverListener {

    private IncomingSmsReceiver mIncomingSmsReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mIncomingSmsReceiver = new IncomingSmsReceiver();
        registerReceiver(mIncomingSmsReceiver, new IntentFilter(SMS_RECEIVED_ACTION));
        mIncomingSmsReceiver.setReceiverListener(this);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mIncomingSmsReceiver.releaseReceiverListener();
        unregisterReceiver(mIncomingSmsReceiver);
        super.onDestroy();
    }

    @Override
    public void onReceiveSms(final String message) {
        final Intent intent = new Intent(INCOMING_SMS_ACTION_RECEIVED);
        intent.putExtra(INCOMING_SMS_EXTRA_MESSAGE, message);
        mLocalBroadcastManager.sendBroadcast(intent);
    }
}
