package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {
    protected Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final Resume RESUME_1;
    protected static final Resume RESUME_2;
    protected static final Resume RESUME_3;
    protected static final Resume RESUME_4;
    protected static final Resume[] RESUME_ARRAY;

    static {
        RESUME_1 = new Resume(UUID_1);
        RESUME_2 = new Resume(UUID_2);
        RESUME_3 = new Resume(UUID_3);
        RESUME_4 = new Resume(UUID_4);
        RESUME_ARRAY = new Resume[]{RESUME_1, RESUME_3, RESUME_2};
    }

    private static final Comparator<Resume> RESUME_COMPARATOR = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());

    AbstractStorageTest(Storage storage) {
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
        Arrays.sort(RESUME_ARRAY, RESUME_COMPARATOR);
        Arrays.sort(methodResumeArr, RESUME_COMPARATOR);
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
        Resume[] methodResumeArr = {storage.get(UUID_1), storage.get(UUID_3), storage.get(UUID_2)};
        Arrays.sort(RESUME_ARRAY, RESUME_COMPARATOR);
        Arrays.sort(methodResumeArr, RESUME_COMPARATOR);
        assertArrayEquals(RESUME_ARRAY, methodResumeArr);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistStorageException() throws Exception {
        storage.get(UUID_4);
    }
}