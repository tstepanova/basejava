<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="static ru.javawebinar.basejava.model.Organization.Position.PATTERN" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
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
    <h2>${resume.fullName}&nbsp;
        <a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a>
        <a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a>
    </h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <p>
    <hr>
    <table cellpadding="2">
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.AbstractSection>"/>
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
            <tr>
                <td colspan="2"><h2>${type.title}</h2></td>
            </tr>
            <c:choose>
                <c:when test="${type=='OBJECTIVE'}">
                    <tr>
                        <td colspan="2">
                            <h3><%=((TextSection) section).getText()%></h3>
                        </td>
                    </tr>
                </c:when>
                <c:when test="${type=='PERSONAL'}">
                    <tr>
                        <td colspan="2">
                            <%=((TextSection) section).getText()%>
                        </td>
                    </tr>
                </c:when>
                <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                    <tr>
                        <td colspan="2">
                            <ul>
                                <c:forEach var="list" items="<%=((ListSection) section).getList()%>">
                                    <li>${list}</li>
                                </c:forEach>
                            </ul>
                        </td>
                    </tr>
                </c:when>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <c:forEach var="organization" items="<%=((OrganizationSection) section).getList()%>">
                        <tr>
                            <td colspan="2">
                                <c:choose>
                                    <c:when test="${empty organization.sectionHeader.url}">
                                        <h3>${organization.sectionHeader.text}</h3>
                                    </c:when>
                                    <c:otherwise>
                                        <h3><a href="${organization.sectionHeader.url}">${organization.sectionHeader.text}</a></h3>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:forEach var="position" items="${organization.positions}">
                            <jsp:useBean id="position" type="ru.javawebinar.basejava.model.Organization.Position"/>
                            <tr>
                                <td width="15%" style="vertical-align: top">
                                    <%=DateUtil.of(position)%>
                                </td>
                                <td><b>${position.textHeader}</b><br>${position.text}</td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
    </table>
    <hr>
    <button onclick="window.history.back()">Назад</button>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

