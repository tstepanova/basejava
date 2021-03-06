package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void updateElement(Resume resume, Integer index) {
        storage[index] = resume;
    }

    @Override
    public List<Resume> getAllSortedElements() {
        return Arrays.asList(Arrays.copyOfRange(storage, 0, size));
    }

    @Override
    protected void insertElement(Resume resume, Integer index) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        } else {
            doInsertElement(resume, index);
            size++;
        }
    }

    protected abstract void doInsertElement(Resume resume, int index);

    @Override
    protected void deleteElement(Integer index) {
        doDeleteElement(index);
        storage[size - 1] = null;
        size--;
    }

    protected abstract void doDeleteElement(int index);

    @Override
    protected Resume getElement(Integer index) {
        return storage[index];
    }

    @Override
    protected boolean isExistElement(Integer index) {
        return (index >= 0);
    }
}
