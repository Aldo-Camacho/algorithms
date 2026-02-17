package org.example.search;

import org.example.sorting.QuickSort;
import org.example.utils.PrintUtils;
import org.example.utils.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QuickSelectTest {

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18, 20})
    public void getIthElementOnSortedArray(int pow) {
        int size = (int) Math.pow(2, pow);
        int ith = RandomUtils.get().nextInt(1, size);
        System.out.println("Finding " + ith + getIthTerm(ith) + " smallest element on array of " + size + " elements");
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            list.add(i);
        }
        long time = System.currentTimeMillis();
        Integer el = QuickSelect.getIthElement(list, ith);
        System.out.println("Found " + el + " as " + ith + getIthTerm(ith) + " element in " + (System.currentTimeMillis() - time) + " ms");
        assertEquals(el, list.get(ith - 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18, 20})
    public void getIthElementOnRandomArray(int pow) {
        int size = (int) Math.pow(2, pow);
        int ith = RandomUtils.get().nextInt(1, size);
        System.out.println("Finding " + ith + getIthTerm(ith) + " smallest element on array of " + size + " elements");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(RandomUtils.get().nextInt());
        }
        long time = System.currentTimeMillis();
        Integer el = QuickSelect.getIthElement(list, ith);
        System.out.println("Found " + el + " as " + ith + getIthTerm(ith) + " element in " + (System.currentTimeMillis() - time) + " ms");
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18, 20})
    public void getIthElementOnDuplicates(int pow) {
        int size = (int) Math.pow(2, pow);
        int ith = RandomUtils.get().nextInt(1, size);
        System.out.println("Finding " + ith + getIthTerm(ith) + " smallest element on array of " + size + " elements");
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            list.add(i % 20);
        }
        long time = System.currentTimeMillis();
        Integer el = QuickSelect.getIthElement(list, ith);
        System.out.println("Found " + el + " as " + ith + getIthTerm(ith) + " element in " + (System.currentTimeMillis() - time) + " ms");
    }

    private String getIthTerm(int i) {
        return switch (i % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}