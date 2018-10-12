package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> list = new ArrayList<Resume>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    protected void updateElement(Resume resume, Object index) {
        list.set((int) index, resume);
    }

    @Override
    public Resume[] getAll() {
        return list.toArray(new Resume[list.size()]);
    }

    @Override
    protected void insertElement(Resume resume, Object index) {
        list.add(resume);
    }

    @Override
    protected void deleteElement(String uuid, Object index) {
        list.set((int) index, list.get(list.size() - 1));
        list.remove(list.size() - 1);
    }

    @Override
    protected Resume getElement(String uuid, Object index) {
        return list.get((int) index);
    }

    @Override
    protected Object getIndex(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (uuid.equals(list.get(i).getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExistElement(Object index) {
        return ((int) index >= 0);
    }
}
