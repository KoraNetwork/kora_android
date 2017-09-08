package com.kora.android.data.web3j.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.kora.android.data.web3j.model.EtherWallet;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class EtherWalletStorage {

    private static final String WALLET_LIST_FILE_NAME = "wallets";
    private static final String EXPORT_FOLDER_NAME = "Kora";
    private static final String JSON_FILE_EXTENSION = ".json";

    private final Context mContext;

    public EtherWalletStorage(final Context context) {
        this.mContext = context;
    }

    public List<EtherWallet> getWalletList() {
        List<EtherWallet> etherWalletList;
        File file;
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            file = new File(mContext.getFilesDir(), WALLET_LIST_FILE_NAME);
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            objectInputStream = new ObjectInputStream(bufferedInputStream);
        } catch (Exception ignored) {
        }
        try {
            etherWalletList = (ArrayList<EtherWallet>) objectInputStream.readObject();
        } catch (Exception e) {
            etherWalletList = new ArrayList<>();
            Log.e("_____", e.toString());
            e.printStackTrace();
        }
        try {
            if (objectInputStream != null)
                objectInputStream.close();
            if (bufferedInputStream != null)
                bufferedInputStream.close();
            if (fileInputStream != null)
                fileInputStream.close();
        } catch (Exception ignored) {
        }
        return etherWalletList;
    }

    public void addWallet(final EtherWallet etherWallet) {
        final List<EtherWallet> etherWalletList = getWalletList();
        if (etherWalletList.contains(etherWallet))
            return;
        etherWalletList.add(etherWallet);
        saveWalletList(etherWalletList);
    }

    private void saveWalletList(List<EtherWallet> etherWalletList) {
        File file;
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            file = new File(mContext.getFilesDir(), WALLET_LIST_FILE_NAME);
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        } catch (Exception ignored) {
        }
        try {
            objectOutputStream.writeObject(etherWalletList);
        } catch (Exception e) {
            Log.e("_____", e.toString());
            e.printStackTrace();
        }
        try {
            if (objectOutputStream != null)
                objectOutputStream.close();
            if (fileOutputStream != null)
                fileOutputStream.close();
        } catch (Exception ignored) {
        }
    }

    public void exportWallet(final String walletFileName) {
        final List<EtherWallet> etherWalletList = getWalletList();
        final EtherWallet etherWallet = new EtherWallet(walletFileName);
        if (!etherWalletList.contains(etherWallet))
            return;
        final File folder = new File(Environment.getExternalStorageDirectory(), EXPORT_FOLDER_NAME);
        if (!folder.exists())
            folder.mkdirs();
        final File original = new File(mContext.getFilesDir(), walletFileName);
        final File copy = new File(folder, walletFileName + JSON_FILE_EXTENSION);

        copyFile(original, copy);

        final Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        final Uri fileUri = Uri.fromFile(copy);
        mediaScannerIntent.setData(fileUri);
        mContext.sendBroadcast(mediaScannerIntent);
    }

    private void copyFile(final File original, final File copy) {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(original).getChannel();
            outChannel = new FileOutputStream(copy).getChannel();
        } catch (Exception ignored) {
        }
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (Exception e) {
            Log.e("_____", e.toString());
            e.printStackTrace();
        }
        try {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        } catch (Exception ignored) {
        }
    }

    public Credentials getCredentials(final String walletFileName, final String password) {
        final File file = new File(mContext.getFilesDir(), walletFileName);
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password, file);
        } catch (Exception e) {
            Log.e("_____", e.toString());
            e.printStackTrace();
        }
        return credentials;
    }
}
