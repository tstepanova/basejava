package learning;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.util.HtmlUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class AllTest {

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
        Files.list(path).limit(10).forEach(fileName -> {
            list.add(fileName);
        });
        System.out.println("list: " + list.get(0));

        //delete
        if (Files.exists(file)) Files.delete(file); // del 1.txt
        if (Files.exists(path)) {
            Files.list(path).map(Path::toFile).forEach(File::delete); //Files.walk(path).map(Path::toFile).forEach(File::delete); //del 2.txt & 3.txt
            Files.delete(path); // del tmp
        }
    }

    @Test
    public void DateTimeParseTest() throws Exception {

        String localDate = "10/2013";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yyyy");
        YearMonth ym = YearMonth.parse(localDate, dtf);
        LocalDate ld = ym.atDay(1);
        System.out.println(ld);

    }

    @Test
    public void splitTest() throws Exception {
        String newline = System.getProperty("line.separator");
        String text = "[\r\n" + "section1\r\n" + "a1\r\n" + "b1\r\n" + "c1\r\n" + "]\r\n" + "[\r\n" + "section2\r\n" + "a2\r\n" + "b2\r\n" + "c2\r\n" + "]\r\n" + "[\r\n" + "section3\r\n" + "a3\r\n" + "b3\r\n" + "c3\r\n" + "]\r\n";
        String[] arr = text.split("(\\[|\\])");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(i + ") " + arr[i]);
        }

    }

    @Test
    public void stringEscapeUtilsTest() {
        String str = "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"";
        String str_escape = StringEscapeUtils.escapeHtml4(str);
        String str_unescape = StringEscapeUtils.unescapeHtml4(str_escape);
        System.out.println(str_escape);
        System.out.println(str_unescape);
    }

    @Test
    public void escapeResume() {
        Resume resume = new Resume(UUID.randomUUID().toString(), "FULLNAME");
        ResumeTestData.addInfo(resume);
        System.out.println("Before:\n" + resume.toString() + "\n");
        System.out.println("After:\n" + HtmlUtil.escapeHtml(resume).toString() + "\n");
    }
}
