package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        response.getWriter().write(
                "<html>\n" +
                    "<head>\n" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                        "<link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                        "<title>Список резюме</title>\n" +
                    "</head>\n" +
                    "<header>Список резюме</header>" +
                    "<br\">" +
                    "<body>\n" +
                        "<section>\n" +
                            "<table border=\"1\">\n" +
                                "<tr>\n" +
                                    "<th>ФИО</th>\n" +
                                    "<th>ID</th>\n" +
                                "</tr>\n" +
                                (uuid == null ? resumeToHTML(storage.getAllSorted()) : resumeToHTML(storage.get(uuid))) +
                            "</table>\n" +
                        "</section>\n" +
                    "</body>\n" +
                "</html>\n");
    }

    private String resumeToHTML(List<Resume> resumes) {
        String res = "";
        for (Resume resume : resumes){
            res += resumeToHTML(resume);
        }
        return res;
    }

    private String resumeToHTML(Resume resume) {
        return "<tr>\n" +
                    "<td>" + resume.getFullName() + "</td>\n" +
                    "<td>" + resume.getUuid() + "</td>\n" +
                "</tr>\n";
    }

}