package org.example.cipher.stream;

import org.example.utils.PrintUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TriviumTest {
    private static final byte[] KEY = hexStringToByteArray("00000000000000000000");
    private static final byte[] NONCE = hexStringToByteArray("00000000000000000000");

    @ParameterizedTest
    @MethodSource("provideBytes")
    public void testCorrectEncryptionAndDecryption(byte[] plaintext) throws Exception {
       long strt = System.currentTimeMillis();
        byte[] enc = Trivium.encrypt(KEY,NONCE, plaintext);
        long fnsh = System.currentTimeMillis();
        PrintUtils.printBytes(enc);
        System.out.printf("Custom implementation took %d ms\n", fnsh - strt);
        byte[] dec = Trivium.decrypt(KEY, NONCE, enc);
        for (int i = 0; i < dec.length; i++) {
            Assertions.assertEquals(dec[i], plaintext[i]);
        }
    }

    private static byte[][] provideBytes() {
        return new byte[][] {
                new byte[64],
                "John Doe Does Not Doe".getBytes(),
                "Ladies and Gentlemen of the class of '99: If I could offer you only one tip for the future, sunscreen would be it.".getBytes(),
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
}