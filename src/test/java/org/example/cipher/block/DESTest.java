package org.example.cipher.block;

import org.example.utils.BitwiseUtils;
import org.example.utils.PrintUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static org.example.utils.BitwiseUtils.bytesToLong;
import static org.example.utils.BitwiseUtils.toByteArray;
import static org.junit.jupiter.api.Assertions.*;

class DESTest {
    private static final byte[] key = new byte[]{ 0x13, 0x34, 0x57, 0x79, (byte) 0x9B, (byte) 0xBC, (byte) 0xDF, (byte) 0xF1};

    @Test
    void testEncryptionECB() throws Exception {
        byte[] plaintext = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
        long start = System.currentTimeMillis();
        byte[] ciphertext = DES.encrypt(key, plaintext, null, CipherMode.ECB);
        long end = System.currentTimeMillis();
        System.out.printf("Time for custom impl: %d ms\n", end - start);
        PrintUtils.printBytes(ciphertext);
        byte[] standard = encryptStandard(plaintext, "ECB/NoPadding", null);
        Assertions.assertEquals(plaintext.length, ciphertext.length);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(standard[i], ciphertext[i]);
        }
        byte[] decrypted = DES.decrypt(key, ciphertext, null, CipherMode.ECB);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(plaintext[i], decrypted[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("provideIvs")
    void testEncryptionCBC(byte[] iv) throws Exception {
        byte[] plaintext = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
        long start = System.currentTimeMillis();
        byte[] ciphertext = DES.encrypt(key, plaintext, iv, CipherMode.CBC);
        long end = System.currentTimeMillis();
        System.out.printf("Time for custom impl: %d ms\n", end - start);
        PrintUtils.printBytes(ciphertext);
        byte[] standard = encryptStandard(plaintext, "CBC/NoPadding", iv);
        Assertions.assertEquals(plaintext.length, ciphertext.length);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(standard[i], ciphertext[i]);
        }
        byte[] dec = DES.decrypt(key, ciphertext, iv, CipherMode.CBC);
        for (int i = 0; i < dec.length; i++) {
            Assertions.assertEquals(plaintext[i], dec[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("provideIvs")
    void testEncryptionCFB(byte[] iv) throws Exception {
        byte[] plaintext = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
        long start = System.currentTimeMillis();
        byte[] ciphertext = DES.encrypt(key, plaintext, iv, CipherMode.CFB);
        long end = System.currentTimeMillis();
        System.out.printf("Time for custom impl: %d ms\n", end - start);
        PrintUtils.printBytes(ciphertext);
        byte[] standard = encryptStandard(plaintext, "CFB/NoPadding", iv);
        Assertions.assertEquals(plaintext.length, ciphertext.length);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(standard[i], ciphertext[i]);
        }
        byte[] dec = DES.decrypt(key, ciphertext, iv, CipherMode.CFB);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(plaintext[i], dec[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("provideIvs")
    void testEncryptionOFB(byte[] iv) throws Exception {
        byte[] plaintext = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
        long start = System.currentTimeMillis();
        byte[] ciphertext = DES.encrypt(key, plaintext, iv, CipherMode.OFB);
        long end = System.currentTimeMillis();
        System.out.printf("Time for custom impl: %d ms\n", end - start);
        PrintUtils.printBytes(ciphertext);
        byte[] standard = encryptStandard(plaintext, "OFB/NoPadding", iv);
        Assertions.assertEquals(plaintext.length, ciphertext.length);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(standard[i], ciphertext[i]);
        }
        byte[] dec = DES.decrypt(key, ciphertext, iv, CipherMode.OFB);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(plaintext[i], dec[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("provideIvs")
    void testEncryptionCTR(byte[] iv) throws Exception {
        byte[] plaintext = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
        long start = System.currentTimeMillis();
        byte[] ciphertext = DES.encrypt(key, plaintext, iv, CipherMode.CTR);
        long end = System.currentTimeMillis();
        System.out.printf("Time for custom impl: %d ms\n", end - start);
        PrintUtils.printBytes(ciphertext);
        byte[] standard = encryptStandard(plaintext, "CTR/NoPadding", iv);
        Assertions.assertEquals(plaintext.length, ciphertext.length);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(standard[i], ciphertext[i]);
        }
        byte[] dec = DES.decrypt(key, ciphertext, iv, CipherMode.CTR);
        for (int i = 0; i < ciphertext.length; i++) {
            Assertions.assertEquals(plaintext[i], dec[i]);
        }
    }

    @Test
    void initialPermutation() {
        int[] input = new int[] { 0x01234567, 0x89ABCDEF };
        DES.initialPermutation(input);
        Assertions.assertEquals(0xcc00ccff, input[0]);
        Assertions.assertEquals(0xf0aaf0aa, input[1]);
        PrintUtils.printBytes(BitwiseUtils.toByteArray(input));
        input = new int[] { 0x55555555, 0x55555555 };
        DES.initialPermutation(input);
        Assertions.assertEquals(0xffffffff, input[0]);
        Assertions.assertEquals(0x00000000, input[1]);
        PrintUtils.printBytes(BitwiseUtils.toByteArray(input));
        input = new int[] { 0xAAAAAAAA, 0xAAAAAAAA };
        DES.initialPermutation(input);
        Assertions.assertEquals(0x00000000, input[0]);
        Assertions.assertEquals(0xffffffff, input[1]);
        PrintUtils.printBytes(BitwiseUtils.toByteArray(input));
    }

    @Test
    void finalPermutation() {
        int[] input = new int[] { 0xcc00ccff, 0xf0aaf0aa };
        DES.finalPermutation(input);
        Assertions.assertEquals(0x01234567, input[0]);
        Assertions.assertEquals(0x89ABCDEF, input[1]);
        PrintUtils.printBytes(BitwiseUtils.toByteArray(input));
        input = new int[] { 0xffffffff, 0x00000000 };
        DES.finalPermutation(input);
        Assertions.assertEquals(0x55555555, input[0]);
        Assertions.assertEquals(0x55555555, input[1]);
        PrintUtils.printBytes(BitwiseUtils.toByteArray(input));
        input = new int[] { 0x00000000, 0xffffffff};
        DES.finalPermutation(input);
        Assertions.assertEquals(0xAAAAAAAA, input[0]);
        Assertions.assertEquals(0xAAAAAAAA, input[1]);
        PrintUtils.printBytes(BitwiseUtils.toByteArray(input));
    }

    @Test
    void rFunc() {
        int r = 0xf0aaf0aa;
        byte[] k1 = new byte[]{ 0x1B, 0x02, (byte) 0xEF, (byte) 0xFC, 0x05, (byte) 0xDB};
        int rfunc = DES.rFunc(r, k1);
        Assertions.assertEquals(591957403, rfunc);
    }

    @Test
    void keyPerm1() {
        byte[] reduced = DES.keyPerm1(key);
        Assertions.assertEquals(0xf0ccaaf556678fL, bytesToLong(reduced));
        PrintUtils.printBytes(reduced);
    }

    @Test
    void desBlock() throws Exception {
        int[] plaintext = { 0x01234567, 0x89ABCDEF };
        int[] ciphertext = DES.desBlock(plaintext, key, false);
        byte[] cipher = toByteArray(ciphertext);
        byte[] standard = encryptStandard(toByteArray(plaintext), "ECB/NoPadding", null);
        for (int i = 0; i < cipher.length; i++) {
            Assertions.assertEquals(cipher[i], standard[i]);
        }
        PrintUtils.printBytes(cipher);
    }

    private static byte[][] provideIvs() {
        return new byte[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 1 },
                {(byte) 0x80, 0, 0, 0, 0, 0, 0, 0 },
                {(byte) 0x08, 0, 0, 0, 0, 0, 0, 0 },
                { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF }
        };
    }

    private static byte[] encryptStandard(byte[] plaintext, String options, byte[] iv) throws Exception {
        SecretKey keySpec = new SecretKeySpec(key, "DES");
        Cipher cipher = Cipher.getInstance("DES/" + options);
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