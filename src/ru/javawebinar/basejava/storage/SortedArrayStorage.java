package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        //Если элемент не найден, то возвращается отрицательное число, означающее индекс,
        //с которым элемент был бы вставлен в массив в заданном порядке, с обратным знаком.
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void save0(int index, Resume r) {
        System.arraycopy(storage, -1 * index - 1, storage, -1 * index, size - (-1 * index - 1));
        storage[-1 * index - 1] = r;
    }

    @Override
    protected void delete0(int index) {
        System.arraycopy(storage, index + 1, storage, index, (size - 1) - index);
        storage[size - 1] = null;
    }
}
