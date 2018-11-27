package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamSerialization implements SerializationStrategy {

    public void doUpdateElement(Resume r, OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(r);
        }
    }

    public Resume doGetElement(InputStream is) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}
