package org.example.utils;

public class BitwiseUtils {
    public static int leftRotate(int i, int bits) {
        return (i << bits) | (i >>> (32 - bits));
    }

    public static int rightRotate(int i, int bits) {
        return (i >>> bits) | (i << (32 - bits));
    }

    public static int[] toIntArrayLittleEndian(byte[] bytes) {
        assert bytes.length % 4 == 0;
        int intSize = bytes.length / 4;
        int[] ints = new int[intSize];
        int counter = 0;
        for (int i = 0; i < bytes.length; i+=4) {
            ints[counter] = bytesToInt(bytes[i], bytes[i+1], bytes[i+2], bytes[i+3]);
            counter++;
        }
        return ints;
    }

    private static int bytesToInt(byte a, byte b, byte c, byte d) {
        return ((d & 0xFF) << 24) | ((c & 0xFF) << 16) | ((b & 0xFF) << 8) | (a & 0xFF);
    }

    public static byte[] toByteArrayLittleEndian(int[] ints) {
        int byteSize = 4 * ints.length;
        byte[] bytes = new byte[byteSize];
        for (int i = 0; i < ints.length; i++) {
            int b = 4 * i;
            bytes[b + 3] = (byte) ((ints[i] >>> 24) & 0xFF);
            bytes[b + 2] = (byte) ((ints[i] >>> 16) & 0xFF);
            bytes[b + 1] = (byte) ((ints[i] >>> 8) & 0xFF);
            bytes[b] = (byte) ((ints[i]) & 0xFF);
        }
        return bytes;
    }

    public static int[] toIntArray(byte[] bytes) {
        assert bytes.length % 4 == 0;
        int intSize = bytes.length / 4;
        int[] ints = new int[intSize];
        int counter = 0;
        for (int i = 0; i < bytes.length; i+=4) {
            ints[counter] = bytesToInt(bytes[i+3], bytes[i+2], bytes[i+1], bytes[i]);
            counter++;
        }
        return ints;
    }

    public static byte[] toByteArray(int[] ints) {
        int byteSize = 4 * ints.length;
        byte[] bytes = new byte[byteSize];
        for (int i = 0; i < ints.length; i++) {
            int b = 4 * i;
            bytes[b] = (byte) ((ints[i] >>> 24) & 0xFF);
            bytes[b + 1] = (byte) ((ints[i] >>> 16) & 0xFF);
            bytes[b + 2] = (byte) ((ints[i] >>> 8) & 0xFF);
            bytes[b + 3] = (byte) ((ints[i]) & 0xFF);
        }
        return bytes;
    }

    public static byte[] getIntBytes(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((i >>> 24) & 0xFF);
        bytes[1] = (byte) ((i >>> 16) & 0xFF);
        bytes[2] = (byte) ((i >>> 8) & 0xFF);
        bytes[3] = (byte) ((i) & 0xFF);
        return bytes;
    }

    public static byte getIthBit(byte[] reg, int i) {
        int byteNum = i / 8;
        int bitNum = i % 8;
        int curr = reg[byteNum] & 0xFF;
        return (byte) ((curr >>> (7 - bitNum)) & 1);
    }

    public static void rightShiftBits(byte[] bytes, int bits) {
        if (bits == 0) {
            return;
        }
        int carry = 0;
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i] & 0xFF;
            bytes[i] = (byte) ((b >>> bits) | carry);
            carry = (byte) (b << (8 - bits)) & 0xFF;
        }
    }

    public static void leftShiftBits(byte[] bytes, int bits) {
        if (bits == 0) {
            return;
        }
        byte carry = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            int b = bytes[i] & 0xFF;
            bytes[i] = (byte) ((b << bits) | carry);
            carry = (byte) (b >>> (8 - bits));
        }
    }

    public static int rightShiftWithNegativeLeft(int num, int bits) {
        if (bits >= 0) {
            return num >>> bits;
        } else {
            return num << -bits;
        }
    }

    public static int leftShiftWithNegativeRight(int num, int bits) {
        if (bits >= 0) {
            return num << bits;
        } else {
            return num >>> -bits;
        }
    }

    public static long bytesToLong(byte[] bytes) {
        assert bytes.length < 9;
        long out = 0;
        for (int i = 0; i < bytes.length; i++) {
            out |= (long) (bytes[i] & 0xFF) << (8* (bytes.length - 1 - i));
        }
        return out;
    }

    public static void xorIntArray(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] ^= b[i];
        }
    }

    public static void xor(byte[] a, byte[] b) {
        assert a.length == b.length;
        for (int i = 0; i < a.length; i++) {
            a[i] ^= b[i];
        }
    }

    public static void incrementOne(byte[] bytes) {
        boolean carry = true;
        int i = bytes.length - 1;
        do {
            bytes[i]++;
            carry = bytes[i] == 0;
            i--;
        } while (carry && i >= 0);
    }
}
