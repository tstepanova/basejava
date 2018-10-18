package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
    private Map<Resume, Resume> map = new HashMap<Resume, Resume>();

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    @Override
    protected void updateElement(Resume resume, Object uuid) {
        map.put(resume, resume);
    }

    public Resume[] getAll() {
        return map.values().toArray(new Resume[map.size()]);
    }

    @Override
    public List<Resume> getAllSortedElements() {
        return new ArrayList<Resume>(map.values());
    }

    @Override
    protected void insertElement(Resume resume, Object uuid) {
        map.put(resume, resume);
    }

    @Override
    protected void deleteElement(Object resume) {
        map.remove(resume);
    }

    @Override
    protected Resume getElement(Object resume) {
        return map.get(resume);
    }

    @Override
    protected Resume getSearchKey(String uuid) {

        for (Map.Entry<Resume, Resume> pair : map.entrySet()) {
            if (pair.getKey().getUuid().equals(uuid)) return pair.getKey();
        }

        return null;
    }

    @Override
    protected boolean isExistElement(Object resume) {
        return map.containsKey(resume);
    }
}

