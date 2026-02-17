package org.example.search;

import org.example.partition.DNFPartition;

import java.util.List;

public class QuickSelect {
    public static <E extends Comparable<E>> E getIthElement(List<E> input, int i) {
        assert i > 0 && i <= input.size();
        return getIthElement(input, i - 1, 0, input.size());
    }

    private static <E extends Comparable<E>> E getIthElement(List<E> input, int i, int start, int end) {
        List<Integer> pivots = DNFPartition.partition(input, start, end);
        if (pivots.get(0) > i) {
            return getIthElement(input, i, start, pivots.get(0));
        } else if (pivots.get(1) <= i) {
            return getIthElement(input, i, pivots.get(1), end);
        } else {
            return input.get(i);
        }
    }
}
