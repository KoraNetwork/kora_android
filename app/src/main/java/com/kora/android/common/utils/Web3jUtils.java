package com.kora.android.common.utils;

import com.kora.android.presentation.model.UserEntity;

import org.web3j.abi.datatypes.Address;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.kora.android.common.Keys.JSON_FILE_EXTENSION;

public class Web3jUtils {

    public static String getKeystoreFileNameFromAddress(final String address) {
        return address + JSON_FILE_EXTENSION;
    }

    public static String getAddressFromKeystoreFileName(final String keystoreFileName) {
        return keystoreFileName.substring(0, keystoreFileName.length() - JSON_FILE_EXTENSION.length());
    }

    public static double convertWeiToEth(final BigInteger bigInteger) {
        return bigInteger.divide(new BigInteger("10000000000000000")).longValue() / 100d;
    }

    public static BigInteger convertEthToWei(final double d) {
        return BigInteger.valueOf(Math.round(d * 100)).multiply(new BigInteger("10000000000000000"));
    }

    public static double convertBigIntegerToToken(final BigInteger bigInteger) {
        return bigInteger.longValue() / 100d;
    }

    public static BigInteger convertTokenToBigInteger(final double d) {
        return BigInteger.valueOf(Math.round(d * 100));
    }

    public static String convertDoubleToString(final double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static long convertDateToSeconds(final Date date) {
        return date.getTime() / 1000;
    }

    public static List<Address> getAddressesFromUserEntities(final List<UserEntity> userEntityList) {
        final List<Address> addressList = new ArrayList<>();
        for (int i = 0; i < userEntityList.size(); i++)
            addressList.add(new Address(userEntityList.get(i).getIdentity()));
        return addressList;
    }

    public static BigInteger convertRateToBigInteger(final double d) {
        return BigInteger.valueOf(Math.round(d * 100));
    }

    public static BigInteger increaseTransactionCount(final BigInteger transactionCount) {
        return transactionCount.add(BigInteger.ONE);
    }
}
