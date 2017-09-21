package com.kora.android.data.web3j.model;

import java.io.Serializable;

public class EtherWallet implements Serializable {

    private static final long serialVersionUID = 2622313531196422839L;
    private static final String ADDRESS_PREFIX = "0x";

    private String walletFileName;
    private String address;

    public EtherWallet(String walletFileName) {
        this.walletFileName = walletFileName;
        this.address = ADDRESS_PREFIX + walletFileName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWalletFileName() {
        return walletFileName;
    }

    public void setWalletFileName(String walletFileName) {
        this.walletFileName = walletFileName;
    }

    @Override
    public String toString() {
        return "EtherWallet{" + "\n" +
                "walletFileName='" + walletFileName + "\n" +
                "address='" + address + "\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtherWallet that = (EtherWallet) o;
        return walletFileName.equals(that.walletFileName);
    }

    @Override
    public int hashCode() {
        return walletFileName.hashCode();
    }
}
