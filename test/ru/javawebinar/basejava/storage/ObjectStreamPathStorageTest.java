package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serialization.ObjectStreamSerialization;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.toString(), new ObjectStreamSerialization()));
    }
}
