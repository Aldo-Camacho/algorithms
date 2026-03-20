package org.example.hash;

import org.example.utils.PrintUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA256Test {

    @ParameterizedTest
    @ValueSource(strings = {"Hello World", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ultrices risus sed pharetra aliquam. Maecenas et ex sit amet elit hendrerit tempus in vel odio. Aliquam blandit augue sed condimentum rhoncus. Proin ac tellus nisl. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam malesuada mi ut augue efficitur aliquet. Etiam ultricies rutrum ullamcorper. Maecenas ut enim arcu. Praesent consectetur, quam nec congue placerat, ante quam tristique tortor, ac viverra libero ex sit amet nulla. Pellentesque bibendum magna augue, eu pharetra metus volutpat commodo. Duis tempus efficitur massa ut ornare. Praesent consequat fringilla massa vitae ornare. Nullam luctus lorem mi, at blandit massa congue vitae. Integer quis augue metus.\n" +
            "Proin lacinia ac felis sed consectetur. Vivamus sodales lorem a neque fringilla, quis feugiat sapien finibus. Nulla purus arcu, lacinia ut elementum eu, mattis sed metus. Quisque sit amet risus congue, aliquam magna eget, aliquam tortor. Ut posuere lobortis lorem, sed mattis dolor auctor at. Mauris suscipit ipsum erat, at sagittis urna pulvinar a. Etiam consequat neque at nisi egestas luctus. Vivamus pretium arcu et libero sodales accumsan. Nulla et vulputate arcu. Suspendisse nec cursus arcu. Aliquam viverra gravida nulla, nec condimentum odio placerat sed. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec dapibus elit ipsum, volutpat dapibus felis elementum in.\n" +
            "Phasellus arcu velit, mollis at orci malesuada, lobortis commodo neque. Nullam malesuada at nibh at laoreet. Suspendisse accumsan risus id tortor iaculis lacinia. Pellentesque congue id tellus id tincidunt. Sed nec nibh aliquam, luctus enim quis, sagittis magna. Praesent commodo lacinia ex, eu consectetur dolor hendrerit in. Aliquam tellus magna, rutrum id mi vel, dignissim lacinia nisi.\n" +
            "Duis neque sapien, mattis malesuada tellus sit amet, fermentum laoreet neque. Curabitur tincidunt nisl magna. Donec sed tellus semper, elementum nisl porta, mollis velit. Integer quam."})
    public void hash(String msg) throws NoSuchAlgorithmException {
        long time = System.currentTimeMillis();
        byte[] hash1 = SHA256.hash(msg.getBytes());
        System.out.printf("Hashed: %s in %d ms\n", msg, (System.currentTimeMillis() - time));
        PrintUtils.printBytes(hash1);
        System.out.println(Base64.getEncoder().encodeToString(hash1));
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        time = System.currentTimeMillis();
        byte[] hash2 = messageDigest.digest(msg.getBytes());
        System.out.printf("Hashed: %s in %d ms\n", msg, (System.currentTimeMillis() - time));
        PrintUtils.printBytes(hash2);
        System.out.println(Base64.getEncoder().encodeToString(hash2));
        for (int i = 0; i < hash1.length; i++) {
            Assertions.assertEquals(hash1[i], hash2[i]);
        }
    }

}