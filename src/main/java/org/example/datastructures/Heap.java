package org.example.datastructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Heap<E extends Comparable<E>> extends ArrayList<E> {
    private boolean maxHeap;

    public Heap(Collection<E> c) {
      super(c);
    }

    public Integer getLeftChildIndex(int i) {
        if (i > this.size()) {
            return null;
        }
        int leftChild = 2 * i + 1;
        if (leftChild < this.size()) {
            return leftChild;
        }
        return null;
    }

    public Integer getRightChildIndex(int i) {
        Integer leftChild = getLeftChildIndex(i);
        if (leftChild == null) {
            return null;
        }
        if (leftChild + 1 < this.size()) {
            return leftChild + 1;
        }
        return null;
    }

    public Integer getParentIndex(int i) {
        if (i > this.size()) {
            return null;
        }
        return (i - 1) / 2;
    }

    public void bubbleUp(int i) {
        if (i > 0 && i < size()) {
            int parentI = getParentIndex(i);
            E a = get(i);
            E parent = get(parentI);
            if (parent.compareTo(a) > 0 ^ isMaxHeap()) {
                set(parentI, a);
                set(i, parent);
                bubbleUp(parentI);
            }
        }
    }

    public void bubbleDown(int i) {
        Integer leftCh = getLeftChildIndex(i);
        Integer rightCh = getRightChildIndex(i);
        if (rightCh != null) {
            E parent = get(i);
            E l = get(leftCh);
            E r = get(rightCh);
            E minChild;
            int mChildI;
            if (l.compareTo(r) <= 0 ^ isMaxHeap()) {
                minChild = l;
                mChildI = leftCh;
            } else {
                minChild = r;
                mChildI = rightCh;
            }
            if (parent.compareTo(minChild) > 0 ^ isMaxHeap()) {
                set(i, minChild);
                set(mChildI, parent);
                bubbleDown(mChildI);
            }
        } else if (leftCh != null) {
            E parent = get(i);
            E l = get(leftCh);
            if (parent.compareTo(l) > 0 ^ isMaxHeap()) {
                set(i, l);
                set(leftCh, parent);
                bubbleDown(leftCh);
            }
        }
    }
}
