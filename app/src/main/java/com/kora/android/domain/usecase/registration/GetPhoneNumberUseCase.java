package com.kora.android.domain.usecase.registration;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

@ConfigPersistent
public class GetPhoneNumberUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {


    private final Context mContext;

    @Inject
    public GetPhoneNumberUseCase(Context context) {
        this.mContext = context;
    }

    @Override
    protected Single buildTask() {
        return Single.just(true).map(a -> {
            final TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getLine1Number();
        });
    }
}
