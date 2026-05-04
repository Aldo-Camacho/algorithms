package org.example.cipher.block;

import static org.example.utils.BitwiseUtils.getIntBytes;
import static org.example.utils.BitwiseUtils.leftShiftWithNegativeRight;
import static org.example.utils.BitwiseUtils.rightShiftWithNegativeLeft;
import static org.example.utils.BitwiseUtils.toByteArray;
import static org.example.utils.BitwiseUtils.toIntArray;
import static org.example.utils.BitwiseUtils.xorIntArray;

public class DES {
    private static final byte[][] S1 = new byte[][] {
            new byte[] { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
            new byte[] { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
            new byte[] { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
            new byte[] { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
    };
    private static final byte[][] S2 = new byte[][] {
            new byte[] { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
            new byte[] { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
            new byte[] { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
            new byte[] { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
    };
    private static final byte[][] S3 = new byte[][] {
            new byte[] { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
            new byte[] { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
            new byte[] { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
            new byte[] { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
    };
    private static final byte[][] S4 = new byte[][] {
            new byte[] { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
            new byte[] { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
            new byte[] { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
            new byte[] { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }
    };
    private static final byte[][] S5 = new byte[][] {
            new byte[] { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
            new byte[] { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
            new byte[] { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
            new byte[] { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }
    };
    private static final byte[][] S6 = new byte[][] {
            new byte[] { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
            new byte[] { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
            new byte[] { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
            new byte[] { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }
    };
    private static final byte[][] S7 = new byte[][] {
            new byte[] { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
            new byte[] { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
            new byte[] { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
            new byte[] { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }
    };
    private static final byte[][] S8 = new byte[][] {
            new byte[] { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
            new byte[] { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
            new byte[] { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
            new byte[] { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 }
    };

    public static byte[] encrypt(byte[] key, byte[] input, byte[] iv, CipherMode mode) {
        return switch (mode) {
            case ECB -> encryptEcb(key, input);
            case CBC -> encryptCbc(key, iv, input);
            case CFB -> encryptCfb(key, iv, input);
            case OFB -> encryptOfb(key, iv, input);
            case CTR -> encryptCtr(key, iv, input);
        };
    }

    private static byte[] encryptEcb(byte[] key, byte[] input) {
        int[] inputInts = toIntArray(input);
        int[] output = new int[inputInts.length];
        int[] blockInput = new int[2];
        int[] blockOut;
        for (int i = 0; i < inputInts.length; i+=2) {
            System.arraycopy(inputInts, i, blockInput, 0, 2);
            blockOut = desBlock(blockInput, key, false);
            System.arraycopy(blockOut, 0, output, i, 2);
        }
        return toByteArray(output);
    }

    private static byte[] encryptCbc(byte[] key, byte[] iv, byte[] input) {
        int[] inputInts = toIntArray(input);
        int[] output = new int[inputInts.length];
        int[] blockInput = new int[2];
        int[] prevOut = toIntArray(iv);
        for (int i = 0; i < inputInts.length; i+=2) {
            System.arraycopy(inputInts, i, blockInput, 0, 2);
            xorIntArray(blockInput, prevOut);
            prevOut = desBlock(blockInput, key, false);
            System.arraycopy(prevOut, 0, output, i, 2);
        }
        return toByteArray(output);
    }

    private static byte[] encryptCfb(byte[] key, byte[] iv, byte[] input) {
        int[] inputInts = toIntArray(input);
        int[] output = new int[inputInts.length];
        int[] blockInput = new int[2];
        int[] prevOut = toIntArray(iv);
        for (int i = 0; i < inputInts.length; i+=2) {
            System.arraycopy(inputInts, i, blockInput, 0, 2);
            prevOut = desBlock(prevOut, key, false);
            xorIntArray(prevOut, blockInput);
            System.arraycopy(prevOut, 0, output, i, 2);
        }
        return toByteArray(output);
    }

    private static byte[] encryptOfb(byte[] key, byte[] iv, byte[] input) {
        int[] inputInts = toIntArray(input);
        int[] output = new int[inputInts.length];
        int[] blockInput = new int[2];
        int[] prevOut = toIntArray(iv);
        for (int i = 0; i < inputInts.length; i+=2) {
            System.arraycopy(inputInts, i, blockInput, 0, 2);
            prevOut = desBlock(prevOut, key, false);
            xorIntArray(blockInput, prevOut);
            System.arraycopy(blockInput, 0, output, i, 2);
        }
        return toByteArray(output);
    }

    private static byte[] encryptCtr(byte[] key, byte[] iv, byte[] input) {
        int[] inputInts = toIntArray(input);
        int[] output = new int[inputInts.length];
        int[] blockInput = new int[2];
        int[] blockOut;
        int[] counter = toIntArray(iv);
        for (int i = 0; i < inputInts.length; i+=2) {
            System.arraycopy(inputInts, i, blockInput, 0, 2);
            blockOut = desBlock(counter, key, false);
            xorIntArray(blockOut, blockInput);
            System.arraycopy(blockOut, 0, output, i, 2);
            counter[1]++;
        }
        return toByteArray(output);
    }

    public static byte[] decrypt(byte[] key, byte[] input, byte[] iv, CipherMode mode) {
        return switch (mode) {
            case ECB -> decryptEcb(key, input);
            case CBC -> decryptCbc(key, iv, input);
            case CFB -> decryptCfb(key, iv, input);
            case OFB -> encryptOfb(key, iv, input);
            case CTR -> encryptCtr(key, iv, input);
        };
    }

    private static byte[] decryptEcb(byte[] key, byte[] input) {
        int[] inputInts = toIntArray(input);
        int[] output = new int[inputInts.length];
        int[] blockInput = new int[2];
        int[] blockOut;
        for (int i = 0; i < inputInts.length; i+=2) {
            System.arraycopy(inputInts, i, blockInput, 0, 2);
            blockOut = desBlock(blockInput, key, true);
            System.arraycopy(blockOut, 0, output, i, 2);
        }
        return toByteArray(output);
    }

    private static byte[] decryptCbc(byte[] key, byte[] iv, byte[] input) {
        int[] inputInts = toIntArray(input);
        int[] output = new int[inputInts.length];
        int[] blockInput = new int[2];
        int[] blockOut;
        int[] prevOut = toIntArray(iv);
        for (int i = 0; i < inputInts.length; i+=2) {
            System.arraycopy(inputInts, i, blockInput, 0, 2);
            blockOut = desBlock(blockInput, key, true);
            xorIntArray(blockOut, prevOut);
            System.arraycopy(blockOut, 0, output, i, 2);
            System.arraycopy(inputInts, i, prevOut, 0, 2);
        }
        return toByteArray(output);
    }

    private static byte[] decryptCfb(byte[] key, byte[] iv, byte[] input) {
        int[] inputInts = toIntArray(input);
        int[] output = new int[inputInts.length];
        int[] blockInput = new int[2];
        int[] blockOut;
        int[] prevIn = toIntArray(iv);
        for (int i = 0; i < inputInts.length; i+=2) {
            System.arraycopy(inputInts, i, blockInput, 0, 2);
            blockOut = desBlock(prevIn, key, false);
            xorIntArray(blockOut, blockInput);
            System.arraycopy(blockOut, 0, output, i, 2);
            System.arraycopy(inputInts, i, prevIn, 0, 2);
        }
        return toByteArray(output);
    }

    public static int[] desBlock(int[] plaintext, byte[] key, boolean isDecrypt) {
        int[] ciphertext = new int[plaintext.length];
        System.arraycopy(plaintext, 0, ciphertext, 0, plaintext.length);
        initialPermutation(ciphertext);
        byte[] k56bits = keyPerm1(key);
        byte[] keySchedule = getKeySchedule(k56bits, isDecrypt);
        byte[] rkey = new byte[6];
        for (int i = 0; i < 16; i++) {
            System.arraycopy(keySchedule, i * 6, rkey, 0, 6);
            round(ciphertext, rkey);
        }
        int tmp = ciphertext[0];
        ciphertext[0] = ciphertext[1];
        ciphertext[1] = tmp;
        finalPermutation(ciphertext);
        return ciphertext;
    }

    public static void initialPermutation(int[] ciphertext) {
        int l = initialPermutationLeft(ciphertext[0], ciphertext[1]);
        int r = initialPermutationRight(ciphertext[0], ciphertext[1]);
        ciphertext[0] = l;
        ciphertext[1] = r;
    }

    private static int initialPermutationLeft(int l, int r) {
        int b1 = (((r << 25) & 0x80000000) | ((r << 16) & 0x40000000) |
                        ((r << 7) & 0x20000000) | ((r >>> 2) & 0x10000000) |
                        ((l << 21) & 0x08000000) | ((l << 12) & 0x04000000) |
                        ((l << 3) & 0x02000000) | ((l >>> 6) & 0x01000000));
        int b2 = (((r << 27) & 0x80000000) | ((r << 18) & 0x40000000) |
                ((r << 9) & 0x20000000) | (r & 0x10000000) |
                ((l << 23) & 0x08000000) | ((l << 14) & 0x04000000) |
                ((l << 5) & 0x02000000) | ((l >>> 4) & 0x01000000)) >>> 8;
        int b3 = (((r << 29) & 0x80000000) | ((r << 20) & 0x40000000) |
                ((r << 11) & 0x20000000) | ((r << 2) & 0x10000000) |
                ((l << 25) & 0x08000000) | ((l << 16) & 0x04000000) |
                ((l << 7) & 0x02000000) | ((l >>> 2) & 0x01000000)) >>> 16;
        int b4 = (((r << 31) & 0x80000000) | ((r << 22) & 0x40000000) |
                ((r << 13) & 0x20000000) | ((r << 4) & 0x10000000) |
                ((l << 27) & 0x08000000) | ((l << 18) & 0x04000000) |
                ((l << 9) & 0x02000000) | (l & 0x01000000)) >>> 24;
        return b1 | b2 | b3 | b4;
    }

    private static int initialPermutationRight(int l, int r) {
        int b1 = (((r << 24) & 0x80000000) | ((r << 15) & 0x40000000) |
                        ((r << 6) & 0x20000000) | ((r >>> 3) & 0x10000000) |
                        ((l << 20) & 0x08000000) | ((l << 11) & 0x04000000) |
                        ((l << 2) & 0x02000000) | ((l >>> 7) & 0x01000000));
        int b2 = (((r << 26) & 0x80000000) | ((r << 17) & 0x40000000) |
                ((r << 8) & 0x20000000) | ((r >>> 1) & 0x10000000) |
                ((l << 22) & 0x08000000) | ((l << 13) & 0x04000000) |
                ((l << 4) & 0x02000000) | ((l >>> 5) & 0x01000000)) >>> 8;
        int b3 = (((r << 28) & 0x80000000) | ((r << 19) & 0x40000000) |
                ((r << 10) & 0x20000000) | ((r << 1) & 0x10000000) |
                ((l << 24) & 0x08000000) | ((l << 15) & 0x04000000) |
                ((l << 6) & 0x02000000) | ((l >>> 3) & 0x01000000)) >>> 16;
        int b4 = (((r << 30) & 0x80000000) | ((r << 21) & 0x40000000) |
                ((r << 12) & 0x20000000) | ((r << 3) & 0x10000000) |
                ((l << 26) & 0x08000000) | ((l << 17) & 0x04000000) |
                ((l << 8) & 0x02000000) | ((l >>> 1) & 0x01000000)) >>> 24;
        return b1 | b2 | b3 | b4;
    }

    public static void finalPermutation(int[] ciphertext) {
        int l = finalPermutationLeft(ciphertext[0], ciphertext[1]);
        int r = finalPermutationRight(ciphertext[0], ciphertext[1]);
        ciphertext[0] = l;
        ciphertext[1] = r;
    }

    private static int finalPermutationLeft(int l, int r) {
        int b1 = (((r << 7) & 0x80000000) | ((l << 6) & 0x40000000) |
                ((r << 13) & 0x20000000) | ((l << 12) & 0x10000000) |
                ((r << 19) & 0x08000000) | ((l << 18) & 0x04000000) |
                ((r << 25) & 0x02000000) | ((l << 24) & 0x01000000));
        int b2 = (((r << 6) & 0x80000000) | ((l << 5) & 0x40000000) |
                ((r << 12) & 0x20000000) | ((l << 11) & 0x10000000) |
                ((r << 18) & 0x08000000) | ((l << 17) & 0x04000000) |
                ((r << 24) & 0x02000000) | ((l << 23) & 0x01000000)) >>> 8;
        int b3 = (((r << 5) & 0x80000000) | ((l << 4) & 0x40000000) |
                ((r << 11) & 0x20000000) | ((l << 10) & 0x10000000) |
                ((r << 17) & 0x08000000) | ((l << 16) & 0x04000000) |
                ((r << 23) & 0x02000000) | ((l << 22) & 0x01000000)) >>> 16;
        int b4 = (((r << 4) & 0x80000000) | ((l << 3) & 0x40000000) |
                ((r << 10) & 0x20000000) | ((l << 9)& 0x10000000) |
                ((r << 16) & 0x08000000) | ((l << 15) & 0x04000000) |
                ((r << 22) & 0x02000000) | ((l << 21) & 0x01000000)) >>> 24;
        return b1 | b2 | b3 | b4;
    }

    private static int finalPermutationRight(int l, int r) {
        int b1 = (((r << 3) & 0x80000000) | ((l << 2) & 0x40000000) |
                ((r << 9) & 0x20000000) | ((l << 8) & 0x10000000) |
                ((r << 15) & 0x08000000) | ((l << 14) & 0x04000000) |
                ((r << 21) & 0x02000000) | ((l << 20) & 0x01000000));
        int b2 = (((r << 2) & 0x80000000) | ((l << 1) & 0x40000000) |
                ((r << 8) & 0x20000000) | ((l << 7) & 0x10000000) |
                ((r << 14) & 0x08000000) | ((l << 13) & 0x04000000) |
                ((r << 20) & 0x02000000) | ((l << 19) & 0x01000000)) >>> 8;
        int b3 = (((r << 1) & 0x80000000) | ((l) & 0x40000000) |
                ((r << 7) & 0x20000000) | ((l << 6) & 0x10000000) |
                ((r << 13) & 0x08000000) | ((l << 12) & 0x04000000) |
                ((r << 19) & 0x02000000) | ((l << 18) & 0x01000000)) >>> 16;
        int b4 = (((r) & 0x80000000) | ((l >>> 1) & 0x40000000) |
                ((r << 6) & 0x20000000) | ((l << 5)& 0x10000000) |
                ((r << 12) & 0x08000000) | ((l << 11) & 0x04000000) |
                ((r << 18) & 0x02000000) | ((l << 17) & 0x01000000)) >>> 24;
        return b1 | b2 | b3 | b4;
    }

    public static byte[] keyPerm1(byte[] key) {
        byte[] k = new byte[key.length - 1];
        for (int i = 0; i < 8; i++) {
            k[0] |= (byte) ((key[i] & 0x80) >>> 7 - i);
            k[1] |= (byte) rightShiftWithNegativeLeft((key[i] & 0x40), 6 - i);
            k[2] |= (byte) rightShiftWithNegativeLeft((key[i] & 0x20), 5 - i);
            if (i < 4) {
                k[4] |= (byte) leftShiftWithNegativeRight((key[i] & 0x02), 3 + i);
                k[5] |= (byte) leftShiftWithNegativeRight((key[i] & 0x04), 2 + i);
                k[6] |= (byte) leftShiftWithNegativeRight((key[i] & 0x08), i + 1);
                k[6] |= (byte) rightShiftWithNegativeLeft((key[i] & 0x10), 4 - i);
            } else {
                k[3] |= (byte) rightShiftWithNegativeLeft((key[i] & 0x10), 4 - i);
                k[3] |= (byte) rightShiftWithNegativeLeft((key[i] & 0x02), 5 - i);
                k[4] |= (byte) rightShiftWithNegativeLeft((key[i] & 0x04), 6 - i);
                k[5] |= (byte) rightShiftWithNegativeLeft((key[i] & 0x08), 7 - i);
            }
        }
        return k;
    }

    public static byte[] keyPerm2(byte[] key) {
        byte[] k = new byte[key.length - 1];
        k[0] = (byte) (((key[1] & 0x04) << 5) | ((key[2] & 0x80) >>> 1) | ((key[1] & 0x20)) | ((key[2] & 0x01) << 4) |
                        ((key[0] & 0x80) >>> 4) | ((key[0] & 0x08) >>> 1) | ((key[0] & 0x20) >>> 4) | ((key[3] & 0x10) >>> 4));
        k[1] = (byte) (((key[1] & 0x02) << 6) | ((key[0] & 0x04) << 4) | ((key[2] & 0x08) << 2) | ((key[1] & 0x40) >>> 2) |
                        ((key[2] & 0x02) << 2) | ((key[2] & 0x20) >>> 3) | ((key[1] & 0x10) >>> 3) | ((key[0] & 0x10) >>> 4));
        k[2] = (byte) (((key[3] & 0x40) << 1) | ((key[0] & 0x01) << 6) | ((key[1] & 0x01) << 5) | ((key[0] & 0x02) << 3) |
                        ((key[3] & 0x20) >>> 2) | ((key[2] & 0x10) >>> 2) | ((key[1] & 0x08) >>> 2) | ((key[0] & 0x40) >>> 6));
        k[3] = (byte) (((key[5] & 0x80)) | ((key[6] & 0x10) << 2) | ((key[3] & 0x02) << 4) | ((key[4] & 0x08) << 1) |
                        ((key[5] & 0x02) << 2) | ((key[6] & 0x02) << 1) | ((key[3] & 0x04) >>> 1) | ((key[4] & 0x01)));
        k[4] = (byte) (((key[6] & 0x20) << 2) | ((key[5] & 0x08) << 3) | ((key[4] & 0x80) >> 2) | ((key[5] & 0x01) << 4) |
                        ((key[5] & 0x10) >>> 1) | ((key[6] & 0x80) >>> 5) | ((key[4] & 0x02)) | ((key[6] & 0x01)));
        k[5] = (byte) (((key[4] & 0x40) << 1) | ((key[6] & 0x08) << 3) | ((key[5] & 0x04) << 3) | ((key[5] & 0x40) >>> 2) |
                        ((key[6] & 0x40) >>> 3) | ((key[4] & 0x10) >>> 2) | ((key[3] & 0x08) >>> 2) | ((key[3] & 0x01)));
        return k;
    }

    private static byte[] roundKeyPerm(byte[] key, int round) {
        int bitRot = 2;
        if (round == 0 || round == 1 || round == 8 || round == 15) {
            bitRot = 1;
        }
        return leftRotateHalves(key, bitRot);
    }

    private static byte[] leftRotateHalves(byte[] key, int bits) {
        byte[] kr = new byte[key.length] ;
        int lr = 8 - bits;
        int hr = 4 - bits;
        kr[0] = (byte) (((key[0] & 0xFF) << bits) | ((key[1] & 0xFF) >>> lr));
        kr[1] = (byte) (((key[1] & 0xFF) << bits) | ((key[2] & 0xFF) >>> lr));
        kr[2] = (byte) (((key[2] & 0xFF) << bits) | ((key[3] & 0xFF) >>> lr));
        kr[3] = (byte) ((((key[3] & 0xF0) << bits) | (((key[0] & 0xFF) >>> hr)) & 0xF0) | (((key[3] & 0x0F) << bits) & 0x0F) | (((key[4] & 0xFF) >>> lr) & 0x0F));
        kr[4] = (byte) (((key[4] & 0xFF) << bits) | ((key[5] & 0xFF) >>> lr));
        kr[5] = (byte) (((key[5] & 0xFF) << bits) | ((key[6] & 0xFF) >>> lr));
        kr[6] = (byte) (((key[6] & 0xFF) << bits) | ((key[3] & 0x0F) >>> hr));

        return kr;
    }

    private static byte[] getKeySchedule(byte[] key56b, boolean isDecrypt) {
        byte[] kSchedule = new byte[16 * 6];
        for (int i = 0; i < 16; i++) {
            key56b = roundKeyPerm(key56b, i);
            int pos = isDecrypt ? (15 - i) * 6 : i * 6;
            System.arraycopy(keyPerm2(key56b), 0, kSchedule, pos, 6);
        }
        return kSchedule;
    }

    private static void round(int[] ciphertext, byte[] roundKey) {
        ciphertext[0] ^= rFunc(ciphertext[1], roundKey);
        int tmp = ciphertext[0];
        ciphertext[0] = ciphertext[1];
        ciphertext[1] = tmp;
    }

    public static int rFunc(int r, byte[] rKey) {
        byte[] rBytes = getIntBytes(r);
        byte[] e = new byte[rKey.length];
        e[0] = (byte) (((rBytes[3] & 0x01) << 7) | ((rBytes[0] & 0x80) >>> 1) |
                        ((rBytes[0] & 0x40) >>> 1) | ((rBytes[0] & 0x20) >>> 1) |
                        ((rBytes[0] & 0x10) >>> 1) | ((rBytes[0] & 0x08) >>> 1) |
                        ((rBytes[0] & 0x10) >>> 3) | ((rBytes[0] & 0x08) >>> 3));
        e[1] = (byte) (((rBytes[0] & 0x04) << 5) | ((rBytes[0] & 0x02) << 5) |
                        ((rBytes[0] & 0x01) << 5) | ((rBytes[1] & 0x80) >>> 3) |
                        ((rBytes[0] & 0x01) << 3) | ((rBytes[1] & 0x80) >>> 5) |
                        ((rBytes[1] & 0x40) >>> 5) | ((rBytes[1] & 0x20) >>> 5));
        e[2] = (byte) (((rBytes[1] & 0x10) << 3) | ((rBytes[1] & 0x08) << 3) |
                        ((rBytes[1] & 0x10) << 1) | ((rBytes[1] & 0x08) << 1) |
                        ((rBytes[1] & 0x04) << 1) | ((rBytes[1] & 0x02) << 1) |
                        ((rBytes[1] & 0x01) << 1) | ((rBytes[2] & 0x80) >>> 7));
        e[3] = (byte) (((rBytes[1] & 0x01) << 7) | ((rBytes[2] & 0x80) >>> 1) |
                        ((rBytes[2] & 0x40) >>> 1) | ((rBytes[2] & 0x20) >>> 1) |
                        ((rBytes[2] & 0x10) >>> 1) | ((rBytes[2] & 0x08) >>> 1) |
                        ((rBytes[2] & 0x10) >>> 3) | ((rBytes[2] & 0x08) >>> 3));
        e[4] = (byte) (((rBytes[2] & 0x04) << 5) | ((rBytes[2] & 0x02) << 5) |
                        ((rBytes[2] & 0x01) << 5) | ((rBytes[3] & 0x80) >>> 3) |
                        ((rBytes[2] & 0x01) << 3) | ((rBytes[3] & 0x80) >>> 5) |
                        ((rBytes[3] & 0x40) >>> 5) | ((rBytes[3] & 0x20) >>> 5));
        e[5] = (byte) (((rBytes[3] & 0x10) << 3) | ((rBytes[3] & 0x08) << 3) |
                        ((rBytes[3] & 0x10) << 1) | ((rBytes[3] & 0x08) << 1) |
                        ((rBytes[3] & 0x04) << 1) | ((rBytes[3] & 0x02) << 1) |
                        ((rBytes[3] & 0x01) << 1) | ((rBytes[0] & 0x80) >>> 7));

        for (int i = 0; i < e.length; i++) {
            e[i] ^= rKey[i];
        }

        int out = (((S1[((e[0] & 0x80) >>> 6) | ((e[0] & 0x04) >>> 2)][(e[0] & 0x7F) >>> 3] & 0XFF) << 28)) |
                ((S2[(e[0] & 0x02) | ((e[1] & 0x10) >>> 4)][((e[0] & 0x01) << 3) | ((e[1] & 0xE0) >>> 5)] & 0xFF) << 24) |
                ((S3[((e[1] & 0x08) >>> 2) | ((e[2] & 0x40) >>> 6)][((e[1] & 0x07) << 1) | ((e[2] & 0x80) >>> 7)] & 0xFF) << 20) |
                ((S4[((e[2] & 0x20) >>> 4) | (e[2] & 0x1)][((e[2] & 0x1E) >>> 1)] & 0xFF) << 16) |
                (((S5[((e[3] & 0x80) >>> 6) | ((e[3] & 0x04) >>> 2)][(e[3] & 0x7F) >>> 3] & 0XFF) << 12)) |
                ((S6[(e[3] & 0x02) | ((e[4] & 0x10) >>> 4)][((e[3] & 0x01) << 3) | ((e[4] & 0xE0) >>> 5)] & 0xFF) << 8) |
                ((S7[((e[4] & 0x08) >>> 2) | ((e[5] & 0x40) >>> 6)][((e[4] & 0x07) << 1) | ((e[5] & 0x80) >>> 7)] & 0xFF) << 4) |
                ((S8[((e[5] & 0x20) >>> 4) | (e[5] & 0x1)][((e[5] & 0x1E) >>> 1)] & 0xFF));

        return pPermutation(out);
    }

    private static int pPermutation(int i) {
        return ((i << 15) & 0x80000000) | ((i << 5) & 0x40000000) |
                ((i << 17) & 0x20000000) | ((i << 17) & 0x10000000) |
                ((i << 24) & 0x08000000) | ((i << 6) & 0x04000000) |
                ((i << 21) & 0x02000000) | ((i << 9) & 0x01000000) |
                ((i >>> 8) & 0x00800000) | ((i << 5) & 0x00400000) |
                ((i << 12) & 0x00200000) | ((i << 14) & 0x00100000) |
                ((i >>> 8) & 0x00080000) | ((i << 4) & 0x00040000) |
                ((i << 16) & 0x00020000) | ((i >>> 6) & 0x00010000) |
                ((i >>> 15) & 0x00008000) | ((i >>> 10) & 0x00004000) |
                ((i << 5) & 0x00002000) | ((i >>> 6) & 0x00001000) |
                ((i << 11) & 0x00000800) | ((i << 5) & 0x00000400) |
                ((i >>> 20) & 0x00000200) | ((i >>> 15) & 0x00000100) |
                ((i >>> 6) & 0x00000080) | ((i >>> 13) & 0x00000040) |
                ((i << 3) & 0x00000020) | ((i >>> 22) & 0x00000010) |
                ((i >>> 7) & 0x00000008) | ((i >>> 19) & 0x00000004) |
                ((i >>> 27) & 0x00000002) | ((i >>> 7) & 0x00000001);
    }
}
