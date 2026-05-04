package org.example.cipher.block;

import org.example.utils.BitwiseUtils;

import static org.example.utils.BitwiseUtils.toByteArray;
import static org.example.utils.BitwiseUtils.toIntArray;
import static org.example.utils.BitwiseUtils.xorIntArray;

public class AES {
    private static final byte[] S_BOX = new byte[] {
            (byte) 0x63, (byte) 0x7c, (byte) 0x77, (byte) 0x7b, (byte) 0xf2, (byte) 0x6b, (byte) 0x6f, (byte) 0xc5, (byte) 0x30, (byte) 0x01, (byte) 0x67, (byte) 0x2b, (byte) 0xfe, (byte) 0xd7, (byte) 0xab, (byte) 0x76,
            (byte) 0xca, (byte) 0x82, (byte) 0xc9, (byte) 0x7d, (byte) 0xfa, (byte) 0x59, (byte) 0x47, (byte) 0xf0, (byte) 0xad, (byte) 0xd4, (byte) 0xa2, (byte) 0xaf, (byte) 0x9c, (byte) 0xa4, (byte) 0x72, (byte) 0xc0,
            (byte) 0xb7, (byte) 0xfd, (byte) 0x93, (byte) 0x26, (byte) 0x36, (byte) 0x3f, (byte) 0xf7, (byte) 0xcc, (byte) 0x34, (byte) 0xa5, (byte) 0xe5, (byte) 0xf1, (byte) 0x71, (byte) 0xd8, (byte) 0x31, (byte) 0x15,
            (byte) 0x04, (byte) 0xc7, (byte) 0x23, (byte) 0xc3, (byte) 0x18, (byte) 0x96, (byte) 0x05, (byte) 0x9a, (byte) 0x07, (byte) 0x12, (byte) 0x80, (byte) 0xe2, (byte) 0xeb, (byte) 0x27, (byte) 0xb2, (byte) 0x75,
            (byte) 0x09, (byte) 0x83, (byte) 0x2c, (byte) 0x1a, (byte) 0x1b, (byte) 0x6e, (byte) 0x5a, (byte) 0xa0, (byte) 0x52, (byte) 0x3b, (byte) 0xd6, (byte) 0xb3, (byte) 0x29, (byte) 0xe3, (byte) 0x2f, (byte) 0x84,
            (byte) 0x53, (byte) 0xd1, (byte) 0x00, (byte) 0xed, (byte) 0x20, (byte) 0xfc, (byte) 0xb1, (byte) 0x5b, (byte) 0x6a, (byte) 0xcb, (byte) 0xbe, (byte) 0x39, (byte) 0x4a, (byte) 0x4c, (byte) 0x58, (byte) 0xcf,
            (byte) 0xd0, (byte) 0xef, (byte) 0xaa, (byte) 0xfb, (byte) 0x43, (byte) 0x4d, (byte) 0x33, (byte) 0x85, (byte) 0x45, (byte) 0xf9, (byte) 0x02, (byte) 0x7f, (byte) 0x50, (byte) 0x3c, (byte) 0x9f, (byte) 0xa8,
            (byte) 0x51, (byte) 0xa3, (byte) 0x40, (byte) 0x8f, (byte) 0x92, (byte) 0x9d, (byte) 0x38, (byte) 0xf5, (byte) 0xbc, (byte) 0xb6, (byte) 0xda, (byte) 0x21, (byte) 0x10, (byte) 0xff, (byte) 0xf3, (byte) 0xd2,
            (byte) 0xcd, (byte) 0x0c, (byte) 0x13, (byte) 0xec, (byte) 0x5f, (byte) 0x97, (byte) 0x44, (byte) 0x17, (byte) 0xc4, (byte) 0xa7, (byte) 0x7e, (byte) 0x3d, (byte) 0x64, (byte) 0x5d, (byte) 0x19, (byte) 0x73,
            (byte) 0x60, (byte) 0x81, (byte) 0x4f, (byte) 0xdc, (byte) 0x22, (byte) 0x2a, (byte) 0x90, (byte) 0x88, (byte) 0x46, (byte) 0xee, (byte) 0xb8, (byte) 0x14, (byte) 0xde, (byte) 0x5e, (byte) 0x0b, (byte) 0xdb,
            (byte) 0xe0, (byte) 0x32, (byte) 0x3a, (byte) 0x0a, (byte) 0x49, (byte) 0x06, (byte) 0x24, (byte) 0x5c, (byte) 0xc2, (byte) 0xd3, (byte) 0xac, (byte) 0x62, (byte) 0x91, (byte) 0x95, (byte) 0xe4, (byte) 0x79,
            (byte) 0xe7, (byte) 0xc8, (byte) 0x37, (byte) 0x6d, (byte) 0x8d, (byte) 0xd5, (byte) 0x4e, (byte) 0xa9, (byte) 0x6c, (byte) 0x56, (byte) 0xf4, (byte) 0xea, (byte) 0x65, (byte) 0x7a, (byte) 0xae, (byte) 0x08,
            (byte) 0xba, (byte) 0x78, (byte) 0x25, (byte) 0x2e, (byte) 0x1c, (byte) 0xa6, (byte) 0xb4, (byte) 0xc6, (byte) 0xe8, (byte) 0xdd, (byte) 0x74, (byte) 0x1f, (byte) 0x4b, (byte) 0xbd, (byte) 0x8b, (byte) 0x8a,
            (byte) 0x70, (byte) 0x3e, (byte) 0xb5, (byte) 0x66, (byte) 0x48, (byte) 0x03, (byte) 0xf6, (byte) 0x0e, (byte) 0x61, (byte) 0x35, (byte) 0x57, (byte) 0xb9, (byte) 0x86, (byte) 0xc1, (byte) 0x1d, (byte) 0x9e,
            (byte) 0xe1, (byte) 0xf8, (byte) 0x98, (byte) 0x11, (byte) 0x69, (byte) 0xd9, (byte) 0x8e, (byte) 0x94, (byte) 0x9b, (byte) 0x1e, (byte) 0x87, (byte) 0xe9, (byte) 0xce, (byte) 0x55, (byte) 0x28, (byte) 0xdf,
            (byte) 0x8c, (byte) 0xa1, (byte) 0x89, (byte) 0x0d, (byte) 0xbf, (byte) 0xe6, (byte) 0x42, (byte) 0x68, (byte) 0x41, (byte) 0x99, (byte) 0x2d, (byte) 0x0f, (byte) 0xb0, (byte) 0x54, (byte) 0xbb, (byte) 0x16,
    };

