package org.example.hash;

import org.example.utils.PrintUtils;
import org.example.utils.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

class PolynomialRollingHashTest {
    private static List<Long> hashes;
    private static long p;
    private static long m;

    @BeforeAll
    static void getPrimes() {
        p = RandomUtils.randomPrimeLong();
        m = RandomUtils.randomPrimeLong();
        System.out.println("p:" + p);
        System.out.println("m:" + m);
    }

    @BeforeEach
    void prepareTest() {
        hashes = new ArrayList<>();
    }

    @ParameterizedTest
    @ValueSource(strings = {"banana", "ananas", "ananab", "orange", "barn"})
    void hash(String str) {
        long hash = PolynomialRollingHash.hash(str, p, m);
        hashes.add(hash);
        PrintUtils.printList(hashes);
        if (hashes.size() > 1) {
            Assertions.assertFalse(hashes.contains(hash));
        }
    }

    @ParameterizedTest
    @ValueSource(classes = {CharSequence.class})
    void testHash() {
    }
}