package com.kora.android.presentation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import static com.kora.android.common.Keys.IncomingSms.INCOMING_SMS_SENDER;

public class IncomingSmsReceiver extends BroadcastReceiver {

    private static final String PDUS = "pdus";

    private ReceiverListener mReceiverListener;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            final Bundle bundle = intent.getExtras();
            if (bundle == null) return;
            final Object[] pdusObjects = (Object[]) bundle.get(PDUS);
            if (pdusObjects == null) return;
            for (Object pdusObject : pdusObjects) {
                final SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObject);
                final String sender = smsMessage.getDisplayOriginatingAddress();
                final String message = smsMessage.getDisplayMessageBody();
                if (sender.equals(INCOMING_SMS_SENDER))
                    mReceiverListener.onReceiveSms(message);
            }
        } catch (Exception e) {
            Log.e("_____", e.toString());
            e.printStackTrace();
        }
    }

    public void setReceiverListener(final ReceiverListener receiverListener) {
        if (mReceiverListener == null)
            mReceiverListener = receiverListener;
    }

    public void releaseReceiverListener() {
        if (mReceiverListener != null)
            mReceiverListener = null;
    }

    public interface ReceiverListener {
        void onReceiveSms(final String message);
    }
}
