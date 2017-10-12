package com.kora.android.domain.usecase.registration;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.di.annotation.ConfigPersistent;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class GetPhoneNumberUseCase extends AsyncUseCase {

    private final Context mContext;

    @Inject
    public GetPhoneNumberUseCase(final Context context) {
        mContext = context;
    }

    @Override
    protected Observable buildObservableTask() {
        return Observable.just(true).map(a -> {
            final TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getLine1Number(); // TODO: permissions
        });
    }
}
