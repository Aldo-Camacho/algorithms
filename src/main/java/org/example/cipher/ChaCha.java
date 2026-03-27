package org.example.cipher;

import java.nio.charset.StandardCharsets;

import static org.example.utils.BitwiseUtils.leftRotate;
import static org.example.utils.BitwiseUtils.toByteArrayLittleEndian;
import static org.example.utils.BitwiseUtils.toIntArrayLittleEndian;

public class ChaCha {
    private static final int[] CONSTANT = toIntArrayLittleEndian("expand 32-byte k".getBytes(StandardCharsets.US_ASCII));

    public static byte[] encrypt(byte[] key, byte[] nonce, byte[] input, int rounds, int counterInit) throws InterruptedException {
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
                    byte[] s20 = chaCha20k(key, nonce, j + counterInit, rounds);
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

    public static byte[] decrypt(byte[] key, byte[] nonce, byte[] input, int rounds, int counterInit) throws InterruptedException {
        return encrypt(key, nonce, input, rounds, counterInit);
    }

    public static byte[] chaCha20k(byte[] k, byte[] nonce, int pos, int rounds) {
        assert k.length == 32 || k.length == 16;
        assert nonce.length == 12;
        assert rounds == 20 || rounds == 12 || rounds == 8;

        if (k.length == 16) {
            byte[] tmp = new byte[32];
            System.arraycopy(k, 0, tmp, 0, 16);
            System.arraycopy(k, 0, tmp, 16, 16);
            k = tmp;
        }

        int[] kInt = toIntArrayLittleEndian(k);
        int[] nInt = toIntArrayLittleEndian(nonce);
        int[] state = new int[] { CONSTANT[0], CONSTANT[1], CONSTANT[2], CONSTANT[3], kInt[0], kInt[1], kInt[2], kInt[3], kInt[4], kInt[5], kInt[6], kInt[7], pos, nInt[0], nInt[1], nInt[2]};
        int[] initialState = state.clone();
        for (int i = 0; i < rounds; i+=2) {
            qR(state,0, 4, 8, 12);
            qR(state, 1, 5, 9, 13);
            qR(state, 2, 6, 10, 14);
            qR(state, 3, 7, 11, 15);

            qR(state,0, 5, 10, 15);
            qR(state, 1, 6, 11, 12);
            qR(state,  2, 7, 8, 13);
            qR(state, 3, 4, 9, 14);
        }
        for (int i = 0; i < state.length; i++) {
            state[i] += initialState[i];
        }
        return toByteArrayLittleEndian(state);
    }

    private static void qR(int[] state, int i0, int i1, int i2, int i3) {
        state[i0] += state[i1];
        state[i3] = leftRotate(state[i3] ^ state[i0], 16);
        state[i2] += state[i3];
        state[i1] = leftRotate(state[i1] ^ state[i2], 12);
        state[i0] += state[i1];
        state[i3] = leftRotate(state[i3] ^ state[i0], 8);
        state[i2] += state[i3];
        state[i1] = leftRotate(state[i1] ^ state[i2], 7);
    }
}
