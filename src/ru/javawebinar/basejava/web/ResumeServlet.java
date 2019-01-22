package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        SqlStorage storage = (SqlStorage) Config.get().getStorage();
        //response.getWriter().write(uuid == null ? storage.getAllSorted().toString() : storage.get(uuid).toString());
        String htmlFile = "";
        if(uuid == null) {
            htmlFile = "<html>\n" +
                    "<head>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                    "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                    "    <title>Все резюме</title>\n" + "</head>\n" +
                    "<body>\n";
            for (Resume resume : storage.getAllSorted()){
                htmlFile = htmlFile + resumeToHTML(resume);
            }
            htmlFile = htmlFile +
                    "</body>\n" +
                    "</html>";
        } else {
            htmlFile = "<html>\n" +
                    "<head>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                    "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                    "    <title>Резюме " + storage.get(uuid).getFullName() + "</title>\n" + "</head>\n" +
                    "<body>\n" +
                    resumeToHTML(storage.get(uuid)) +
                    "</body>\n" +
                    "</html>";
        }
        response.getWriter().write(htmlFile);
    }

    private String resumeToHTML(Resume resume) {
        String res = "<section>\n" +
                "    <h1>" + resume.getFullName() + "</h1>\n" +
                "    <p>\n";
        Iterator<ContactType> itr1 = resume.getContacts().keySet().iterator();
        while (itr1.hasNext()) {
            ContactType key = itr1.next();
            res = res + key.getTitle() + " " + resume.getContacts().get(key).toString() + "<br/>\n";
        }
        res = res +
                "    <p>\n" +
                "    <hr>\n" +
                "    <table cellpadding=\"2\">\n";
        Iterator<SectionType> itr2 = resume.getSections().keySet().iterator();
        while (itr2.hasNext()) {
            SectionType key = itr2.next();
            res = res + sectionToString(key, resume.getSections().get(key));
        }
        res = res +
                "    </table>\n" +
                "</section>\n";
        return res;
    }

    private String sectionToString(SectionType type, AbstractSection section) {
        String res = "";
        if (section != null) {
            switch (type) {
                case OBJECTIVE:
                case PERSONAL:
                    res =
                            "        <tr>\n" +
                            "            <td colspan=\"2\"><h2><a name=\"type.name\">" + type.getTitle() + "</a></h2></td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "            <td colspan=\"2\">\n" + ((TextSection) section).getText() + "\n" +
                            "            </td>\n" +
                            "        </tr>\n";
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    res =
                            "        <tr>\n" +
                                    "            <td colspan=\"2\"><h2><a name=\"type.name\">" + type.getTitle() + "</a></h2></td>\n" +
                                    "        </tr>\n" +
                                    "        <tr>\n" +
                                    "            <td colspan=\"2\">\n" +
                                    "                <ul>\n";
                    List<String> sectionsList = ((ListSection) section).getList();
                    for (int i = 0; i < sectionsList.size(); i++) {
                        res = res +
                                "                    <li>" + sectionsList.get(i) + "\n" +
                                "                    </li>\n";
                    }
                    res = res +
                            "                </ul>\n" +
                            "            </td>\n" +
                            "        </tr>\n";
                    break;
            }
        }
        return res;
    }

}