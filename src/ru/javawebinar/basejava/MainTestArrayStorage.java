package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.ArrayStorage;
import ru.javawebinar.basejava.storage.SortedArrayStorage;

import java.util.Arrays;

public class MainTestArrayStorage {
    static final ArrayStorage ARRAY_STORAGE = new ArrayStorage();
    static final SortedArrayStorage SORTED_ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        int[] arr = {10, 11, 12, 13, 14, 15, 17, 18, 19, 20};
        System.out.println(Arrays.binarySearch(arr, 0, 10, 14));
        System.out.println(Arrays.binarySearch(arr, 0, 10, 10));
        System.out.println(Arrays.binarySearch(arr, 0, 10, 16));

        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r4 = new Resume();
        r4.setUuid("uuid2");

        ARRAY_STORAGE.save(r3);
        SORTED_ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r2);
        SORTED_ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.update(r4);
        SORTED_ARRAY_STORAGE.update(r4);
        ARRAY_STORAGE.save(r1);
        SORTED_ARRAY_STORAGE.save(r1);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()) + " | " + SORTED_ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size() + " | " + SORTED_ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy") + " | " + SORTED_ARRAY_STORAGE.get("dummy"));

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        SORTED_ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        SORTED_ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size() + " | " + SORTED_ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        System.out.println("ARRAY_STORAGE: ");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
        System.out.println("SORTED_ARRAY_STORAGE: ");
        for (Resume r : SORTED_ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
