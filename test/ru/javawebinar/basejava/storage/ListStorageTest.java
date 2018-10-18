package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Test
    public void getAll() {
        ListStorage listStorage = new ListStorage();
        listStorage.save(RESUME_1);
        listStorage.save(RESUME_2);
        listStorage.save(RESUME_3);
        Resume[] methodResumeArr = listStorage.getAll();
        Arrays.sort(RESUME_ARRAY, RESUME_UUID_COMPARATOR);
        Arrays.sort(methodResumeArr, RESUME_UUID_COMPARATOR);
        assertArrayEquals(RESUME_ARRAY, methodResumeArr);
    }
}