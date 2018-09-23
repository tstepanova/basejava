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
        Resume resume1 = new Resume();
        resume1.setUuid("uuid1");
        Resume resume2 = new Resume();
        resume2.setUuid("uuid2");
        Resume resume3 = new Resume();
        resume3.setUuid("uuid3");
        Resume resume4 = new Resume();
        resume4.setUuid("uuid2");

        storage.save(resume3);
        storage.save(resume2);
        storage.update(resume4);
        storage.save(resume1);

        System.out.println("\n");
        System.out.println("Get resume1: " + storage.get(resume1.getUuid()));
        System.out.println("Size: " + storage.size());

        System.out.println("Get dummy: " + storage.get("dummy"));

        printAll(storage);
        storage.delete(resume1.getUuid());
        printAll(storage);
        storage.clear();
        printAll(storage);

        System.out.println("Size: " + storage.size());
    }

    static void printAll(Storage storage) {
        System.out.println("\nGet All");
        System.out.println(storage.getClass().getSimpleName() + ": ");
        for (Resume resume : storage.getAll()) {
            System.out.println(resume);
        }
    }
}
