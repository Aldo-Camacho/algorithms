package org.example.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RandomUtils {
    private final static Random random = new Random();
    private final static SecureRandom secRandom = new SecureRandom();

    public static Random get() {
        return random;
    }

    public static long randomPrimeLong() {
        return BigInteger.probablePrime(63, secRandom).longValueExact();
    }
}
