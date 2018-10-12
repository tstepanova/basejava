package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void updateElement(Resume resume, Object index) {
        storage[(int) index] = resume;
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    protected void insertElement(Resume resume, Object index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        } else {
            doInsertElement(resume, (int) index);
            size++;
        }
    }

    @Override
    protected void deleteElement(String uuid, Object index) {
        doDeleteElement((int) index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume getElement(String uuid, Object index) {
        return storage[(int) index];
    }

    protected abstract void doDeleteElement(int index);

    protected abstract void doInsertElement(Resume resume, int index);

    @Override
    protected boolean isExistElement(Object index) {
        return ((int) index >= 0);
    }
}
