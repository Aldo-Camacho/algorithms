package org.example.datastructures;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HeapTest {

    @Test
    public void bubbleUp() {
        Heap<Integer> heap = new Heap<>(List.of(2, 4, 7, 6, 5, 6, 8, 9, 10));
        printHeap(heap);
        heap.bubbleUp(5);
        printHeap(heap);
    }

    @Test
    public void bubbleDown() {
        Heap<Integer> heap = new Heap<>(List.of(15, 2, 5, 9, 11, 5, 8, 12, 19));
        printHeap(heap);
        heap.bubbleDown(0);
        printHeap(heap);
    }

    private void printHeap(Heap<Integer> heap) {
        List<StringBuilder> lines = new ArrayList<>();
        int levels = (int) (Math.floor(log2(heap.size())) + 1);
        for (int i = 0; i < levels; i++) {
            lines.add(new StringBuilder());
        }
        for (int i = 0; i < heap.size(); i++) {
            int level = (int) Math.floor(log2(i + 1));
            lines.get(level).append(heap.get(i)).append(", ");
            for (int j = level - 1; j >= 0; j--) {
                lines.get(j).append(" ").insert(0, " ");
            }
        }
        for (StringBuilder line: lines) {
            System.out.println(line.toString());
        }
    }

    private double log2(int x) {
        return Math.log(x)/Math.log(2);
    }
}