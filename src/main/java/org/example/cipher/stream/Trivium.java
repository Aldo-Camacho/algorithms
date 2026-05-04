package org.example.cipher.stream;

import static org.example.utils.BitwiseUtils.getIthBit;
import static org.example.utils.BitwiseUtils.leftShiftBits;
import static org.example.utils.BitwiseUtils.rightShiftBits;

public class Trivium {
    private static final int A_MASK = 0xF8;
    private static final int B_MASK = 0xF0;
    private static final int C_MASK = 0xFE;
    private static final int INITIALIZATION_ROUNDS = 4 * 288;

    public static byte[] encrypt(byte[] key, byte[] nonce, byte[] in) {
        byte[] kStream = trivium(key, nonce, in.length);
        byte[] out = new byte[in.length];
        for (int i = 0; i < in.length; i++) {
            out[i] = (byte) ((kStream[i] ^ in[i]) & 0xFF);
        }
        return out;
    }

    public static byte[] decrypt(byte[] key, byte[] nonce, byte[] in) {
        return encrypt(key, nonce, in);
    }

    public static byte[] trivium(byte[] key, byte[] nonce, int bytes) {
        assert key.length == 10;
        assert nonce.length == 10;
        byte[] a = new byte[12];
        byte[] b = new byte[11];
        byte[] c = new byte[14];
        byte[] out = new byte[bytes];
        System.arraycopy(key, 0, a, 0, key.length);
        System.arraycopy(nonce, 0, b, 0, nonce.length);
        c[13] |= (0x0E);
        for (int i = 0; i < INITIALIZATION_ROUNDS; i++) {
            round(a, b, c);
        }

        for (int i = 0; i < bytes; i++) {
            int byt = 0;
            for (int j = 0; j < 8; j++) {
                byt |= (round(a, b, c) << j);
            }
            out[i] = (byte) (byt & 0xFF);
        }
        return out;
    }

    private static byte round(byte[] a, byte[] b, byte[] c) {
        int aFw = (getIthBit(a, 65) ^ getIthBit(a, 92)) & 1;
        int bFw = (getIthBit(b, 68) ^ getIthBit(b, 83)) & 1;
        int cFw = (getIthBit(c, 65) ^ getIthBit(c, 110)) & 1;

        int aAnd = (getIthBit(a, 90) & getIthBit(a, 91)) & 1;
        int bAnd = (getIthBit(b, 81) & getIthBit(b, 82)) & 1;
        int cAnd = (getIthBit(c, 108) & getIthBit(c, 109)) & 1;

        int aFb = (getIthBit(a, 68) ^ (cAnd ^ cFw)) & 1;
        int bFb = (getIthBit(b, 77) ^ (aAnd ^ aFw)) & 1;
        int cFb = (getIthBit(c, 86) ^ (bAnd ^ bFw)) & 1;

        byte out = (byte) ((aFw ^ bFw ^ cFw) & 1);

        rightShiftBits(a, 1);
        a[0] = (byte) ((a[0] & 0xFF) | (aFb << 7));
        a[11] = (byte) (a[11] & A_MASK);

        rightShiftBits(b, 1);
        b[0] = (byte) ((b[0] & 0xFF) | (bFb << 7));
        b[10] = (byte) (b[10] & B_MASK);

        rightShiftBits(c, 1);
        c[0] = (byte) ((c[0] & 0xFF) | (cFb << 7));
        c[13] = (byte) (c[13] & C_MASK);

        return out;
    }
}
