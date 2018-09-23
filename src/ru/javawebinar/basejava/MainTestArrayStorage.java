package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.ArrayStorage;
import ru.javawebinar.basejava.storage.SortedArrayStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.util.Arrays;

public class MainTestArrayStorage {
    static final ArrayStorage ARRAY_STORAGE = new ArrayStorage();
    static final SortedArrayStorage SORTED_ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        int[] arr = {10, 11, 12, 13, 14, 15, 17, 18, 19, 20};
        System.out.println(Arrays.binarySearch(arr, 0, 10, 14));
        System.out.println(Arrays.binarySearch(arr, 0, 10, 10));
        System.out.println(Arrays.binarySearch(arr, 0, 10, 16));

        testArrayStorage(ARRAY_STORAGE);
        testArrayStorage(SORTED_ARRAY_STORAGE);
    }

    static void testArrayStorage(Storage storage) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r4 = new Resume();
        r4.setUuid("uuid2");

        storage.save(r3);
        storage.save(r2);
        storage.update(r4);
        storage.save(r1);

        System.out.println("\n");
        System.out.println("Get r1: " + storage.get(r1.getUuid()));
        System.out.println("Size: " + storage.size());

        System.out.println("Get dummy: " + storage.get("dummy"));

        printAll(storage);
        storage.delete(r1.getUuid());
        printAll(storage);
        storage.clear();
        printAll(storage);

        System.out.println("Size: " + storage.size());
    }

    static void printAll(Storage storage) {
        System.out.println("\nGet All");
        System.out.println(storage.getClass().getSimpleName() + ": ");
        for (Resume r : storage.getAll()) {
            System.out.println(r);
        }
    }
}
