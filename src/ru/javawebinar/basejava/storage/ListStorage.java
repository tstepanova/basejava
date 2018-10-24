package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> list = new ArrayList<Resume>();

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    @Override
    protected void updateElement(Resume resume, Object index) {
        list.set((int) index, resume);
    }

    public Resume[] getAll() {
        return list.toArray(new Resume[list.size()]);
    }

    @Override
    public List<Resume> getAllSortedElements() {
        return new ArrayList<>(list);
    }

    @Override
    protected void insertElement(Resume resume, Object index) {
        list.add(resume);
    }

    @Override
    protected void deleteElement(Object index) {
        list.remove((int) index);
    }

    @Override
    protected Resume getElement(Object index) {
        return list.get((int) index);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (uuid.equals(list.get(i).getUuid())) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExistElement(Object index) {
        return (index != null);
    }
}
