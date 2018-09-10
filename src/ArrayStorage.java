import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (size() < storage.length) {
            if (indexResumeInStorage(r.uuid) == -1) {
                storage[size] = r;
                size++;
            } else {
                System.out.println("ERROR: resume already exists");
            }
        } else {
            System.out.println("ERROR: failed save resume");
        }
    }

    public void update(Resume r) {
        int index = indexResumeInStorage(r.uuid);
        if (index > -1) {
            storage[index] = r;
        } else {
            System.out.println("ERROR: failed update resume");
        }
    }

    public Resume get(String uuid) {
        int index = indexResumeInStorage(uuid);
        if (index > -1) {
            return storage[index];
        } else {
            System.out.println("ERROR: failed get resume");
            return null;
        }
    }

    public void delete(String uuid) {
        int index = indexResumeInStorage(uuid);
        if (index > -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("ERROR: failed delete resume");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] result = Arrays.copyOf(storage, size);
        return result;
    }

    public int size() {
        return size;
    }

    /**
     * @return int (Resume index in storage), if the resume is in the repository,
     * otherwise it returns -1
     */
    private int indexResumeInStorage(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
