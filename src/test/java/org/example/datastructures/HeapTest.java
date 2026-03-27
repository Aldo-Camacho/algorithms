package org.example.datastructures;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.example.utils.PrintUtils.printHeap;

public class HeapTest {

    @Test
    public void bubbleUp() {
        Heap<Integer> heap = new Heap<>(List.of(2, 4, 7, 6, 5, 6, 8, 9, 10));
        printHeap(heap);
        Assert.assertFalse(heap.validate());
        heap.bubbleUp(5);
        printHeap(heap);
        Assert.assertTrue(heap.validate());
    }

    @Test
    public void bubbleDown() {
        Heap<Integer> heap = new Heap<>(List.of(15, 2, 5, 9, 11, 5, 8, 12, 19));
        printHeap(heap);
        Assert.assertFalse(heap.validate());
        heap.bubbleDown(0);
        printHeap(heap);
        Assert.assertTrue(heap.validate());
    }

    @Test
    public void add() {
        Heap<Integer> heap = new Heap<>(List.of(2, 4, 6, 6, 5, 7, 8, 9, 10));
        printHeap(heap);
        Assert.assertTrue(heap.validate());
        heap.add(1);
        printHeap(heap);
        Assert.assertTrue(heap.validate());
    }

    @Test
    public void remove() {
        Heap<Integer> heap = new Heap<>(List.of(2, 4, 6, 6, 5, 7, 8, 9, 10));
        printHeap(heap);
        Assert.assertTrue(heap.validate());
        Integer removed = heap.remove(3);
        printHeap(heap);
        Assert.assertNotNull(removed);
        Assert.assertEquals(6, removed.intValue());
        Assert.assertTrue(heap.validate());
    }

    @Test
    public void heapify() {
        List<Integer> list = List.of(1, 9, 15, 8, 6, 7, 3);
        printHeap(new Heap<>(list));
        Heap<Integer> minHeap = Heap.heapify(list, false);
        printHeap(minHeap);
        Assert.assertTrue(minHeap.validate());
        Heap<Integer> maxHeap = Heap.heapify(list, true);
        printHeap(maxHeap);
        Assert.assertTrue(maxHeap.validate());
    }
}