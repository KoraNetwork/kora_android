package com.kora.android.data.web3j.model;

import java.io.Serializable;

public class EtherWallet implements Serializable {

    private static final String ADDRESS_PREFIX = "0x";
    private static final String OWNER_WALLET_TYPE = "OWNER_WALLET_TYPE";
    private static final String RECOVERY_WALLET_TYPE = "RECOVERY_WALLET_TYPE";
    private static final String KORA_WALLET_TYPE = "KORA_WALLET_TYPE";

    private String mWalletFileName;
    private String mAddress;
    private String mWalletType;

    public EtherWallet(final String walletFileName) {
        mWalletFileName = walletFileName;
        mAddress = ADDRESS_PREFIX + walletFileName;
    }

    public EtherWallet(final String walletFileName, final String walletType) {
        mWalletFileName = walletFileName;
        mAddress = ADDRESS_PREFIX + walletFileName;
        mWalletType = walletType;
    }

    public static EtherWallet creteOwnerEtherWallet(final String walletFileName) {
        return new EtherWallet(walletFileName, OWNER_WALLET_TYPE);
    }

    public static EtherWallet creteRecoveryEtherWallet(final String walletFileName) {
        return new EtherWallet(walletFileName, RECOVERY_WALLET_TYPE);
    }

    public static EtherWallet creteKoraEtherWallet(final String walletFileName) {
        return new EtherWallet(walletFileName, KORA_WALLET_TYPE);
    }

    public boolean isOwnerWallet(final EtherWallet etherWallet) {
        return etherWallet.getWalletType().equals(OWNER_WALLET_TYPE);
    }

    public boolean isRecoveryWallet(final EtherWallet etherWallet) {
        return etherWallet.getWalletType().equals(RECOVERY_WALLET_TYPE);
    }

    public boolean isKoraWallet(final EtherWallet etherWallet) {
        return etherWallet.getWalletType().equals(KORA_WALLET_TYPE);
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getWalletFileName() {
        return mWalletFileName;
    }

    public void setWalletFileName(String walletFileName) {
        this.mWalletFileName = walletFileName;
    }

    public String getWalletType() {
        return mWalletType;
    }

    public void setWalletType(String mWalletType) {
        this.mWalletType = mWalletType;
    }

    @Override
    public String toString() {
        return "EtherWallet{" + "\n" +
                "mWalletFileName=" + mWalletFileName + "\n" +
                "mAddress=" + mAddress + "\n" +
                "mWalletType=" + mWalletType + "\n" +
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
