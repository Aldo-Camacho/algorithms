package org.example.sorting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubbleSortTest implements SortTest {
    Random random = new Random();

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18})
    public void sort(int pow) {
        int size = (int) Math.pow(2, pow);
        System.out.println("Sorting " + size + " elements");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i, random.nextInt());
        }
        long time = System.currentTimeMillis();
        BubbleSort.sort(list);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18})
    public void alreadySorted(int pow) {
        int size = (int) Math.pow(2, pow);
        System.out.println("Sorting " + size + " elements");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        long time = System.currentTimeMillis();
        BubbleSort.sort(list);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18})
    public void descSorted(int pow) {
        int size = (int) Math.pow(2, pow);
        System.out.println("Sorting " + size + " elements");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(size - i);
        }
        long time = System.currentTimeMillis();
        BubbleSort.sort(list);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }
}