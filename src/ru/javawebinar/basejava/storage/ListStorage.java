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
    public Resume[] getAll() {
        return list.toArray(new Resume[list.size()]);
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (uuid.equals(list.get(i).getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void updateElement(Resume resume, int index) {
        list.set(index, resume);
    }

    @Override
    protected void insertElement(Resume resume, int index) {
        list.add(resume);
    }

    @Override
    protected void fillDeletedElement(String uuid, int index) {
        list.set(index, list.get(list.size() - 1));
        list.remove(list.size() - 1);
    }

    @Override
    protected Resume getElement(String uuid, int index) {
        return list.get(index);
    }
}
