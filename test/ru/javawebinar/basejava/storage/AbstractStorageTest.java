package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = new File("C:\\projects\\storage");

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
    protected static final List<Resume> RESUME_LIST;

    static {
        RESUME_1 = new Resume(UUID_1, "FULLNAME1");
        RESUME_2 = new Resume(UUID_2, "FULLNAME2");
        RESUME_3 = new Resume(UUID_3, "FULLNAME3");
        RESUME_4 = new Resume(UUID_4, "FULLNAME4");
        ResumeTestData.addInfo(RESUME_1);
        ResumeTestData.addInfo(RESUME_2);
        ResumeTestData.addInfo(RESUME_3);
        ResumeTestData.addInfo(RESUME_4);
        RESUME_ARRAY = new Resume[]{RESUME_1, RESUME_2, RESUME_3};
        RESUME_LIST = Arrays.asList(RESUME_ARRAY);

    }

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
        Resume resume = new Resume(UUID_2, "FULLNAME2");
        ResumeTestData.addInfo(resume);
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistStorageException() throws Exception {
        storage.update(RESUME_4);
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> list = storage.getAllSorted();
        assertEquals(3, list.size());
        assertEquals(RESUME_LIST, list);
    }

    @Test
    public void save() throws Exception {
        storage.save(RESUME_4);
        assertEquals(4, storage.size());
        assertEquals(RESUME_4, storage.get(UUID_4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistStorageException() throws Exception {
        storage.save(RESUME_2);
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_2);
        assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistStorageException() throws Exception {
        storage.delete(UUID_4);
    }

    @Test
    public void get() throws Exception {
        assertEquals(RESUME_1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistStorageException() throws Exception {
        storage.get(UUID_4);
    }
}