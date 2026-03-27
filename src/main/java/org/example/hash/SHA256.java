package org.example.hash;

import java.util.Arrays;

import static org.example.utils.BitwiseUtils.rightRotate;

public class SHA256 {
    public static final int H0 = 0x6a09e667;
    public static final int H1 = 0xbb67ae85;
    public static final int H2 = 0x3c6ef372;
    public static final int H3 = 0xa54ff53a;
    public static final int H4 = 0x510e527f;
    public static final int H5 = 0x9b05688c;
    public static final int H6 = 0x1f83d9ab;
    public static final int H7 = 0x5be0cd19;
    public static final int[] k = new int[] {
        0x428a2f98, 0x71374491, 0xb5c0fbcf,0xe9b5dba5,
        0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
        0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3,
        0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
        0xe49b69c1, 0xefbe4786, 0x0fc19dc6 ,0x240ca1cc,
        0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
        0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
        0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
        0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
        0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
        0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
        0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
        0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
        0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    public static byte[] hash(byte[] input) {
        byte[] hash = new byte[32];
        int[] hTotal = new int[] { H0, H1, H2, H3, H4, H5, H6, H7 };
        int size = input.length;
        int paddedSize = (size + 1 + 8 + 64) & ~(63);
        byte[] padded = new byte[paddedSize];
        System.arraycopy(input, 0, padded, 0, size);
        padded[size] = (byte) (1 << 7);
        long sizeL = size * 8L;
        for (int i = 1; i <= 8; i++) {
            byte lastByte = (byte) (sizeL & 0xFF);
            padded[paddedSize - i] = lastByte;
            sizeL >>>= 8;
        }
        for (int i = 0; i < paddedSize; i+=64) {
            int[] h = Arrays.copyOf(hTotal, 8);
            int[] w = new int[64];
            for (int j = i; j < i + 64; j+=4) {
                w[(j- i)/4] = ((padded[j] & 0xFF) << 24) | ((padded[j+1] & 0xFF) << 16) | ((padded[j+2] & 0xFF) << 8) | (padded[j+3] & 0xFF);
            }
            for (int j = 16; j < w.length; j++) {
                int s0 = rightRotate(w[j-15], 7) ^ rightRotate(w[j-15], 18) ^ (w[j-15] >>> 3);
                int s1 = rightRotate(w[j-2], 17) ^ rightRotate(w[j-2], 19) ^ (w[j-2] >>> 10);
                w[j] = w[j-16] + s0 + w[j-7] + s1;
            }
            for (int j = 0; j < w.length; j++) {
                int s1 = rightRotate(h[4], 6) ^ rightRotate(h[4], 11) ^ rightRotate(h[4], 25);
                int ch = (h[4] & h[5]) ^ ((~h[4]) & h[6]);
                int tmp1 = h[7] + s1 + ch + k[j] + w[j];
                int s0 = rightRotate(h[0], 2) ^ rightRotate(h[0], 13) ^ rightRotate(h[0],22);
                int maj = (h[0] & h[1]) ^ (h[0] & h[2]) ^(h[1] & h[2]);
                int tmp2 = s0 + maj;
                h[7] = h[6];
                h[6] = h[5];
                h[5] = h[4];
                h[4] = h[3] + tmp1;
                h[3] = h[2];
                h[2] = h[1];
                h[1] = h[0];
                h[0] = tmp1 + tmp2;
            }
            for (int j = 0; j < hTotal.length; j++) {
                hTotal[j] += h[j];
            }
        }
        for (int i = 0; i < hTotal.length; i++) {
            hash[4*i] = (byte) ((hTotal[i] >>> 24) & 0xFF);
            hash[4*i + 1] = (byte) ((hTotal[i] >>> 16) & 0xFF);
            hash[4*i + 2] = (byte) ((hTotal[i] >>> 8) & 0xFF);
            hash[4*i + 3] = (byte) ((hTotal[i]) & 0xFF);
        }
        return hash;
    }
}
