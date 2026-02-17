package org.example.hash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PolynomialRollingHash {
    public static long hash(String input, long p, long m) {
        byte[] inputBytes = input.getBytes();
        List<Long> outArr = new ArrayList<>();
        for (int i = 0; i < inputBytes.length; i++) {
            long pPow = (long) Math.pow(p, i);
            long in = inputBytes[i];
            outArr.add((pPow * in) % m);
        }
        return outArr.stream().reduce(Long::sum).orElseThrow(RuntimeException::new);
    }

    public static <E extends Serializable> long hash(E input, long p, long m) throws IOException {
        List<Long> outArr = new ArrayList<>();
        try (ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(bAOS)) {
            objectOutputStream.writeObject(input);
            objectOutputStream.flush();
            byte[] inputBytes = bytesWithPadding(bAOS.toByteArray());
            ByteBuffer buffer = ByteBuffer.wrap(inputBytes);
            for (int i = 0; i < inputBytes.length; i += 4) {
                long pPow = (long) Math.pow(p, i);
                long inputBytesAsLong = buffer.getLong();
                outArr.add((pPow * inputBytesAsLong) % m);
            }
        }
        return outArr.stream().reduce(Long::sum).orElseThrow(RuntimeException::new);
    }

    private static byte[] bytesWithPadding(byte[] bytes) {
        int mult = (bytes.length + 7) & ~7;
        byte[] inBytes = new byte[mult];
        for (int i = 0; i < mult; i++) {
            if (i < bytes.length) {
                inBytes[i] = bytes[i];
            } else {
                inBytes[i] = 0;
            }
        }
        return inBytes;
    }
}
