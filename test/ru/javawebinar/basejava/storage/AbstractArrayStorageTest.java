package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    protected AbstractArrayStorage arrayStorage;

    AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveStorageException() throws Exception {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        storage.save(new Resume());
    }

    @Test
    public void getAllTest() {
        arrayStorage.clear();
        arrayStorage.save(RESUME_1);
        arrayStorage.save(RESUME_2);
        arrayStorage.save(RESUME_3);
        Resume[] methodResumeArr = arrayStorage.getAll();
        Arrays.sort(RESUME_ARRAY, RESUME_UUID_COMPARATOR);
        Arrays.sort(methodResumeArr, RESUME_UUID_COMPARATOR);
        assertArrayEquals(RESUME_ARRAY, methodResumeArr);
    }
}