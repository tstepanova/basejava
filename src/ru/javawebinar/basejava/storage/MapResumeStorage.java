package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
    private Map<String, Resume> map = new HashMap<>();

    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    @Override
    protected void updateElement(Resume resume, Resume searchKey) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    public List<Resume> getAllSortedElements() {
        return new ArrayList<Resume>(map.values());
    }

    @Override
    protected void insertElement(Resume resume, Resume searchKey) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteElement(Resume resume) {
        map.remove(resume.getUuid());
    }

    @Override
    protected Resume getElement(Resume resume) {
        return resume;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    protected boolean isExistElement(Resume resume) {
        return resume != null;
    }
}

