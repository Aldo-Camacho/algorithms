package org.example.partition;

import org.example.utils.RandomUtils;

import java.util.Collections;
import java.util.List;

public class HoarePartition {
    public static <E extends Comparable<E>> int partition(List<E> input, int start, int end) {
        int i = start - 1;
        int j = end;
        int pivotI = RandomUtils.get().nextInt(start, end);
        E pivot = input.get(pivotI);
        Collections.swap(input, start, pivotI);
        while (true) {
            do {
                i++;
            } while (input.get(i).compareTo(pivot) < 0 && i < end - 1);

            do {
                j--;
            } while (input.get(j).compareTo(pivot) > 0);

            if (j <= i) {
                break;
            }
            Collections.swap(input, i, j);
        }
        return j;
    }
}
