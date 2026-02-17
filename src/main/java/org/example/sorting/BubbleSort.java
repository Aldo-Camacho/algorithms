package org.example.sorting;

import java.util.Collections;
import java.util.List;

public class BubbleSort {
    public static <E extends Comparable<E>> void sort(List<E> list) {
        for (int i = 0; i < list.size() - 2; i++) {
            for (int j = i; j < list.size() - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    Collections.swap(list, i, i + 1);
                }
            }
        }
    }
}
