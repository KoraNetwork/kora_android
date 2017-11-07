package com.kora.android.common.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;

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
        return BigInteger.valueOf((long) (d*100)).multiply(new BigInteger("10000000000000000"));
    }

    public static double convertBigIntegerToToken(final BigInteger bigInteger) {
        return bigInteger.longValue() / 100d;
    }

    public static BigInteger convertTokenToBigInteger(final double d) {
        return BigInteger.valueOf((long) (d * 100));
    }

    public static String convertDoubleToString(final double d) {
        return new DecimalFormat("#.##").format(d);
    }
}
