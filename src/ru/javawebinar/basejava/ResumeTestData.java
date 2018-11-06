package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ResumeTestData {
    private static final Logger LOG = Logger.getLogger(ResumeTestData.class.getName());

    public static void main(String[] args) {
        Resume resume = new Resume("Григорий Кислин");

        resume.addContact(ContactType.PHONE, new Link("+7(921) 855-0482"));
        resume.addContact(ContactType.SKYPE, new Link("grigory.kislin", "skype:grigory.kislin"));
        resume.addContact(ContactType.EMAIL, new Link("gkislin@yandex.ru", "mailto:gkislin@yandex.ru"));
        resume.addContact(ContactType.LINKEDINPROFILE, new Link("Профиль LinkedIn", "https://www.linkedin.com/in/gkislin"));
        resume.addContact(ContactType.GITHUBPROFILE, new Link("Профиль GitHub", "https://github.com/gkislin"));
        resume.addContact(ContactType.STACKOVERFLOWPROFILE, new Link("Профиль Stackoverflow", "https://stackoverflow.com/users/548473/gkislin"));
        resume.addContact(ContactType.HOMEPAGE, new Link("Домашняя страница", "http://gkislin.ru/"));

        resume.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        resume.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        List<String> achievementList = new ArrayList<>();
        achievementList.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        achievementList.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        achievementList.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        achievementList.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        achievementList.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        achievementList.add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");
        resume.addSection(SectionType.ACHIEVEMENT, new ListSection(achievementList));

        List<String> qualificationsList = new ArrayList<>();
        qualificationsList.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        qualificationsList.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        qualificationsList.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,");
        qualificationsList.add("MySQL, SQLite, MS SQL, HSQLDB");
        qualificationsList.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy,");
        qualificationsList.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts,");
        qualificationsList.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).");
        qualificationsList.add("Python: Django.");
        qualificationsList.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        qualificationsList.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        qualificationsList.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.");
        qualificationsList.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix,");
        qualificationsList.add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer.");
        qualificationsList.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования");
        qualificationsList.add("Родной русский, английский \"upper intermediate\"");
        resume.addSection(SectionType.QUALIFICATIONS, new ListSection(qualificationsList));

        OrganizationSection experienceSection = new OrganizationSection();
        experienceSection.setList(new Link("Java Online Projects", "http://javaops.ru/"), "10/2013", null, "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок.");
        experienceSection.setList(new Link("Wrike", "https://www.wrike.com/"), "10/2014", "01/2016", "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO.");
        experienceSection.setList(new Link("RIT Center"), "04/2012", "10/2014", "Java архитектор", "Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python");
        experienceSection.setList(new Link("Luxoft (Deutsche Bank)", "http://www.luxoft.ru/"), "12/2010", "04/2012", "Ведущий программист", "Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5.");
        experienceSection.setList(new Link("Yota", "https://www.yota.ru/"), "06/2008", "12/2010", "Ведущий специалист", "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)");
        experienceSection.setList(new Link("Enkata", "http://enkata.com/"), "03/2007", "06/2008", "Разработчик ПО", "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining).");
        experienceSection.setList(new Link("Siemens AG", "https://www.siemens.com/ru/ru/home.html"), "01/2005", "02/2007", "Разработчик ПО", "Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix).");
        experienceSection.setList(new Link("Alcatel", "http://www.alcatel.ru/"), "09/1997", "01/2005", "Инженер по аппаратному и программному тестированию", "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM).");
        resume.addSection(SectionType.EXPERIENCE, experienceSection);

        OrganizationSection educationSection = new OrganizationSection();
        educationSection.setList(new Link("Coursera", "https://www.coursera.org/course/progfun"), "03/2013", "05/2013", "\"Functional Programming Principles in Scala\" by Martin Odersky", null);
        educationSection.setList(new Link("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366"), "03/2011", "04/2011", "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"", "");
        educationSection.setList(new Link("Siemens AG", "http://www.siemens.ru/"), "01/2005", "04/2005", "3 месяца обучения мобильным IN сетям (Берлин)", "");
        educationSection.setList(new Link("Alcatel", "http://www.alcatel.ru/"), "09/1997", "03/1998", "6 месяцев обучения цифровым телефонным сетям (Москва)", "");
        educationSection.setList(new Link("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "http://www.ifmo.ru/"), "09/1993", "07/1996", "Аспирантура (программист С, С++)", "");
        educationSection.setList(null, "09/1987", "07/1993", "Инженер (программист Fortran, C)", "");
        educationSection.setList(new Link("Заочная физико-техническая школа при МФТИ", "http://www.school.mipt.ru/"), "09/1984", "06/1987", "Закончил с отличием", "");
        resume.addSection(SectionType.EDUCATION, educationSection);

        LOG.info('\n' + resume.toString());
    }
}
