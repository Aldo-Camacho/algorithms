package org.example.sorting;

import org.example.partition.DNFPartition;
import org.example.partition.HoarePartition;
import org.example.partition.LomutoPartition;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuickSort {
    private static final Random random = new Random();

    public enum PartitionScheme {
        LOMUTO,
        HOARE,
        DNF
    }

    public static <E extends Comparable<E>> void sort(List<E> input, PartitionScheme scheme) {
        sort(input, scheme, 0, input.size());
    }

    private static <E extends Comparable<E>> void sort(List<E> input, PartitionScheme scheme, int start, int end) {
        if (end - start == 2) {
            if (input.get(start).compareTo(input.get(end - 1)) > 0) {
                Collections.swap(input, start, end - 1);
            }
            return;
        } else if (end - start < 2) {
            return;
        }
        switch (scheme) {
            case LOMUTO -> handleLomutoPartition(input, scheme, start, end);
            case HOARE -> handleHoarePartition(input, scheme, start, end);
            case DNF -> handleDNFPartition(input, scheme, start, end);
            default -> throw new IllegalArgumentException("Unknown partition scheme");
        }
    }

    private static <E extends Comparable<E>> void handleLomutoPartition(List<E> input, PartitionScheme scheme, int start, int end) {
        int pivot = LomutoPartition.partition(input, start, end);
        sort(input, scheme, start, pivot);
        sort(input, scheme, pivot + 1, end);
    }

    private static <E extends Comparable<E>> void handleHoarePartition(List<E> input, PartitionScheme scheme, int start, int end) {
        int pivot = HoarePartition.partition(input, start, end);
        sort(input, scheme, start, pivot + 1);
        sort(input, scheme, pivot + 1, end);
    }

    private static <E extends Comparable<E>> void handleDNFPartition(List<E> input, PartitionScheme scheme, int start, int end) {
        List<Integer> pivots = DNFPartition.partition(input, start, end);
        sort(input, scheme, start, pivots.get(0));
        sort(input, scheme, pivots.get(1), end);
    }
}
