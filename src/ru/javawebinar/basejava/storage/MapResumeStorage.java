package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
    private Map<String, Resume> map = new HashMap<String, Resume>();

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    @Override
    protected void updateElement(Resume resume, Object searchKey) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    public List<Resume> getAllSortedElements() {
        return new ArrayList<Resume>(map.values());
    }

    @Override
    protected void insertElement(Resume resume, Object searchKey) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteElement(Object resume) {
        map.remove(((Resume) resume).getUuid());
    }

    @Override
    protected Resume getElement(Object resume) {
        return (Resume) resume;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    protected boolean isExistElement(Object resume) {
        return resume != null;
    }
}