    private static final byte[] S_BOX_INV = new byte[] {
            (byte) 0x52, (byte) 0x09, (byte) 0x6a, (byte) 0xd5, (byte) 0x30, (byte) 0x36, (byte) 0xa5, (byte) 0x38, (byte) 0xbf, (byte) 0x40, (byte) 0xa3, (byte) 0x9e, (byte) 0x81, (byte) 0xf3, (byte) 0xd7, (byte) 0xfb,
            (byte) 0x7c, (byte) 0xe3, (byte) 0x39, (byte) 0x82, (byte) 0x9b, (byte) 0x2f, (byte) 0xff, (byte) 0x87, (byte) 0x34, (byte) 0x8e, (byte) 0x43, (byte) 0x44, (byte) 0xc4, (byte) 0xde, (byte) 0xe9, (byte) 0xcb,
            (byte) 0x54, (byte) 0x7b, (byte) 0x94, (byte) 0x32, (byte) 0xa6, (byte) 0xc2, (byte) 0x23, (byte) 0x3d, (byte) 0xee, (byte) 0x4c, (byte) 0x95, (byte) 0x0b, (byte) 0x42, (byte) 0xfa, (byte) 0xc3, (byte) 0x4e,
            (byte) 0x08, (byte) 0x2e, (byte) 0xa1, (byte) 0x66, (byte) 0x28, (byte) 0xd9, (byte) 0x24, (byte) 0xb2, (byte) 0x76, (byte) 0x5b, (byte) 0xa2, (byte) 0x49, (byte) 0x6d, (byte) 0x8b, (byte) 0xd1, (byte) 0x25,
            (byte) 0x72, (byte) 0xf8, (byte) 0xf6, (byte) 0x64, (byte) 0x86, (byte) 0x68, (byte) 0x98, (byte) 0x16, (byte) 0xd4, (byte) 0xa4, (byte) 0x5c, (byte) 0xcc, (byte) 0x5d, (byte) 0x65, (byte) 0xb6, (byte) 0x92,
            (byte) 0x6c, (byte) 0x70, (byte) 0x48, (byte) 0x50, (byte) 0xfd, (byte) 0xed, (byte) 0xb9, (byte) 0xda, (byte) 0x5e, (byte) 0x15, (byte) 0x46, (byte) 0x57, (byte) 0xa7, (byte) 0x8d, (byte) 0x9d, (byte) 0x84,
            (byte) 0x90, (byte) 0xd8, (byte) 0xab, (byte) 0x00, (byte) 0x8c, (byte) 0xbc, (byte) 0xd3, (byte) 0x0a, (byte) 0xf7, (byte) 0xe4, (byte) 0x58, (byte) 0x05, (byte) 0xb8, (byte) 0xb3, (byte) 0x45, (byte) 0x06,
            (byte) 0xd0, (byte) 0x2c, (byte) 0x1e, (byte) 0x8f, (byte) 0xca, (byte) 0x3f, (byte) 0x0f, (byte) 0x02, (byte) 0xc1, (byte) 0xaf, (byte) 0xbd, (byte) 0x03, (byte) 0x01, (byte) 0x13, (byte) 0x8a, (byte) 0x6b,
            (byte) 0x3a, (byte) 0x91, (byte) 0x11, (byte) 0x41, (byte) 0x4f, (byte) 0x67, (byte) 0xdc, (byte) 0xea, (byte) 0x97, (byte) 0xf2, (byte) 0xcf, (byte) 0xce, (byte) 0xf0, (byte) 0xb4, (byte) 0xe6, (byte) 0x73,
            (byte) 0x96, (byte) 0xac, (byte) 0x74, (byte) 0x22, (byte) 0xe7, (byte) 0xad, (byte) 0x35, (byte) 0x85, (byte) 0xe2, (byte) 0xf9, (byte) 0x37, (byte) 0xe8, (byte) 0x1c, (byte) 0x75, (byte) 0xdf, (byte) 0x6e,
            (byte) 0x47, (byte) 0xf1, (byte) 0x1a, (byte) 0x71, (byte) 0x1d, (byte) 0x29, (byte) 0xc5, (byte) 0x89, (byte) 0x6f, (byte) 0xb7, (byte) 0x62, (byte) 0x0e, (byte) 0xaa, (byte) 0x18, (byte) 0xbe, (byte) 0x1b,
            (byte) 0xfc, (byte) 0x56, (byte) 0x3e, (byte) 0x4b, (byte) 0xc6, (byte) 0xd2, (byte) 0x79, (byte) 0x20, (byte) 0x9a, (byte) 0xdb, (byte) 0xc0, (byte) 0xfe, (byte) 0x78, (byte) 0xcd, (byte) 0x5a, (byte) 0xf4,
            (byte) 0x1f, (byte) 0xdd, (byte) 0xa8, (byte) 0x33, (byte) 0x88, (byte) 0x07, (byte) 0xc7, (byte) 0x31, (byte) 0xb1, (byte) 0x12, (byte) 0x10, (byte) 0x59, (byte) 0x27, (byte) 0x80, (byte) 0xec, (byte) 0x5f,
            (byte) 0x60, (byte) 0x51, (byte) 0x7f, (byte) 0xa9, (byte) 0x19, (byte) 0xb5, (byte) 0x4a, (byte) 0x0d, (byte) 0x2d, (byte) 0xe5, (byte) 0x7a, (byte) 0x9f, (byte) 0x93, (byte) 0xc9, (byte) 0x9c, (byte) 0xef,
            (byte) 0xa0, (byte) 0xe0, (byte) 0x3b, (byte) 0x4d, (byte) 0xae, (byte) 0x2a, (byte) 0xf5, (byte) 0xb0, (byte) 0xc8, (byte) 0xeb, (byte) 0xbb, (byte) 0x3c, (byte) 0x83, (byte) 0x53, (byte) 0x99, (byte) 0x61,
            (byte) 0x17, (byte) 0x2b, (byte) 0x04, (byte) 0x7e, (byte) 0xba, (byte) 0x77, (byte) 0xd6, (byte) 0x26, (byte) 0xe1, (byte) 0x69, (byte) 0x14, (byte) 0x63, (byte) 0x55, (byte) 0x21, (byte) 0x0c, (byte) 0x7d,
    };

