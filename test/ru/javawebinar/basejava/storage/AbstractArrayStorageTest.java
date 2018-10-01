package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractArrayStorageTest {
    private Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;
    private static final Resume[] RESUME_ARRAY;

    static {
        RESUME_1 = new Resume(UUID_1);
        RESUME_2 = new Resume(UUID_2);
        RESUME_3 = new Resume(UUID_3);
        RESUME_4 = new Resume(UUID_4);
		RESUME_ARRAY = new Resume[]{RESUME_1, RESUME_2, RESUME_3};
    }	
	
    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() throws Exception {
        assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        Resume resume = new Resume(UUID_2);
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistStorageException() throws Exception {
        storage.update(RESUME_4);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] methodResumeArr = storage.getAll();
        assertArrayEquals(RESUME_ARRAY, methodResumeArr);
    }

    @Test
    public void save() throws Exception {
        storage.save(RESUME_4);
        assertTrue(storage.size() == 4);
        assertEquals(RESUME_4, storage.get(UUID_4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistStorageException() throws Exception {
        storage.save(RESUME_2);
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
    public void delete() throws Exception {
        storage.delete(UUID_2);
        assertTrue(storage.size() == 2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistStorageException() throws Exception {
        storage.delete(UUID_4);
    }

    @Test
    public void get() throws Exception {
        Resume[] methodResumeArr = {storage.get(UUID_1), storage.get(UUID_2), storage.get(UUID_3)};
        assertArrayEquals(RESUME_ARRAY, methodResumeArr);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistStorageException() throws Exception {
        storage.get(UUID_4);
    }
}