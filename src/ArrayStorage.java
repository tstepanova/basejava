import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        Arrays.fill(storage, null);
    }

    void save(Resume r) {
        for(int i=0;i<storage.length;i++){
            if(storage[i] == null) {
                storage[i]=r;
                break;
            }
        }
    }

    Resume get(String uuid) {
        if(uuid == null || uuid.isEmpty()) return null;
        for(int i=0;i<storage.length;i++){
            if(storage[i] == null) return null;
            if(storage[i].uuid.equals(uuid)) return storage[i];
        }
        return null;
    }

    void delete(String uuid) {
        if(uuid != null && !uuid.isEmpty()){
            for(int i=0;i<storage.length;i++){
                if(storage[i] == null) break;
                if(storage[i].uuid.equals(uuid)) {
                    for(int j=i;j<storage.length-1;j++){
                        storage[j] = storage[j+1];
                        if(storage[j] == null) break;
                        if(j==storage.length-1) storage[j+1]=null;
                    }
                    break;
                }
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage,size());
    }

    int size() {
        for(int i=0;i<storage.length;i++){
            if(storage[i] == null) return i;
        }
        return 0;
    }
}