    private static final byte MOD_P = (byte) 0b00011011;
    private static final int[] RC = {
            0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000, 0x20000000, 0x40000000, 0x80000000, 0x1b000000, 0x36000000
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
        byte[] blockInput = new byte[16];
        byte[] blockOut;
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i += 16) {
            System.arraycopy(blockInput, i, blockInput, 0, 16);
            blockOut = aesBlock(key, blockInput);
            System.arraycopy(blockOut, 0, output, i, 16);
        }
        return output;
    }

    private static byte[] encryptCbc(byte[] key, byte[] iv, byte[] input) {
        byte[] output = new byte[input.length];
        byte[] blockInput = new byte[16];
        byte[] prevOut = iv;
        for (int i = 0; i < input.length; i+=16) {
            System.arraycopy(input, i, blockInput, 0, 16);
            BitwiseUtils.xor(blockInput, prevOut);
            prevOut = aesBlock(key, blockInput);
            System.arraycopy(prevOut, 0, output, i, 16);
        }
        return output;
    }

    private static byte[] encryptCfb(byte[] key, byte[] iv, byte[] input) {
        byte[] output = new byte[input.length];
        byte[] blockInput = new byte[16];
        byte[] prevOut = iv;
        for (int i = 0; i < input.length; i+=16) {
            System.arraycopy(input, i, blockInput, 0, 16);
            prevOut = aesBlock(key, prevOut);
            BitwiseUtils.xor(prevOut, blockInput);
            System.arraycopy(prevOut, 0, output, i, 16);
        }
        return output;
    }

    private static byte[] encryptOfb(byte[] key, byte[] iv, byte[] input) {
        byte[] output = new byte[input.length];
        byte[] blockInput = new byte[16];
        byte[] prevOut = iv;
        for (int i = 0; i < input.length; i+=16) {
            System.arraycopy(input, i, blockInput, 0, 16);
            prevOut = aesBlock(key, prevOut);
            BitwiseUtils.xor(blockInput, prevOut);
            System.arraycopy(blockInput, 0, output, i, 16);
        }
        return output;
    }

    private static byte[] encryptCtr(byte[] key, byte[] iv, byte[] input) {
        byte[] output = new byte[input.length];
        byte[] blockInput = new byte[16];
        byte[] blockOut;
        for (int i = 0; i < input.length; i+=16) {
            System.arraycopy(input, i, blockInput, 0, 16);
            blockOut = aesBlock(key, iv);
            BitwiseUtils.xor(blockOut, blockInput);
            System.arraycopy(blockOut, 0, output, i, 2);
            BitwiseUtils.incrementOne(iv);
        }
        return output;
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
        byte[] blockInput = new byte[16];
        byte[] blockOut;
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i+=16) {
            System.arraycopy(input, i, blockInput, 0, 16);
            blockOut = aesBlockDecrypt(key, blockInput);
            System.arraycopy(blockOut, 0, output, i, 16);
        }
        return output;
    }

    private static byte[] decryptCbc(byte[] key, byte[] iv, byte[] input) {
        byte[] output = new byte[input.length];
        byte[] blockInput = new byte[16];
        byte[] blockOut;
        for (int i = 0; i < input.length; i+=16) {
            System.arraycopy(input, i, blockInput, 0, 16);
            blockOut = aesBlockDecrypt(key, blockInput);
            BitwiseUtils.xor(blockOut, iv);
            System.arraycopy(blockOut, 0, output, i, 16);
            System.arraycopy(input, i, iv, 0, 16);
        }
        return output;
    }

    private static byte[] decryptCfb(byte[] key, byte[] iv, byte[] input) {
        byte[] output = new byte[input.length];
        byte[] blockInput = new byte[16];
        byte[] blockOut;
        for (int i = 0; i < input.length; i+=16) {
            System.arraycopy(input, i, blockInput, 0, 16);
            blockOut = aesBlock(key, iv);
            BitwiseUtils.xor(blockOut, blockInput);
            System.arraycopy(blockOut, 0, output, i, 16);
            System.arraycopy(input, i, iv, 0, 16);
        }
        return output;
    }

    public static byte[] aesBlock(byte[] key, byte[] in) {
        assert key.length == 16 || key.length == 24 || key.length == 32;
        assert in.length == 16;
        int nRounds = key.length == 16 ? 10 : key.length == 24 ? 12 : 14;
        byte[] cipher = new byte[in.length];
        System.arraycopy(in, 0, cipher, 0, in.length);
        byte[] keys = key.length == 16 ? keySchedule128(key) : key.length == 24 ? keySchedule192(key) : keySchedule256(key);
        byte[] rKey = new byte[16];
        System.arraycopy(keys, 0, rKey, 0, 16);
        keyAddition(cipher, rKey);
        for (int i = 0; i < nRounds; i++) {
            for (int j = 0; j < cipher.length; j++) {
                cipher[j] = S_BOX[(cipher[j] & 0xff)];
            }
            shiftRows(cipher);
            if (i < nRounds - 1) {
                mixColumns(cipher);
            }
            System.arraycopy(keys, 16 * (i + 1), rKey, 0, 16);
            keyAddition(cipher, rKey);
        }
        return cipher;
    }

    public static byte[] aesBlockDecrypt(byte[] key, byte[] in) {
        assert key.length == 16 || key.length == 24 || key.length == 32;
        assert in.length == 16;
        int nRounds = key.length == 16 ? 10 : key.length == 24 ? 12 : 14;
        byte[] plaintext = new byte[in.length];
        System.arraycopy(in, 0, plaintext, 0, in.length);
        byte[] keys = key.length == 16 ? keySchedule128(key) : key.length == 24 ? keySchedule192(key) : keySchedule256(key);
        byte[] rKey = new byte[16];
        for (int i = 0; i < nRounds; i++) {
            System.arraycopy(keys, keys.length - 16 * (i + 1), rKey, 0, 16);
            keyAddition(plaintext, rKey);
            if (i > 0) {
                invMixColumns(plaintext);
            }

            invShiftRows(plaintext);

            for (int j = 0; j < plaintext.length; j++) {
                plaintext[j] = S_BOX_INV[(plaintext[j] & 0xff)];
            }
        }

        System.arraycopy(keys, 0, rKey, 0, 16);
        keyAddition(plaintext, rKey);

        return plaintext;
    }

    public static void keyAddition(byte[] cipher, byte[] rKey) {
        BitwiseUtils.xor(cipher, rKey);
    }

    public static void shiftRows(byte[] cipher) {
        byte tmp = cipher[15];
        cipher[15] = cipher[11];
        cipher[11] = cipher[7];
        cipher[7] = cipher[3];
        cipher[3] = tmp;
        tmp = cipher[14];
        cipher[14] = cipher[6];
        cipher[6] = tmp;
        tmp = cipher[10];
        cipher[10] = cipher[2];
        cipher[2] = tmp;
        tmp = cipher[1];
        cipher[1] = cipher[5];
        cipher[5] = cipher[9];
        cipher[9] = cipher[13];
        cipher[13] = tmp;
    }

    public static void invShiftRows(byte[] cipher) {
        byte tmp = cipher[15];
        cipher[15] = cipher[3];
        cipher[3] = cipher[7];
        cipher[7] = cipher[11];
        cipher[11] = tmp;
        tmp = cipher[14];
        cipher[14] = cipher[6];
        cipher[6] = tmp;
        tmp = cipher[10];
        cipher[10] = cipher[2];
        cipher[2] = tmp;
        tmp = cipher[1];
        cipher[1] = cipher[13];
        cipher[13] = cipher[9];
        cipher[9] = cipher[5];
        cipher[5] = tmp;
    }

    public static void mixColumns(byte[] cipher) {
        byte[] tmp = new byte[4];
        for (int i = 0; i < cipher.length; i+=4) {
            tmp[0] = (byte) (times2gf(cipher[i]) ^ times3gf(cipher[i + 1]) ^ cipher[i + 2] ^ cipher[i + 3]);
            tmp[1] = (byte) (cipher[i] ^ times2gf(cipher[i + 1]) ^ times3gf(cipher[i + 2]) ^ cipher[i + 3]);
            tmp[2] = (byte) (cipher[i] ^ cipher[i + 1] ^ times2gf(cipher[i + 2]) ^ times3gf(cipher[i + 3]));
            tmp[3] = (byte) (times3gf(cipher[i]) ^ cipher[i + 1] ^ cipher[i + 2] ^ times2gf(cipher[i + 3]));
            System.arraycopy(tmp, 0, cipher, i, 4);
        }
    }

    public static void invMixColumns(byte[] cipher) {
        byte[] tmp = new byte[4];
        for (int i = 0; i < cipher.length; i+=4) {
            tmp[0] = (byte) (timesEgf(cipher[i]) ^ timesBgf(cipher[i + 1]) ^ timesDgf(cipher[i + 2]) ^ times9gf(cipher[i + 3]));
            tmp[1] = (byte) (times9gf(cipher[i]) ^ timesEgf(cipher[i + 1]) ^ timesBgf(cipher[i + 2]) ^ timesDgf(cipher[i + 3]));
            tmp[2] = (byte) (timesDgf(cipher[i]) ^ times9gf(cipher[i + 1]) ^ timesEgf(cipher[i + 2]) ^ timesBgf(cipher[i + 3]));
            tmp[3] = (byte) (timesBgf(cipher[i]) ^ timesDgf(cipher[i + 1]) ^ times9gf(cipher[i + 2]) ^ timesEgf(cipher[i + 3]));
            System.arraycopy(tmp, 0, cipher, i, 4);
        }
    }

    private static byte times2gf(byte bt) {
        int msb = bt & 0x80;
        int out = (bt & 0xff) << 1;
        if (msb != 0) {
            out ^= MOD_P;
        }
        return (byte) out;
    }

    private static byte times3gf(byte bt) {
        int msb = bt & 0x80;
        int out = ((bt & 0xff) << 1) ^ (bt & 0xff);
        if (msb != 0) {
            out ^= MOD_P;
        }
        return (byte) out;
    }

    private static byte timesEgf(byte bt) {
        int out = ((bt & 0xff) << 3) ^ ((bt & 0xff) << 2) ^ ((bt & 0xff) << 1);
        for (int i = 0; i < 3; i++) {
            if(((out >>> i + 8) & 0x01) != 0) {
                out ^= (MOD_P & 0xff) << i;
            }
        }
        return (byte) out;
    }

    private static byte timesBgf(byte bt) {
        int out = ((bt & 0xff) << 3) ^ ((bt & 0xff) << 1) ^ (bt & 0xff);
        for (int i = 0; i < 3; i++) {
            if(((out >>> i + 8) & 0x01) != 0) {
                out ^= (MOD_P & 0xff) << i;
            }
        }
        return (byte) out;
    }

    private static byte timesDgf(byte bt) {
        int out = ((bt & 0xff) << 3) ^ ((bt & 0xff) << 2) ^ (bt & 0xff);
        for (int i = 0; i < 3; i++) {
            if(((out >>> i + 8) & 0x01) != 0) {
                out ^= (MOD_P & 0xff) << i;
            }
        }
        return (byte) out;
    }

    private static byte times9gf(byte bt) {
        int out = ((bt & 0xff) << 3) ^ (bt & 0xff);
        for (int i = 0; i < 3; i++) {
            if(((out >>> i + 8) & 0x01) != 0) {
                out ^= (MOD_P & 0xff) << i;
            }
        }
        return (byte) out;
    }

    public static byte[] keySchedule128(byte[] key) {
        int[] words = new int[11 * 4];
        System.arraycopy(BitwiseUtils.toIntArray(key), 0, words, 0, 4);
        for (int i = 0; i < 10; i++) {
            int indx = 4 * i;
            words[indx + 4] = words[indx] ^ g(words[indx + 3], i);
            words[indx + 5] = words[indx + 1] ^ words[indx + 4];
            words[indx + 6] = words[indx + 2] ^ words[indx + 5];
            words[indx + 7] = words[indx + 3] ^ words[indx + 6];
        }
        return BitwiseUtils.toByteArray(words);
    }

    private static int g(int word, int round) {
        word = (word << 8) ^ (word >>> 24);
        word = h(word);
        word ^= RC[round];
        return word;
    }

    public static byte[] keySchedule192(byte[] key) {
        int[] words = new int[13 * 4];
        System.arraycopy(BitwiseUtils.toIntArray(key), 0, words, 0, 6);
        for (int i = 0; i < 8; i++) {
            int indx = 6 * i;
            words[indx + 6] = words[indx] ^ g(words[indx + 5], i);
            words[indx + 7] = words[indx + 1] ^ words[indx + 6];
            words[indx + 8] = words[indx + 2] ^ words[indx + 7];
            words[indx + 9] = words[indx + 3] ^ words[indx + 8];
            if (indx + 10 < 52) {
                words[indx + 10] = words[indx + 4] ^ words[indx + 9];
                words[indx + 11] = words[indx + 5] ^ words[indx + 10];
            }
        }
        return BitwiseUtils.toByteArray(words);
    }

    public static byte[] keySchedule256(byte[] key) {
        int[] words = new int[15 * 4];
        System.arraycopy(BitwiseUtils.toIntArray(key), 0, words, 0, 8);
        for (int i = 0; i < 7; i++) {
            int indx = 8 * i;
            words[indx + 8] = words[indx] ^ g(words[indx + 7], i);
            words[indx + 9] = words[indx + 1] ^ words[indx + 8];
            words[indx + 10] = words[indx + 2] ^ words[indx + 9];
            words[indx + 11] = words[indx + 3] ^ words[indx + 10];
            if (indx + 12 < 60) {
                words[indx + 12] = words[indx + 4] ^ h(words[indx + 11]);
                words[indx + 13] = words[indx + 5] ^ words[indx + 12];
                words[indx + 14] = words[indx + 6] ^ words[indx + 13];
                words[indx + 15] = words[indx + 7] ^ words[indx + 14];
            }
        }
        return BitwiseUtils.toByteArray(words);
    }

    private static int h(int word) {
        return ((S_BOX[word >>> 24] & 0xff) << 24) ^ ((S_BOX[(word >>> 16) & 0xff] & 0xff) << 16) ^ ((S_BOX[(word >>> 8) & 0xff] & 0xff) << 8) ^ ((S_BOX[word & 0xff] & 0xff));
    }
}
