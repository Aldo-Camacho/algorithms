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

    public static BigInteger randomPrime(int bits) {
        return BigInteger.probablePrime(bits - 1, secRandom);
    }
}
