package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serialization.DataStreamSerialization;

public class DataPathStorageTest extends AbstractStorageTest {

    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerialization()));
    }
}
