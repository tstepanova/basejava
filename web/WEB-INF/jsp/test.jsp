<%@ page import="ru.javawebinar.basejava.JspTest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="jspptest" type="ru.javawebinar.basejava.JspTest" scope="request"/>
<html>
<body>
<section>
    <form method="post" action="test" enctype="application/x-www-form-urlencoded">
        <dl>
            <dt>${jspptest.text}</dt>
            <dt>Поле ввода:</dt>
            <dd>
                <input type="text" name="myFieldName" size=100 value="${jspptest.text}">
            </dd>
        </dl>
        <button type="submit">Сохранить</button>
    </form>
</section>
</body>
</html>