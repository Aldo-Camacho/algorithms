package org.example.search;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class BinarySearchTest {
    Random random = new Random();

    @Test
    public void search() {
        List<Integer> input = new ArrayList<>();
        for (int i = 1; i <= 1000000; i++) {
            input.add(2 * i + 1);
        }
        Integer element = random.nextInt(1, input.get(input.size() - 1) + 1);
        System.out.println("Element to search: " + element);
        long startTime = System.currentTimeMillis();
        Integer index = BinarySearch.search(input, element);
        System.out.println("Time searching: " + (System.currentTimeMillis() - startTime) + " ms");
        if (index != null) {
            System.out.println("Element found at: " + index);
            assertTrue(input.contains(element));
            assertEquals(element, input.get(index));
        } else {
            System.out.println("Element not found");
            assertFalse(input.contains(element));
        }
    }
}