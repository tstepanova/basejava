package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
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
    public void clear() {
        try {
            Files.list(directory).forEach(this::deleteElement);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Directory read error", null);
        }
        return list.length;
    }

    @Override
    protected void updateElement(Resume resume, Path path) {
        try {
            doUpdateElement(resume, new BufferedOutputStream(new PathOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", resume.getUuid(), e);
        }
    }

    protected abstract void doUpdateElement(Resume resume, OutputStream os) throws IOException;

    @Override
    protected List<Resume> getAllSortedElements() {
        Path[] Paths = directory.listPaths();
        if (Paths == null) {
            throw new StorageException("Directory read error", null);
        }
        List<Resume> list = new ArrayList<>(Paths.length);
        for (Path path : Paths) {
            list.add(getElement(path));
        }
        return list;
    }

    @Override
    protected void insertElement(Resume resume, Path path) {
        try {
            path.createNewPath();
        } catch (IOException e) {
            throw new StorageException("Couldn't create Path " + path.getAbsolutePath(), path.getName(), e);
        }
        updateElement(resume, path);
    }

    @Override
    protected void deleteElement(Path path) {
        if (!path.delete()) {
            throw new StorageException("Path delete error", path.getName());
        }
    }

    @Override
    protected Resume getElement(Path path) {
        try {
            return doGetElement(new BufferedInputStream(new PathInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.getName(), e);
        }
    }

    protected abstract Resume doGetElement(InputStream is) throws IOException;

    @Override
    protected Path getSearchKey(String uuid) {
        return new Path(directory, uuid);
    }

    @Override
    protected boolean isExistElement(Path path) {
        return path.exists();
    }
}
