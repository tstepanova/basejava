import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int cnt = 0;

    void clear() {
        Arrays.fill(storage, 0, cnt, null);
        cnt = 0;
    }

    void save(Resume r) {
        storage[cnt] = r;
        cnt++;
    }

    Resume get(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return null;
        }
        for (int i = 0; i < cnt; i++) {
            if (storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        if (uuid != null && !uuid.isEmpty()) {
            for (int i = 0; i < cnt; i++) {
                if (storage[i].uuid.equals(uuid)) {
                    System.arraycopy(storage, i + 1, storage, i, cnt - 1 - i);
                    cnt--;
                    break;
                }
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, cnt);
    }

    int size() {
        return cnt;
    }
}
