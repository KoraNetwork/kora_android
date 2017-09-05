package com.kora.android.presentation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kora.android.R;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.web3j.StorableWallet;
import com.kora.android.web3j.WalletStorage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    public static Intent getLaunchIntent(BaseActivity baseActivity) {
        final Intent intent = new Intent(baseActivity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        try {
            getWallets();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWallets() throws IOException {

        final WalletStorage walletStorage = WalletStorage.getInstance(this);
        final ArrayList<StorableWallet> storableWallets = new ArrayList<>(walletStorage.get());
        for (int i = 0; i < storableWallets.size(); i++) {
            Log.e("_____", storableWallets.get(i).toString());
        }
    }
}
