package com.kora.android.presentation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kora.android.R;
import com.kora.android.common.permission.PermissionChecker;
import com.kora.android.common.permission.SecurityException;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.RawTransaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.infura.InfuraHttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

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

        sendTransaction();
    }

    private void sendTransaction() {
        new Thread(() -> {
            final InfuraHttpService infuraHttpService = new InfuraHttpService("https://ropsten.infura.io/Q86Oq3CueCrb1idXkNGW");
            final Web3j web3j = Web3jFactory.build(infuraHttpService);

//            try {
//                final Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
//                final String clientVersion = web3ClientVersion.getWeb3ClientVersion();
//                Log.e("_____", clientVersion);
//            } catch (Exception e) {
//                Log.e("_____", "Exception");
//                e.printStackTrace();
//            }

//            try {
//                final EthGetBalance ethGetBalance = web3j
//                        .ethGetBalance("0x5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890", DefaultBlockParameterName.LATEST)
//                        .sendAsync()
//                        .get();
//                final BigInteger balance = ethGetBalance.getBalance();
//                Log.e("_____", String.valueOf(balance));
//            } catch (Exception e) {
//                Log.e("_____", "Exception");
//                e.printStackTrace();
//            }

//            try {
//                final EthGetTransactionCount ethGetTransactionCount = web3j
//                        .ethGetTransactionCount("0x5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890", DefaultBlockParameterName.LATEST)
//                        .sendAsync()
//                        .get();
//                final BigInteger transactionCount = ethGetTransactionCount.getTransactionCount();
//                Log.e("_____", String.valueOf(transactionCount));
//            } catch (Exception e) {
//                Log.e("_____", "Exception");
//                e.printStackTrace();
//            }

            try {
                final String password = "123456789";
                final String walletFileName = "5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890".toLowerCase();

                final File file = new File(this.getFilesDir(), walletFileName);
                final Credentials credentials = WalletUtils.loadCredentials(password, file);

                // get the next available nonce
                final EthGetTransactionCount ethGetTransactionCount = web3j
                        .ethGetTransactionCount("0x5c3D13b00F0fdE8dE60C45aB62EC0125C6b0F890", DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
                final BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                // create our transaction
                final BigInteger amount = new BigInteger("100000000000000000");

                final RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                        nonce,
                        Contract.GAS_PRICE,
                        Contract.GAS_LIMIT,
                        "0x97bb2587B02715e2936b95f36892a457966757FF",
                        amount);

                // sign & send our transaction
                final byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                final String hexValue = Numeric.toHexString(signedMessage);
                final EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
                Log.e("_____", ethSendTransaction.getTransactionHash());

            } catch (Exception e) {
                Log.e("_____", e.toString());
                e.printStackTrace();
            }

        }).start();
    }

    public void exportWallet() {
        final String walletFileName = "97bb2587b02715e2936b95f36892a457966757ff";
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
