package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.logging.Logger;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());
    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    public void update(Resume resume) {
        LOG.info("Update " + resume);
        updateElement(resume, getExistElement(resume.getUuid()));
    }

    protected abstract void updateElement(Resume resume, SK searchKey);

    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> list = getAllSortedElements();
        list.sort(RESUME_COMPARATOR);
        return list;
    }

    protected abstract List<Resume> getAllSortedElements();

    public void save(Resume resume) {
        LOG.info("Save " + resume);
        insertElement(resume, getNotExistElement(resume.getUuid()));
    }

    protected abstract void insertElement(Resume resume, SK searchKey);

    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        deleteElement(getExistElement(uuid));
    }

    protected abstract void deleteElement(SK searchKey);

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return getElement(getExistElement(uuid));
    }

    protected abstract Resume getElement(SK searchKey);

    private SK getExistElement(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExistElement(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistElement(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExistElement(searchKey)) {
            LOG.warning("Resume " + uuid + " already exist");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract SK getSearchKey(String uuid);

    protected abstract boolean isExistElement(SK searchKey);
}
