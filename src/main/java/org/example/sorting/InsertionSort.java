package org.example.sorting;

import java.util.Collections;
import java.util.List;

public class InsertionSort {
    public static <E extends Comparable<E>> void sort(List<E> input) {
        for (int i = 0; i < input.size(); i++) {
            insert(input, i);
        }
    }

    public static <E extends Comparable<E>> void sort(List<E> input, int start, int end) {
        for (int i = start; i < end; i++) {
            insert(input, i);
        }
    }

    private static <E extends Comparable<E>> void insert(List<E> input, int j) {
        for (int i = j - 1; i >= 0; i--) {
            if (input.get(i).compareTo(input.get(i + 1)) > 0) {
                Collections.swap(input, i, i + 1);
            } else {
                break;
            }
        }
    }
}
