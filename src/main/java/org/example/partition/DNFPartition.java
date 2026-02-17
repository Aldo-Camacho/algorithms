package org.example.partition;

import org.example.utils.RandomUtils;

import java.util.Collections;
import java.util.List;

public class DNFPartition {
    public static <E extends Comparable<E>> List<Integer> partition(List<E> input, int start, int end) {
        int l = start;
        int m = start;
        int h = end - 1;
        int pivotI = RandomUtils.get().nextInt(start, end);
        E pivot = input.get(pivotI);
        Collections.swap(input, start, pivotI);
        while (m <= h) {
            if (input.get(m).compareTo(pivot) < 0) {
                Collections.swap(input, l, m);
                m++;
                l++;
            } else if (input.get(m).compareTo(pivot) > 0) {
                while (h > m && input.get(h).compareTo(pivot) > 0) {
                    h--;
                }
                Collections.swap(input, m, h);
                h--;
            } else {
                m++;
            }
        }
        return List.of(l, m);
    }
}
