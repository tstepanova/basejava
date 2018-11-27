package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private Path directory;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Error read directory", directory.toString(), e);
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::deleteElement);
        } catch (IOException e) {
            throw new StorageException("Error delete directory", directory.toString());
        }
    }

    @Override
    protected void updateElement(Resume resume, Path file) {
        try {
            doUpdateElement(resume, Files.newOutputStream(file));
        } catch (IOException e) {
            throw new StorageException("Error write file", file.toString(), e);
        }
    }

    protected abstract void doUpdateElement(Resume resume, OutputStream os) throws IOException;


    @Override
    protected List<Resume> getAllSortedElements() {
        List<Resume> list = new ArrayList<>(size());
        try {
            Files.list(directory).filter(Files::isRegularFile).sorted().forEach(file -> {
                list.add(getElement(file));
            });
        } catch (IOException e) {
            throw new StorageException("Error read directory", directory.toString(), e);
        }
        return list;
    }

    @Override
    protected void insertElement(Resume resume, Path file) {
        try {
            if (!Files.exists(file)) Files.createFile(file);
        } catch (IOException e) {
            throw new StorageException("Error create file", file.toString(), e);
        }
        updateElement(resume, file);
    }

    @Override
    protected void deleteElement(Path file) {
        if (Files.exists(file)) {
            try {
                Files.delete(file);
            } catch (IOException e) {
                throw new StorageException("Error delete file", file.toString(), e);
            }
        }
    }

    @Override
    protected Resume getElement(Path file) {
        try {
            return doGetElement(Files.newInputStream(file));
        } catch (IOException e) {
            throw new StorageException("Error read file", file.toString(), e);
        }
    }

    protected abstract Resume doGetElement(InputStream is) throws IOException;

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExistElement(Path file) {
        return Files.exists(file);
    }
}
