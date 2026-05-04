package org.example.cipher.block;

import org.example.utils.BitwiseUtils;
import org.example.utils.PrintUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class AESTest {

    @Test
    void shiftRows() {
        byte[] bytes = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
        AES.shiftRows(bytes);
        Assertions.assertEquals(0, bytes[0]);
        Assertions.assertEquals(5, bytes[1]);
        Assertions.assertEquals(10, bytes[2]);
        Assertions.assertEquals(15, bytes[3]);
    }

    @Test
    void mixColumns() {
        byte[] bytes = new byte[] { 0x0C, (byte) 0x81, 0x31, 0x38, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        AES.mixColumns(bytes);
        Assertions.assertEquals((byte) 0x89, bytes[0]);
        Assertions.assertEquals((byte) 0x7e, bytes[1]);
        Assertions.assertEquals((byte) 0xa7, bytes[2]);
        Assertions.assertEquals((byte) 0xd4, bytes[3]);
    }

    @Test
    void keySchedule128() {
        byte[] key = { 0x2b, 0x7e, 0x15, 0x16, 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6, (byte) 0xab, (byte) 0xf7, 0x15, (byte) 0x88, 0x09, (byte) 0xcf, 0x4f, 0x3c };
        byte[] schedule = AES.keySchedule128(key);
        int[] asInts = BitwiseUtils.toIntArray(schedule);
        int[] expected = { 0xa0fafe17, 0x88542cb1, 0x23a33939, 0x2a6c7605, 0xf2c295f2, 0x7a96b943, 0x5935807a, 0x7359f67f };
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i], asInts[i + 4]);
        }
    }

    @Test
    void keySchedule192() {
        byte[] key = { (byte) 0x8e, 0x73, (byte) 0xb0, (byte) 0xf7, (byte) 0xda, 0x0e, 0x64, 0x52, (byte) 0xc8, 0x10, (byte) 0xf3, 0x2b, (byte) 0x80, (byte) 0x90, 0x79, (byte) 0xe5, 0x62, (byte) 0xf8, (byte) 0xea, (byte) 0xd2, 0x52, 0x2c, 0x6b, 0x7b };
        byte[] schedule = AES.keySchedule192(key);
        int[] asInts = BitwiseUtils.toIntArray(schedule);
        int[] expected = { 0xfe0c91f7, 0x2402f5a5, 0xec12068e, 0x6c827f6b, 0x0e7a95b9, 0x5c56fec2, 0x4db7b4bd, 0x69b54118 };
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i], asInts[i + 6]);
        }
    }

    @Test
    void keySchedule256() {
        byte[] key = { 0x60, 0x3d, (byte) 0xeb, 0x10, 0x15, (byte) 0xca, 0x71, (byte) 0xbe, 0x2b, 0x73, (byte) 0xae, (byte) 0xf0, (byte) 0x85, 0x7d, 0x77, (byte) 0x81, 0x1f, 0x35, 0x2c, 0x07, 0x3b, 0x61, 0x08, (byte) 0xd7, 0x2d, (byte) 0x98, 0x10, (byte) 0xa3, 0x09, 0x14, (byte) 0xdf, (byte) 0xf4};
        byte[] schedule = AES.keySchedule256(key);
        int[] asInts = BitwiseUtils.toIntArray(schedule);
        int[] expected = { 0x9ba35411, 0x8e6925af, 0xa51a8b5f, 0x2067fcde, 0xa8b09c1a, 0x93d194cd, 0xbe49846e, 0xb75d5b9a };
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i], asInts[i + 8]);
        }
    }

    @Test
    void aesBlock() {
        byte[] key = { 0x2b, 0x7e, 0x15, 0x16, 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6, (byte) 0xab, (byte) 0xf7, 0x15, (byte) 0x88, 0x09, (byte) 0xcf, 0x4f, 0x3c };
        byte[] plaintext = { 0x32, 0x43, (byte) 0xf6, (byte) 0xa8, (byte) 0x88, 0x5a, 0x30, (byte) 0x8d, 0x31, 0x31, (byte) 0x98, (byte) 0xa2, (byte) 0xe0, 0x37, 0x07, 0x34 };
        byte[] cipher = AES.aesBlock(key, plaintext);
        byte[] expected = { 0x39, 0x25, (byte) 0x84, 0x1d, 0x02, (byte) 0xdc, 0x09, (byte) 0xfb, (byte) 0xdc, 0x11, (byte) 0x85, (byte) 0x97, 0x19, 0x6a, 0x0b, 0x32 };
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i], cipher[i]);
        }
        byte[] dec = AES.aesBlockDecrypt(key, cipher);
        for (int i = 0; i < plaintext.length; i++) {
            Assertions.assertEquals(plaintext[i], dec[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("provideIvs")
    void testCbcEncryption(byte[] iv) throws Exception {
        byte[] key = { 0x2b, 0x7e, 0x15, 0x16, 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6, (byte) 0xab, (byte) 0xf7, 0x15, (byte) 0x88, 0x09, (byte) 0xcf, 0x4f, 0x3c };
        byte[] plaintext = { 0x32, 0x43, (byte) 0xf6, (byte) 0xa8, (byte) 0x88, 0x5a, 0x30, (byte) 0x8d, 0x31, 0x31, (byte) 0x98, (byte) 0xa2, (byte) 0xe0, 0x37, 0x07, 0x34, 0x32, 0x43, (byte) 0xf6, (byte) 0xa8, (byte) 0x88, 0x5a, 0x30, (byte) 0x8d, 0x31, 0x31, (byte) 0x98, (byte) 0xa2, (byte) 0xe0, 0x37, 0x07, 0x34 };
        long start = System.currentTimeMillis();
        byte[] cipher = AES.encrypt(key, plaintext, iv, CipherMode.CBC);
        long end = System.currentTimeMillis();
        System.out.printf("Time for custom impl: %d ms\n", end - start);
        byte[] expected = encryptStandard(key, plaintext, "CBC/NoPadding", iv);
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i], cipher[i]);
        }
        byte[] dec = AES.decrypt(key, cipher, iv, CipherMode.CBC);
        for (int i = 0; i < plaintext.length; i++) {
            Assertions.assertEquals(plaintext[i], dec[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("provideIvs")
    void testCfbEncryption(byte[] iv) throws Exception {
        byte[] key = { 0x2b, 0x7e, 0x15, 0x16, 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6, (byte) 0xab, (byte) 0xf7, 0x15, (byte) 0x88, 0x09, (byte) 0xcf, 0x4f, 0x3c };
        byte[] plaintext = { 0x32, 0x43, (byte) 0xf6, (byte) 0xa8, (byte) 0x88, 0x5a, 0x30, (byte) 0x8d, 0x31, 0x31, (byte) 0x98, (byte) 0xa2, (byte) 0xe0, 0x37, 0x07, 0x34, 0x32, 0x43, (byte) 0xf6, (byte) 0xa8, (byte) 0x88, 0x5a, 0x30, (byte) 0x8d, 0x31, 0x31, (byte) 0x98, (byte) 0xa2, (byte) 0xe0, 0x37, 0x07, 0x34 };
        long start = System.currentTimeMillis();
        byte[] cipher = AES.encrypt(key, plaintext, iv, CipherMode.CFB);
        long end = System.currentTimeMillis();
        System.out.printf("Time for custom impl: %d ms\n", end - start);
        byte[] expected = encryptStandard(key, plaintext, "CFB/NoPadding", iv);
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i], cipher[i]);
        }
        byte[] dec = AES.decrypt(key, cipher, iv, CipherMode.CFB);
        for (int i = 0; i < plaintext.length; i++) {
            Assertions.assertEquals(plaintext[i], dec[i]);
        }
    }

    private static byte[][] provideIvs() {
        return new byte[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                { (byte) 0x80, 0, 0, 0, 0, 0, 0, 0, (byte) 0x80, 0, 0, 0, 0, 0, 0, 0 },
                { (byte) 0x08, 0, 0, 0, 0, 0, 0, 0, (byte) 0x08, 0, 0, 0, 0, 0, 0, 0 },
                { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF }
        };
    }

    private static byte[] encryptStandard(byte[] key, byte[] plaintext, String options, byte[] iv) throws Exception {
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/" + options);
        if (iv == null) {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        } else {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        }
        long start = System.currentTimeMillis();
        byte[] out = cipher.doFinal(plaintext);
        long end = System.currentTimeMillis();
        System.out.printf("Time for standard impl: %d ms\n", end - start);
        PrintUtils.printBytes(out);
        return out;
    }
}