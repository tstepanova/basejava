package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.storage.serialization.JsonStreamSerialization;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonStreamSerialization()));
    }
}
