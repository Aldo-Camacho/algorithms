package org.example.cipher;

import java.nio.charset.StandardCharsets;

import static org.example.utils.BitwiseUtils.leftRotate;
import static org.example.utils.BitwiseUtils.toByteArrayLittleEndian;
import static org.example.utils.BitwiseUtils.toIntArrayLittleEndian;

public class Salsa20 {
    private static final int[] CONSTANT = toIntArrayLittleEndian("expand 32-byte k".getBytes(StandardCharsets.US_ASCII));

    public static byte[] encrypt(byte[] key, byte[] nonce, byte[] input, int rounds) throws InterruptedException {
        int blocks = (input.length + 63) / 64;
        byte[] output = new byte[input.length];
        int nThreads = Math.min(Runtime.getRuntime().availableProcessors(), blocks);
        Thread[] threads = new Thread[nThreads];
        int blocksInThread = (blocks + nThreads - 1) / nThreads;
        for (int i = 0; i < nThreads; i++) {
            int initBlock = i * blocksInThread;
            int finishBlock = initBlock + blocksInThread;
            Thread block = new Thread(() -> {
                for (int j = initBlock; j < finishBlock; j++) {
                    if (j >= blocks) break;
                    int[] pos = new int[] {j, 0};
                    byte[] s20 = salsa20k(key, nonce, pos, rounds);
                    for (int k = 0; k < s20.length; k++) {
                        int index = 64 * j + k;
                        if (index >= input.length) break;
                        output[index] = (byte) ((input[index] ^ s20[k]) & 0xFF);
                    }
                }
            });
            block.start();
            threads[i] = block;
        }

        for (Thread thread: threads) {
            thread.join();
        }

        return output;
    }

    public static byte[] decrypt(byte[] key, byte[] nonce, byte[] input, int rounds) throws InterruptedException {
        return encrypt(key, nonce, input, rounds);
    }

    public static byte[] salsa20k(byte[] k, byte[] nonce, int[] pos, int rounds) {
        assert k.length == 32 || k.length == 16;
        assert nonce.length == 8;
        assert pos.length == 2;
        assert rounds == 20 || rounds == 12 || rounds == 8;

        if (k.length == 16) {
            byte[] tmp = new byte[32];
            System.arraycopy(k, 0, tmp, 0, 16);
            System.arraycopy(k, 0, tmp, 16, 16);
            k = tmp;
        }

        int[] kInt = toIntArrayLittleEndian(k);
        int[] nInt = toIntArrayLittleEndian(nonce);
        int[] state = new int[] { CONSTANT[0], kInt[0], kInt[1], kInt[2], kInt[3], CONSTANT[1], nInt[0], nInt[1], pos[0], pos[1], CONSTANT[2], kInt[4], kInt[5], kInt[6], kInt[7], CONSTANT[3]};
        int[] initialState = state.clone();
        for (int i = 0; i < rounds; i+=2) {
            qR(state,0, 4, 8, 12);
            qR(state, 5, 9, 13, 1);
            qR(state, 10, 14, 2, 6);
            qR(state, 15, 3, 7, 11);

            qR(state,0, 1, 2, 3);
            qR(state, 5, 6, 7, 4);
            qR(state,  10, 11, 8, 9);
            qR(state, 15, 12, 13, 14);
        }
        for (int i = 0; i < state.length; i++) {
            state[i] += initialState[i];
        }
        return toByteArrayLittleEndian(state);
    }

    private static void qR(int[] state, int i0, int i1, int i2, int i3) {
        state[i1] ^= leftRotate(state[i0] + state[i3], 7);
        state[i2] ^= leftRotate(state[i1] + state[i0], 9);
        state[i3] ^= leftRotate(state[i2] + state[i1], 13);
        state[i0] ^= leftRotate(state[i3] + state[i2], 18);
    }
}
