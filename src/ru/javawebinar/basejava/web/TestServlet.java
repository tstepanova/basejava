package ru.javawebinar.basejava.web;

import org.apache.commons.lang3.StringEscapeUtils;
import ru.javawebinar.basejava.JspTest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String text = "value \"from\" get";
        JspTest jspptest = new JspTest();
        jspptest.setText(StringEscapeUtils.escapeHtml4(text));
        //jspptest.setText(text);
        request.setAttribute("jspptest", jspptest);
        request.getRequestDispatcher("/WEB-INF/jsp/test.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String text = request.getParameter("myFieldName");
        System.out.println("text=" + text);
        JspTest jspptest = new JspTest();
        jspptest.setText(StringEscapeUtils.escapeHtml4(text + "!!!"));
        //jspptest.setText(text);
        request.setAttribute("jspptest", jspptest);
        request.getRequestDispatcher("/WEB-INF/jsp/test.jsp").forward(request, response);
    }
}
