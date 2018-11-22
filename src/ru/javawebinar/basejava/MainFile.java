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

        printTreeFiles("./src/ru/javawebinar/basejava");

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    static String tab = "";

    public static void printTreeFiles(String pathName) {
        System.out.println(tab + pathName + ":");
        tab = tab + "    ";
        File dir = new File(pathName);
        String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                dir = new File(pathName + "/" + name);
                if (dir.isDirectory()) {
                    printTreeFiles(pathName + "/" + name);
                } else {
                    System.out.println(tab + "- " + name);
                }
            }
        }
        tab = "    ";
    }
}
