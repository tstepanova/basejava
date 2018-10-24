package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
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
    protected static final List<Resume> RESUME_LIST;

    static {
        RESUME_1 = new Resume(UUID_1, "FULLNAME2");
        RESUME_2 = new Resume(UUID_2, "FULLNAME1");
        RESUME_3 = new Resume(UUID_3, "FULLNAME2");
        RESUME_4 = new Resume(UUID_4, "FULLNAME4");
        RESUME_ARRAY = new Resume[]{RESUME_1, RESUME_3, RESUME_2};
        RESUME_LIST = Arrays.asList(RESUME_ARRAY);

    }

    AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    protected static final Comparator<Resume> RESUME_UUID_COMPARATOR = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());
    protected static final Comparator<Resume> RESUME_FULLNAME_COMPARATOR = (o1, o2) -> o1.getFullName().compareTo(o2.getFullName());

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
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistStorageException() throws Exception {
        storage.update(RESUME_4);
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> methodResumeList = storage.getAllSorted();
        RESUME_LIST.sort(RESUME_FULLNAME_COMPARATOR);
        assertThat(RESUME_LIST, is(methodResumeList));
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
        Arrays.sort(RESUME_ARRAY, RESUME_UUID_COMPARATOR);
        Arrays.sort(methodResumeArr, RESUME_UUID_COMPARATOR);
        assertArrayEquals(RESUME_ARRAY, methodResumeArr);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistStorageException() throws Exception {
        storage.get(UUID_4);
    }
}