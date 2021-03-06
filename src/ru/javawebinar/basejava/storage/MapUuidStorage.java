package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage<String> {
    private Map<String, Resume> map = new HashMap<>();

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    @Override
    protected void updateElement(Resume resume, String uuid) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    public List<Resume> getAllSortedElements() {
        return new ArrayList<Resume>(map.values());
    }

    @Override
    protected void insertElement(Resume resume, String uuid) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteElement(String uuid) {
        map.remove(uuid);
    }

    @Override
    protected Resume getElement(String uuid) {
        return map.get(uuid);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExistElement(String uuid) {
        return map.containsKey(uuid);
    }
}

