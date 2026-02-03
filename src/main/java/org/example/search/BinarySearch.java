package org.example.search;

import java.util.List;

public class BinarySearch {
    public static <E extends Comparable<E>> Integer search(List<E> input, E element) {
        int l = 0;
        int r = input.size() - 1;
        while (l <= r) {
            int m = (l + r) / 2;
            E elM = input.get(m);
            if (elM.compareTo(element) > 0) {
                r = m - 1;
            } else if (elM.compareTo(element) < 0) {
                l = m + 1;
            } else {
                return m;
            }
        }
        return null;
    }
}
