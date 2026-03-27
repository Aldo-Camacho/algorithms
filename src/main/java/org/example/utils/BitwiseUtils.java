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
}
