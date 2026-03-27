package org.example.cipher;

import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.example.utils.PrintUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class Salsa20Test {
    private static final byte[] KEY = hexStringToByteArray("0053a6f94c9ff24598eb3e91e4378add3083d6297ccf2275c81b6ec11467ba0d");
    private static final byte[] NONCE = hexStringToByteArray("0d74db42a91077de");

    @ParameterizedTest
    @MethodSource("provideBytes")
    public void testCorrectEncryptionAndDecryption(byte[] plaintext) throws InterruptedException {
        byte[] bCEnc = encryptWithBouncyCastle(KEY, NONCE, plaintext);
        long strt = System.currentTimeMillis();
        byte[] enc = Salsa20.encrypt(KEY,NONCE, plaintext, 20);
        long fnsh = System.currentTimeMillis();
        PrintUtils.printBytes(enc);
        System.out.printf("Custom implementation took %d ms\n", fnsh - strt);
        for (int i = 0; i < enc.length; i++) {
            Assertions.assertEquals(bCEnc[i], enc[i]);
        }
        byte[] dec = Salsa20.decrypt(KEY, NONCE, enc, 20);
        for (int i = 0; i < dec.length; i++) {
            Assertions.assertEquals(dec[i], plaintext[i]);
        }
    }

    private static byte[][] provideBytes() {
        return new byte[][] {
                new byte[64],
                "John Doe Does Not Doe".getBytes(),
                ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ultrices risus sed pharetra aliquam. Maecenas et ex sit amet elit hendrerit tempus in vel odio. Aliquam blandit augue sed condimentum rhoncus. Proin ac tellus nisl. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam malesuada mi ut augue efficitur aliquet. Etiam ultricies rutrum ullamcorper. Maecenas ut enim arcu. Praesent consectetur, quam nec congue placerat, ante quam tristique tortor, ac viverra libero ex sit amet nulla. Pellentesque bibendum magna augue, eu pharetra metus volutpat commodo. Duis tempus efficitur massa ut ornare. Praesent consequat fringilla massa vitae ornare. Nullam luctus lorem mi, at blandit massa congue vitae. Integer quis augue metus.\n" +
                "Proin lacinia ac felis sed consectetur. Vivamus sodales lorem a neque fringilla, quis feugiat sapien finibus. Nulla purus arcu, lacinia ut elementum eu, mattis sed metus. Quisque sit amet risus congue, aliquam magna eget, aliquam tortor. Ut posuere lobortis lorem, sed mattis dolor auctor at. Mauris suscipit ipsum erat, at sagittis urna pulvinar a. Etiam consequat neque at nisi egestas luctus. Vivamus pretium arcu et libero sodales accumsan. Nulla et vulputate arcu. Suspendisse nec cursus arcu. Aliquam viverra gravida nulla, nec condimentum odio placerat sed. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec dapibus elit ipsum, volutpat dapibus felis elementum in.\n" +
                "Phasellus arcu velit, mollis at orci malesuada, lobortis commodo neque. Nullam malesuada at nibh at laoreet. Suspendisse accumsan risus id tortor iaculis lacinia. Pellentesque congue id tellus id tincidunt. Sed nec nibh aliquam, luctus enim quis, sagittis magna. Praesent commodo lacinia ex, eu consectetur dolor hendrerit in. Aliquam tellus magna, rutrum id mi vel, dignissim lacinia nisi.\n" +
                "Duis neque sapien, mattis malesuada tellus sit amet, fermentum laoreet neque. Curabitur tincidunt nisl magna. Donec sed tellus semper, elementum nisl porta, mollis velit. Integer quam.").getBytes(),
                new byte[640]
        };
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] encryptWithBouncyCastle(byte[] key, byte[] nonce, byte[] plaintext) {
        Salsa20Engine bcEngine = new Salsa20Engine();

        KeyParameter keyParam = new KeyParameter(key);
        ParametersWithIV ivParams = new ParametersWithIV(keyParam, nonce);

        bcEngine.init(true, ivParams);

        byte[] ciphertext = new byte[plaintext.length];
        long strt = System.currentTimeMillis();
        bcEngine.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
        long fnsh = System.currentTimeMillis();
        PrintUtils.printBytes(ciphertext);
        System.out.printf("Standard implementation took %d ms\n", fnsh - strt);

        return ciphertext;
    }
}