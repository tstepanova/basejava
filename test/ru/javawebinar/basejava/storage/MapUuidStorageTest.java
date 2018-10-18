package ru.javawebinar.basejava.storage;

import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public class MapUuidStorageTest extends AbstractStorageTest {

    public MapUuidStorageTest() {
        super(new MapUuidStorage());
    }

    @Test
    public void getAll() {
        MapUuidStorage mapUuidStorage = new MapUuidStorage();
        mapUuidStorage.save(RESUME_1);
        mapUuidStorage.save(RESUME_2);
        mapUuidStorage.save(RESUME_3);
        Resume[] methodResumeArr = mapUuidStorage.getAll();
        Arrays.sort(RESUME_ARRAY, RESUME_UUID_COMPARATOR);
        Arrays.sort(methodResumeArr, RESUME_UUID_COMPARATOR);
        assertArrayEquals(RESUME_ARRAY, methodResumeArr);
    }
}