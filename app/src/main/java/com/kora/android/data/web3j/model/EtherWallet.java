package com.kora.android.data.web3j.model;

import java.io.Serializable;

public class EtherWallet implements Serializable {

    private static final long serialVersionUID = 2622313531196422839L;
    private static final String ADDRESS_PREFIX = "0x";

    private String publicKey;
    private String address;

    public EtherWallet(String publicKey) {
        this.publicKey = publicKey;
        this.address = ADDRESS_PREFIX + publicKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "EtherWallet{" + "\n" +
                "publicKey='" + publicKey + "\n" +
                "address='" + address + "\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EtherWallet that = (EtherWallet) o;

        return publicKey.equals(that.publicKey);

    }

    @Override
    public int hashCode() {
        return publicKey.hashCode();
    }
}
