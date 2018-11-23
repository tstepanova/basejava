package ru.javawebinar.basejava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * gkislin
 * 21.07.2016
 */
public class MainFile {
    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        printTreeFiles(new File("./src/ru/javawebinar/basejava"));

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void printTreeFiles(File dir) {
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                File subdir = new File(dir.getAbsolutePath() + "/" + name);
                if (subdir.isDirectory()) {
                    printTreeFiles(subdir);
                } else {
                    System.out.println(name);
                }
            }
        }
    }
}
