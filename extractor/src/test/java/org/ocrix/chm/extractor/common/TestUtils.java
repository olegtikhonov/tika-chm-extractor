package org.ocrix.chm.extractor.common;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class TestUtils {
    private static final SecureRandom random = new SecureRandom();

    private TestUtils() {}

    public static String nextString() {
        return new BigInteger(130, random).toString(32);
    }
}
