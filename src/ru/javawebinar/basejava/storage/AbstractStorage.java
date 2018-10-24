package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    public void update(Resume resume) {
        updateElement(resume, getExistElement(resume.getUuid()));
    }

    protected abstract void updateElement(Resume resume, Object searchKey);

    public List<Resume> getAllSorted() {
        List<Resume> list = getAllSortedElements();
        Comparator<Resume> resumeComparator = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
        list.sort(resumeComparator);
        return list;
    }

    protected abstract List<Resume> getAllSortedElements();

    public void save(Resume resume) {
        insertElement(resume, getNotExistElement(resume.getUuid()));
    }

    protected abstract void insertElement(Resume resume, Object searchKey);

    public void delete(String uuid) {
        deleteElement(getExistElement(uuid));
    }

    protected abstract void deleteElement(Object searchKey);

    public Resume get(String uuid) {
        return getElement(getExistElement(uuid));
    }

    protected abstract Resume getElement(Object searchKey);

    private Object getExistElement(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExistElement(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistElement(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExistElement(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExistElement(Object searchKey);
}
