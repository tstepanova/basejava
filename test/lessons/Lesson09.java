package lessons;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Lesson09 {

    @Test
    public void NIOtest() throws Exception {

        //create
        Path path = Paths.get("C:\\tmp");
        if (!Files.exists(path)) Files.createDirectories(path);

        Path file = Paths.get("C:\\tmp\\1.txt");
        if (!Files.exists(file)) Files.createFile(file);
        if (!Files.exists(Paths.get("C:\\tmp\\2.txt"))) Files.createFile(Paths.get("C:\\tmp\\2.txt"));
        if (!Files.exists(path.resolve("3.txt"))) Files.createFile(path.resolve("3.txt"));

        System.out.println("path: " + path.toString());
        System.out.println("path is directory: " + Files.isDirectory(path));
        System.out.println("file is directory: " + Files.isDirectory(file));
        System.out.println("file is file: " + Files.isRegularFile(file));

        //print
        Stream<Path> stream = Files.list(path); //Files.walk(path);
        stream.forEach(System.out::println);

        //count
        System.out.println("Count files is directory: " + Files.list(path).count());

        //list
        List<Path> list = new ArrayList<>(10);
        Files.list(path).limit(10).forEach(fileName -> {list.add(fileName);});
        System.out.println("list: " + list.get(0));

        //delete
        if (Files.exists(file)) Files.delete(file); // del 1.txt
        if (Files.exists(path)) {
            Files.list(path).map(Path::toFile).forEach(File::delete); //Files.walk(path).map(Path::toFile).forEach(File::delete); //del 2.txt & 3.txt
            Files.delete(path); // del tmp
        }
    }
}
