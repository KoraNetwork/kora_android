package com.kora.android.presentation.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.kora.android.R;
import com.kora.android.common.permission.PermissionCheckerActivity;
import com.kora.android.common.permission.PermissionException;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import java.math.BigInteger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.kora.android.common.Keys.PermissionChecker.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE;

public class TestActivity extends BaseActivity<TestPresenter> implements TestView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_view_main_screen)
    TextView mTvMainScreen;

    @Inject
    PermissionCheckerActivity mPermissionCheckerActivity;

    public static Intent getLaunchIntent(BaseActivity baseActivity) {
        final Intent intent = new Intent(baseActivity, TestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void injectToComponent(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_test;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_grey);

        final String password = "1234";
        final String privateKey = "29984ecd73f5e4f9e2fc5ce49dfe61a5b13571ab34c0d427ca066a170cc4e851";
//        final String privateKey = "fc0c329808e37c81fa441809f648f091c14c530177083fec386b7a220fb460e5";

        // create new wallet
//        getPresenter().generateWallet(password, null);

        // import wallet
//        getPresenter().generateWallet(password, privateKey);

//        getPresenter().getWalletList();

//        showExportWalletDialog();

        getPresenter().sendTransaction(
                "0x5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890.json".toLowerCase(),
                "123456789",
                "0x5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890".toLowerCase(),
                "0x2c4169db0a11e2ba3b0af42ee6a886f6f77e65b6".toLowerCase(),
                new BigInteger("1000000000000000000"));

//        getPresenter().createIdentity();

//        final TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        final String phoneNumber = telephonyManager.getLine1Number();
//
//        if (phoneNumber == null)
//            Log.e("_____", "NULL");
//        else if (phoneNumber.isEmpty())
//            Log.e("_____", "EMPTY");
//        else if (!phoneNumber.isEmpty())
//            Log.e("_____", phoneNumber);
    }

    @OnClick(R.id.text_view_main_screen)
    public void onClickMainScreen() {
        getPresenter().startGetCountriesTask();
    }

    public void exportWallet() {
        final String walletFileName = "5c3d13b00f0fde8de60c45ab62ec0125c6b0f890";
        try {
            mPermissionCheckerActivity.verifyPermissions(WRITE_EXTERNAL_STORAGE);
            getPresenter().exportWallet(walletFileName);
        } catch (PermissionException e) {
            mPermissionCheckerActivity.requestPermissions(PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE, e.getRequiredPermissions());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (mPermissionCheckerActivity.permissionsGranted(permissions, grantResults))
                    exportWallet();
                else
                    showDialogMessage(R.string.export_wallet_pemission_dialog_title, R.string.export_wallet_permission_dialog_message);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
