package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializationStrategy {

    void doUpdateElement(Resume r, OutputStream os) throws IOException;

    Resume doGetElement(InputStream is) throws IOException;
}
