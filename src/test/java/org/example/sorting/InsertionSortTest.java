package org.example.sorting;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class InsertionSortTest {
    Random random = new Random();

    @Test
    public void sort() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            list.add(i, random.nextInt());
        }
        long time = System.currentTimeMillis();
        InsertionSort.sort(list);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assert.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }

    @Test
    public void alreadySorted() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i, i);
        }
        long time = System.currentTimeMillis();
        InsertionSort.sort(list);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assert.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }

    @Test
    public void descSorted() {
        List<Integer> list = new ArrayList<>();
        int elements = 1000;
        for (int i = 0; i < elements; i++) {
            list.add(i, elements - i);
        }
        long time = System.currentTimeMillis();
        InsertionSort.sort(list);
        for (int i = 0; i < list.size() - 1; i++) {
            Assert.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }
}