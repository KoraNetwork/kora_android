package com.kora.android.data.web3j.model;

import com.kora.android.common.utils.Web3jUtils;

import java.io.Serializable;

public class EtherWallet implements Serializable {

    private String mWalletFileName;
    private String mAddress;

    public static EtherWallet createEtherWalletFromFileName(final String walletFileName) {
        return new EtherWallet()
                .addWalletFileName(walletFileName)
                .addAddress(Web3jUtils.getAddressFromKeystoreFileName(walletFileName));
    }

    public static EtherWallet createEtherWalletFromAddress(final String address) {
        return new EtherWallet()
                .addWalletFileName(Web3jUtils.getKeystoreFileNameFromAddress(address))
                .addAddress(address);
    }

    public EtherWallet addAddress(final String address) {
        mAddress = address;
        return this;
    }

    public EtherWallet addWalletFileName(final String walletFileName) {
        mWalletFileName = walletFileName;
        return this;
    }

    public String getWalletFileName() {
        return mWalletFileName;
    }

    public void setWalletFileName(String walletFileName) {
        mWalletFileName = walletFileName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    @Override
    public String toString() {
        return "EtherWallet{" + "\n" +
                "mWalletFileName=" + mWalletFileName + "\n" +
                "mAddress=" + mAddress + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtherWallet that = (EtherWallet) o;
        return mWalletFileName.equals(that.mWalletFileName);
    }

    @Override
    public int hashCode() {
        return mWalletFileName.hashCode();
    }
}
