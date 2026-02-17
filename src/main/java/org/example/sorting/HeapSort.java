package org.example.sorting;

import org.example.datastructures.Heap;

import java.util.List;

public class HeapSort {
    public static <E extends Comparable<E>> void sort(List<E> input) {
        Heap<E> heap = Heap.heapify(input, false);
        for (int i = 0; i < input.size(); i++) {
            input.set(i, heap.remove(0));
        }
    }
}
