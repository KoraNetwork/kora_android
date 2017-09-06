package com.kora.android.data.web3j.storage;

import android.content.Context;
import android.util.Log;

import com.kora.android.data.web3j.model.EtherWallet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EtherWalletStorage {

    private final static String WALLETS_FILE_NAME = "wallets.dat";

    private final Context mContext;

    private ArrayList<EtherWallet> etherWalletList;

    public EtherWalletStorage(Context context) {
        mContext = context;
        loadWalletList();
    }

    private void loadWalletList() {
        File file;
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            file = new File(mContext.getFilesDir(), WALLETS_FILE_NAME);
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            objectInputStream = new ObjectInputStream(bufferedInputStream);
        } catch (Exception ignored) {
        }
        try {
            etherWalletList = (ArrayList<EtherWallet>) objectInputStream.readObject();
        } catch (Exception e) {
            Log.e("_____", e.toString());
            e.printStackTrace();
            etherWalletList = new ArrayList<>();
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
    }

    public ArrayList<EtherWallet> getWalletList() {
        return etherWalletList;
    }

    public boolean addWallet(EtherWallet etherWallet) {
        for (int i = 0; i < etherWalletList.size(); i++)
            if (etherWalletList.get(i).getPublicKey().equalsIgnoreCase(etherWallet.getPublicKey()))
                return false;
        etherWalletList.add(etherWallet);
        saveWalletList();
        return true;
    }

    private void saveWalletList() {
        File file;
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            file = new File(mContext.getFilesDir(), WALLETS_FILE_NAME);
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
}
