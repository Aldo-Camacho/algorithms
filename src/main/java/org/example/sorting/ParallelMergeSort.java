package org.example.sorting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import lombok.AllArgsConstructor;

public class ParallelMergeSort {
    private final static int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    @AllArgsConstructor
    private static class RecursiveSort<E extends Comparable<E>> extends RecursiveAction {
        List<E> input;
        int minSize;
        int p;
        int r;
        E maxTypeValue;

        @Override
        protected void compute() {
            if (p < r) {
                if (r - p <= minSize) {
                    MergeSort.sort(input, p, r, maxTypeValue);
                } else {
                    int q = (p + r) / 2;
                    RecursiveSort<E> r1 = new RecursiveSort<>(input, minSize, p, q, maxTypeValue);
                    RecursiveSort<E> r2 = new RecursiveSort<>(input, minSize, q + 1, r, maxTypeValue);

                    r1.fork();
                    r2.compute();
                    r1.join();
                    merge(input, p, q, r, maxTypeValue);
                }
            }
        }
    }
    public static <E extends Comparable<E>> void sort(List<E> input, E maxTypeValue) throws InterruptedException {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        int minSize = input.size() / MAX_THREADS;
        RecursiveSort<E> recursiveSort = new RecursiveSort<>(input,  minSize, 0, input.size() - 1, maxTypeValue);
        commonPool.invoke(recursiveSort);
        recursiveSort.join();
    }

    private static <E extends Comparable<E>> void merge(List<E> input, int p, int q, int r, E maxTypeValue) {
        List<E> left = new ArrayList<>();
        List<E> right = new ArrayList<>();
        for (int i = p; i <= q; i++) {
            left.add(input.get(i));
        }
        for (int i = q + 1; i <= r; i++) {
            right.add(input.get(i));
        }
        left.add(maxTypeValue);
        right.add(maxTypeValue);
        int i = 0;
        int j = 0;
        for (int k = p; k <= r; k++) {
            if (left.get(i).compareTo(right.get(j)) < 0) {
                input.set(k, left.get(i));
                i++;
            } else {
                input.set(k, right.get(j));
                j++;
            }
        }
    }
}
