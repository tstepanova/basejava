package ru.javawebinar.basejava.storage;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    public ArrayStorageTest() throws Exception {
        super(new ArrayStorage());
        super.arrayStorage = new ArrayStorage();
        super.getAllTest();
    }
}