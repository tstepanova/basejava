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
    public Resume[] getAll() {
        return map.values().toArray(new Resume[map.size()]);
    }

    @Override
    protected int getIndex(String uuid) {
        return (map.get(uuid) == null ? -1 : 0);
    }

    @Override
    protected void updateElement(Resume resume, int index) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void insertElement(Resume resume, int index) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void fillDeletedElement(String uuid, int index) {
        map.remove(uuid);
    }

    @Override
    protected Resume getElement(String uuid, int index) {
        return map.get(uuid);
    }
}
