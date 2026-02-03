package org.example.sorting;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {
    public static <E extends Comparable<E>> void sort(List<E> input, int  p, int r, E maxTypeValue) {
        if (p < r) {
            int q = (p + r) / 2;
            sort(input, p, q, maxTypeValue);
            sort(input, q + 1, r, maxTypeValue);
            merge(input, p, q, r, maxTypeValue);
        }
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
