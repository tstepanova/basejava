package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void update(Resume resume) {
        updateElement(resume, getExistElement(resume.getUuid()));
    }

    protected abstract void updateElement(Resume resume, Object index);

    public void save(Resume resume) {
        insertElement(resume, getNotExistElement(resume.getUuid()));
    }

    protected abstract void insertElement(Resume resume, Object index);

    public void delete(String uuid) {
        deleteElement(getExistElement(uuid));
    }

    protected abstract void deleteElement(Object index);

    public Resume get(String uuid) {
        return getElement(getExistElement(uuid));
    }

    protected abstract Resume getElement(Object index);

    private Object getExistElement(String uuid) {
        Object index = getIndex(uuid);
        if (!isExistElement(index)) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private Object getNotExistElement(String uuid) {
        Object index = getIndex(uuid);
        if (isExistElement(index)) {
            throw new ExistStorageException(uuid);
        }
        return index;
    }

    protected abstract Object getIndex(String uuid);

    protected abstract boolean isExistElement(Object index);
}
