package org.example.partition;

import org.example.utils.RandomUtils;

import java.util.Collections;
import java.util.List;

public class LomutoPartition {
    public static <E extends Comparable<E>> int partition(List<E> input, int start, int end) {
        int i = start - 1;
        int j = start;
        int pivotI = end - 1;
        int randI = RandomUtils.get().nextInt(start, end);
        Collections.swap(input, randI, pivotI);
        while (j < pivotI) {
            if (input.get(j).compareTo(input.get(pivotI)) < 0) {
                Collections.swap(input, j, i + 1);
                i++;
            }
            j++;
        }
        Collections.swap(input, pivotI, i + 1);
        return i + 1;
    }
}
