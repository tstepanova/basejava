package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private Map<String, Resume> map = new HashMap<String, Resume>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    protected void updateElement(Resume resume, Object index) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    public Resume[] getAll() {
        return map.values().toArray(new Resume[map.size()]);
    }

    @Override
    protected void insertElement(Resume resume, Object index) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteElement(String uuid, Object index) {
        map.remove(uuid);
    }

    @Override
    protected Resume getElement(String uuid, Object index) {
        return map.get(uuid);
    }

    @Override
    protected Object getIndex(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExistElement(Object uuid) {
        return map.containsKey((String) uuid);
    }
}

