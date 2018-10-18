package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class MapResumeStorageTest extends AbstractStorageTest {

    public MapResumeStorageTest() {
        super(new MapResumeStorage());
    }

    @Test
    public void getAll() {
        MapResumeStorage mapResumeStorage = new MapResumeStorage();
        mapResumeStorage.save(RESUME_1);
        mapResumeStorage.save(RESUME_2);
        mapResumeStorage.save(RESUME_3);
        Resume[] methodResumeArr = mapResumeStorage.getAll();
        Arrays.sort(RESUME_ARRAY, RESUME_UUID_COMPARATOR);
        Arrays.sort(methodResumeArr, RESUME_UUID_COMPARATOR);
        assertArrayEquals(RESUME_ARRAY, methodResumeArr);
    }
}