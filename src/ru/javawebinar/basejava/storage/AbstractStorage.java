package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public abstract int size();

    public abstract void clear();

    public void update(Resume resume) {
        updateElement(resume, getExistElement(resume.getUuid()));
    }

    public abstract Resume[] getAll();

    public void save(Resume resume) {
        insertElement(resume, getNotExistElement(resume.getUuid()));
    }

    public void delete(String uuid) {
        deleteElement(uuid, getExistElement(uuid));
    }

    public Resume get(String uuid) {
        return getElement(uuid, getExistElement(uuid));
    }

    protected abstract void updateElement(Resume resume, Object index);

    protected abstract void insertElement(Resume resume, Object index);

    protected abstract void deleteElement(String uuid, Object index);

    protected abstract Resume getElement(String uuid, Object index);

    protected abstract Object getIndex(String uuid);

    protected abstract boolean isExistElement(Object index);

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
}
