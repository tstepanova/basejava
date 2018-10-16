package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage {
    private Map<String, Resume> map = new HashMap<String, Resume>();

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    @Override
    protected void updateElement(Resume resume, Object uuid) {
        map.put(resume.getUuid(), resume);
    }

    public Resume[] getAll() {
        return map.values().toArray(new Resume[map.size()]);
    }

    @Override
    protected void insertElement(Resume resume, Object uuid) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteElement(Object uuid) {
        map.remove(uuid);
    }

    @Override
    protected Resume getElement(Object uuid) {
        return map.get(uuid);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExistElement(Object uuid) {
        return map.containsKey((String) uuid);
    }
}

