package org.example.hash;

import org.example.utils.PrintUtils;
import org.example.utils.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class PolynomialRollingHashTest {
    private static List<BigInteger> hashes;
    private static List<String> hashed;
    private static BigInteger p;
    private static BigInteger m;
    private static String lastTestMethodName = "";
    private static BigInteger p2;
    private static BigInteger m2;
    private static final int listSize = 7;


    @BeforeAll
    static void getPrimes() {
        p = RandomUtils.randomPrime(7);
        m = RandomUtils.randomPrime(32);
        p2 = null;
        m2 = null;
        System.out.println("p:" + p);
        System.out.println("m:" + m);
    }

    @BeforeEach
    void prepareTest(TestInfo testInfo) {
        String currentMethodName = testInfo.getTestMethod().get().getName();
        if (!currentMethodName.equals(lastTestMethodName)) {
            hashes = new ArrayList<>();
            hashed = new ArrayList<>();
            if (currentMethodName.equals("hashForTable") || currentMethodName.equals("hashForTableEfficient")) {
                for (int i = 0; i < listSize; i++) {
                    hashed.add(null);
                }
            }
            lastTestMethodName = currentMethodName;
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"banana", "ananas", "ananab", "orange", "barn"})
    void hash(String str) {
        BigInteger hash = PolynomialRollingHash.hash(str, p, m);
        if (hashes.size() > 1) {
            Assertions.assertFalse(hashes.contains(hash));
        }
        hashes.add(hash);
        PrintUtils.printList(hashes);
    }

    @ParameterizedTest
    @ValueSource(strings = {"banana", "ananas", "ananab", "orange", "barn"})
    void hashEf(String str) {
        BigInteger hash = PolynomialRollingHash.hashE(str, p, m);
        if (hashes.size() > 1) {
            Assertions.assertFalse(hashes.contains(hash));
        }
        hashes.add(hash);
        PrintUtils.printList(hashes);
    }

    @ParameterizedTest
    @ValueSource(strings = {"banana", "ananas", "ananab", "orange", "barn"})
    void hashForTable(String str) {
        BigInteger hash = PolynomialRollingHash.hash(str, p, m).mod(BigInteger.valueOf(listSize));
        int count = 0;
        BigInteger step = BigInteger.ZERO;
        BigInteger originalHash = hash;
        while (hashed.get(hash.intValueExact()) != null) {
            count++;
            while (p2 == null || m2 == null || p2.equals(p) || m2.equals(m)) {
                p2 = RandomUtils.randomPrime(7);
                m2 = RandomUtils.randomPrime(32);
            }
            if (step.equals(BigInteger.ZERO)) {
             step = PolynomialRollingHash.hash(str, p2, m2).mod(BigInteger.valueOf(listSize - 1));
            }
            hash = (originalHash.add(step.multiply(BigInteger.valueOf(count)))).mod(BigInteger.valueOf(listSize));
        }
        System.out.printf("Rehashed value %s %d times\n", str, count);
        Assertions.assertFalse(hashed.contains(str));
        hashed.set(hash.intValueExact(), str);
        PrintUtils.printStrList(hashed);
    }

    @ParameterizedTest
    @ValueSource(strings = {"banana", "ananas", "ananab", "orange", "barn"})
    void hashForTableEfficient(String str) {
        BigInteger step = BigInteger.ZERO;
        BigInteger hash = PolynomialRollingHash.hashE(str, p, m).mod(BigInteger.valueOf(listSize));
        int count = 0;
        BigInteger originalHash = hash;
        while (hashed.get(hash.intValueExact()) != null) {
            count++;
            while (p2 == null || m2 == null || p2.equals(p) || m2.equals(m)) {
                p2 = RandomUtils.randomPrime(7);
                m2 = RandomUtils.randomPrime(32);
            }
            if (step.equals(BigInteger.ZERO)) {
                step = PolynomialRollingHash.hash(str, p2, m2).mod(BigInteger.valueOf(listSize - 1));
            }
            hash = (originalHash.add(step.multiply(BigInteger.valueOf(count)))).mod(BigInteger.valueOf(listSize));
        }
        System.out.printf("Rehashed value %s %d times\n", str, count);
        Assertions.assertFalse(hashed.contains(str));
        hashed.set(hash.intValueExact(), str);
        PrintUtils.printStrList(hashed);
    }

    @ParameterizedTest
    @ValueSource(classes = {CharSequence.class})
    void testHash() {
    }
}