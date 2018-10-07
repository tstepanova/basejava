package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
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
    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(resume.getUuid());
        } else if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        } else {
            insertElement(resume, index);
        }
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    protected void updateElement(Resume resume, int index) {
        storage[index] = resume;
    }

    @Override
    protected Resume getElement(String uuid, int index) {
        return storage[index];
    }

    @Override
    protected void fillDeletedElement(String uuid, int index) {
        doDeletedElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void insertElement(Resume resume, int index) {
        doInsertElement(resume, index);
        size++;
    }

    protected abstract void doDeletedElement(int index);

    protected abstract void doInsertElement(Resume resume, int index);
}
