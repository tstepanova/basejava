package ru.javawebinar.basejava.storage;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() throws Exception {
        super(new SortedArrayStorage());
        super.arrayStorage = new SortedArrayStorage();
        getAllTest();
    }
}