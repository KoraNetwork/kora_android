package com.kora.android.presentation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.common.permission.PermissionChecker;
import com.kora.android.common.permission.SecurityException;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.kora.android.common.Keys.PermissionChecker.PERMISSION_REQUEST_CODE;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    PermissionChecker mPermissionChecker;

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
        setToolbar(mToolbar, R.drawable.ic_back_black);

        final String password = "123456789";
        final String privateKey = "29984ecd73f5e4f9e2fc5ce49dfe61a5b13571ab34c0d427ca066a170cc4e851";

        // create new wallet
//        getPresenter().generateWallet(password, null);

        // import wallet
//        getPresenter().generateWallet(password, privateKey);

//        getPresenter().getWalletList();

//        exportWallet();

//        getPresenter().sendTransaction(
//                "5c3d13b00f0fde8de60c45ab62ec0125c6b0f890",
//                "123456789",
//                "0x5c3d13b00f0fde8de60c45ab62ec0125c6b0f890",
//                "0x97bb2587b02715e2936b95f36892a457966757ff",
//                BigInteger.valueOf(10000000000000000L));
    }

    public void exportWallet() {
        final String walletFileName = "5c3d13b00f0fde8de60c45ab62ec0125c6b0f890";
        try {
            mPermissionChecker.verifyPermissions(WRITE_EXTERNAL_STORAGE);
            getPresenter().exportWallet(walletFileName);
        } catch (SecurityException e) {
            mPermissionChecker.requestPermissions(PERMISSION_REQUEST_CODE, e.getRequiredPermissions());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (mPermissionChecker.permissionsGranted(permissions, grantResults))
                    exportWallet();
                else
                    showDialogMessage(R.string.dialog_permission_write_title, R.string.dialog_permission_write_message);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
