package org.example.sorting;

import org.example.partition.DNFPartition;
import org.example.partition.HoarePartition;
import org.example.partition.LomutoPartition;
import org.example.utils.PrintUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuickSortTest implements SortTest {
    Random random = new Random();

    @Test
    public void lomutoScheme() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(random.nextInt(0,20));
        }
        PrintUtils.printList(list);
        int pivot = LomutoPartition.partition(list, 0, list.size());
        PrintUtils.printList(list);
        System.out.println(pivot);
    }

    @Test
    public void hoareScheme() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(random.nextInt(0,20));
        }
        PrintUtils.printList(list);
        int pivot = HoarePartition.partition(list, 0, list.size());
        PrintUtils.printList(list);
        System.out.println(pivot);
    }

    @Test
    public void dnfScheme() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(random.nextInt(0,20));
        }
        PrintUtils.printList(list);
        List<Integer> pivot = DNFPartition.partition(list, 0, list.size());
        PrintUtils.printList(list);
        PrintUtils.printList(pivot);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18, 20})
    public void breakit(int pow) {
        int size = (int) Math.pow(2, pow);
        System.out.println("Sorting " + size + " elements with LOMUTO partitioning");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
        }
        long time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.LOMUTO);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
        System.out.println("Sorting " + size + " elements with HOARE partitioning");
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
        }
        time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.HOARE);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
        System.out.println("Sorting " + size + " elements with DNF partitioning");
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
        }
        time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.DNF);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18, 20})
    public void sort(int pow) {
        int size = (int) Math.pow(2, pow);
        System.out.println("Sorting " + size + " elements with LOMUTO partitioning");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
        }
        long time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.LOMUTO);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
        System.out.println("Sorting " + size + " elements with HOARE partitioning");
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
        }
        time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.HOARE);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
        System.out.println("Sorting " + size + " elements with DNF partitioning");
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
        }
        time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.DNF);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18, 20})
    public void alreadySorted(int pow) {
        int size = (int) Math.pow(2, pow);
        System.out.println("Sorting " + size + " elements with LOMUTO partitioning");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        long time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.LOMUTO);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
        System.out.println("Sorting " + size + " elements with HOARE partitioning");
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.HOARE);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
        System.out.println("Sorting " + size + " elements with DNF partitioning");
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.DNF);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 8, 10, 15, 18, 20})
    public void descSorted(int pow) {
        int size = (int) Math.pow(2, pow);
        System.out.println("Sorting " + size + " elements with LOMUTO partitioning");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(size - i);
        }
        long time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.LOMUTO);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
        System.out.println("Sorting " + size + " elements with HOARE partitioning");
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(size - i);
        }
        time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.HOARE);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
        System.out.println("Sorting " + size + " elements with DNF partitioning");
        list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(size - i);
        }
        time = System.currentTimeMillis();
        QuickSort.sort(list, QuickSort.PartitionScheme.DNF);
        System.out.println("Sorting time " + (System.currentTimeMillis() - time) + " ms");
        for (int i = 0; i < list.size() - 1; i++) {
            Assertions.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }
}