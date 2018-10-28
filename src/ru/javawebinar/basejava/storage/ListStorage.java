package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private List<Resume> list = new ArrayList<Resume>();

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    @Override
    protected void updateElement(Resume resume, Integer index) {
        list.set((int) index, resume);
    }

    @Override
    public List<Resume> getAllSortedElements() {
        return new ArrayList<>(list);
    }

    @Override
    protected void insertElement(Resume resume, Integer index) {
        list.add(resume);
    }

    @Override
    protected void deleteElement(Integer index) {
        list.remove((int) index);
    }

    @Override
    protected Resume getElement(Integer index) {
        return list.get((int) index);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (uuid.equals(list.get(i).getUuid())) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected boolean isExistElement(Integer index) {
        return (index != null);
    }
}
