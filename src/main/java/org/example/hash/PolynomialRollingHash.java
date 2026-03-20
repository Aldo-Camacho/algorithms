package org.example.hash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PolynomialRollingHash {
    public static BigInteger hash(String input, BigInteger p, BigInteger m) {
        byte[] inputBytes = input.getBytes();
        List<BigInteger> outArr = new ArrayList<>();
        for (int i = 0; i < inputBytes.length; i++) {
            BigInteger pPow = p.pow(inputBytes.length - i - 1);
            BigInteger in = BigInteger.valueOf(inputBytes[i]);
            outArr.add(pPow.multiply(in).mod(m));
        }
        return outArr.stream().reduce(BigInteger::add).orElseThrow(RuntimeException::new);
    }

    public static BigInteger hashE(String input, BigInteger p, BigInteger m) {
        byte[] inputBytes = input.getBytes();
        BigInteger hash = BigInteger.ZERO;
        for (byte inputByte : inputBytes) {
            hash = hash.multiply(p).add(BigInteger.valueOf(inputByte)).mod(m);
        }
        return hash;
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
