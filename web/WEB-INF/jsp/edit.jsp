<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()" name="backWithoutSaving">Отменить</button>
        <hr>
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=100 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=100 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSections().get(type)}"/>
            <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
            <h2><a>${type.title}</a></h2>
            <c:choose>
                <c:when test="${type=='OBJECTIVE'}">
                    <input type='text' name='${type}' size=100 value='<%=section%>'>
                </c:when>
                <c:when test="${type=='PERSONAL'}">
                    <textarea name='${type}' cols=97 rows=5><%=section%></textarea>
                </c:when>
                <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                    <textarea name='${type}' cols=97
                              rows=5><%=String.join("\n", ((ListSection) section).getList())%></textarea>
                </c:when>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getList()%>"
                               varStatus="counter">
                        <dl>
                            <dt>
                                <c:choose>
                                    <c:when test="${type=='EXPERIENCE'}">
                                        Организация:
                                    </c:when>
                                    <c:when test="${type=='EDUCATION'}">
                                        Учебное заведение:
                                    </c:when>
                                </c:choose>
                            </dt>
                            <dd><input type="text" name='${type}' size=100 value="${org.sectionHeader.text}"></dd>
                        </dl>
                        <dl>
                            <dt>Сайт:</dt>
                            <dd><input type="text" name='${type}url' size=100 value="${org.sectionHeader.url}"></dd>
                            </dd>
                        </dl>
                        <br>
                        <div style="margin-left: 30px">
                            <c:forEach var="pos" items="${org.positions}">
                                <jsp:useBean id="pos" type="ru.javawebinar.basejava.model.Organization.Position"/>
                                <dl>
                                    <dt>Начало периода:</dt>
                                    <dd>
                                        <input type="text" name="${type}${counter.index}startDate" size=10 value="<%=DateUtil.of(pos.startDate)%>" placeholder="<%=pos.PATTERN%>">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Окончание периода:</dt>
                                    <dd>
                                        <input type="text" name="${type}${counter.index}endDate" size=10 value="<%=DateUtil.of(pos.endDate)%>" placeholder="<%=pos.PATTERN%>">
                                </dl>
                                <dl>
                                    <dt>
                                        <c:choose>
                                            <c:when test="${type=='EXPERIENCE'}">
                                                Должность:
                                            </c:when>
                                            <c:when test="${type=='EDUCATION'}">
                                                Специальность/курс:
                                            </c:when>
                                        </c:choose>
                                    </dt>
                                    <dd><input type="text" name='${type}${counter.index}title' size=95 value="${pos.textHeader}">
                                </dl>
                                <c:choose>
                                    <c:when test="${type=='EXPERIENCE'}">
                                        <dl>
                                            <dt>Служебные обяз.:</dt>
                                            <dd><textarea name="${type}${counter.index}description" rows=5 cols=93>${StringEscapeUtils.escapeHtml4(pos.text)}</textarea></dd>
                                        </dl>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()" name="backWithoutSaving">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

