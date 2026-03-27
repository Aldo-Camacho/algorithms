package org.example.utils;

import org.example.datastructures.Heap;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

public class PrintUtils {
    private static double log2(int x) {
        return Math.log(x)/Math.log(2);
    }

    public static void printHeap(Heap<Integer> heap) {
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

    public static <E extends Number> void printList(List<E> list) {
        printList(list, 50);
    }

    public static <E extends Number> void printList(List<E> list, int maxElements) {
        StringBuilder sb = new StringBuilder("[");
        if (list.size() > maxElements) {
            for (int i = 0; i < maxElements; i++) {
                sb.append(list.get(i)).append(", ");
            }
        } else {
            list.forEach(i -> sb.append(i).append(", "));
        }
        sb.replace(sb.lastIndexOf(", "), sb.length(), "]");
        System.out.println(sb);
    }

    public static void printStrList(List<String> list) {
        printStrList(list, 50);
    }

    public static void printStrList(List<String> list, int maxElements) {
        StringBuilder sb = new StringBuilder("[");
        if (list.size() > maxElements) {
            for (int i = 0; i < maxElements; i++) {
                sb.append(list.get(i)).append(", ");
            }
        } else {
            list.forEach(i -> sb.append(i).append(", "));
        }
        sb.replace(sb.lastIndexOf(", "), sb.length(), "]");
        System.out.println(sb);
    }

    public static void printBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        HexFormat hex = HexFormat.of();
        for (byte b : bytes) {
            sb.append(hex.toHexDigits(b));
        }
        System.out.println(sb);
    }
}
