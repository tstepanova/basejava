package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public int size() {
        int n = 0;
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory is not exist", directory.getAbsolutePath());
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    n++;
                }
            }
        }
        return n;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory is not exist", directory.getAbsolutePath());
        } else {
            for (File file : files) {
                deleteElement(file);
            }
        }
    }

    @Override
    protected void updateElement(Resume resume, File file) {
        try {
            doUpdateElement(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error write file", file.getName(), e);
        }
    }

    protected abstract void doUpdateElement(Resume resume, OutputStream os) throws IOException;;

    @Override
    protected List<Resume> getAllSortedElements() {
        List<Resume> list = null;
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory is not exist", directory.getAbsolutePath());
        } else {
            list = new ArrayList<>(files.length);
            for (File file : files) {
                if (file.isFile()) {
                    list.add(getElement(file));
                }
            }
        }
        return list;
    }

    @Override
    protected void insertElement(Resume resume, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Error create file", file.getName(), e);
        }
		updateElement(resume, file);
    }


    @Override
    protected void deleteElement(File file) {
        if (!file.delete()) {
            throw new StorageException("Error delete file", file.getName());
        }
    }

    @Override
    protected Resume getElement(File file) {
        try {
            return doGetElement(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error read file", file.getName(), e);
        }
    }

    protected abstract Resume doGetElement(InputStream is) throws IOException;

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExistElement(File file) {
        return file.exists() && file.isFile();
    }
}
