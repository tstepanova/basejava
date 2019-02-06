package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;
import ru.javawebinar.basejava.util.HtmlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("backWithoutSaving") == null) {
            String uuid = request.getParameter("uuid");
            String fullName = request.getParameter("fullName");

            Resume resume;
            if (uuid == null || uuid.isEmpty() || uuid.length() == 0) {
                resume = new Resume(fullName);
                storage.save(resume);
            } else {
                resume = storage.get(uuid);
                resume.setFullName(fullName);
            }

            for (ContactType type : ContactType.values()) {
                String value = request.getParameter(type.name());
                if (value != null && value.trim().length() != 0) {
                    resume.addContact(type, value);
                } else {
                    resume.getContacts().remove(type);
                }
            }
            for (SectionType type : SectionType.values()) {
                String[] firstvalues = request.getParameterValues(type.name());
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.addSection(type, new TextSection(firstvalues[0]));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(type, new ListSection(Arrays.asList(firstvalues[0].split("\\n"))));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> orgList = new ArrayList<>();
                        String[] orgUrls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < firstvalues.length; i++) {
                            String orgName = firstvalues[i];
                            if (orgName != null && !orgName.isEmpty()) {
                                List<Organization.Position> positionsList = new ArrayList<>();
                                String pfx = type.name() + i;
                                String[] positionsStartDates = request.getParameterValues(pfx + "startDate");
                                String[] positionsEndDates = request.getParameterValues(pfx + "endDate");
                                String[] positionsTitles = request.getParameterValues(pfx + "title");
                                String[] positionsDescriptions = request.getParameterValues(pfx + "description");
                                for (int j = 0; j < positionsTitles.length; j++) {
                                    if (positionsTitles[j] != null && !positionsTitles[j].isEmpty()) {
                                        String text = positionsDescriptions != null ? positionsDescriptions[j] : null;
                                        positionsList.add(new Organization.Position(DateUtil.of(positionsStartDates[j]), DateUtil.of(positionsEndDates[j]), positionsTitles[j], text));
                                    }
                                }
                                orgList.add(new Organization(new Link(orgName, orgUrls[i]), positionsList));
                            }
                        }
                        resume.addSection(type, new OrganizationSection(orgList));
                        break;
                }
            }
            storage.update(resume);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = HtmlUtil.escapeHtml(storage.get(uuid));
                break;
            case "add":
                r = Resume.EMPTY;
                break;
            case "edit":
                r = HtmlUtil.escapeHtml(storage.get(uuid));
                for (SectionType type : SectionType.values()) {
                    AbstractSection section = r.getSections().get(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            OrganizationSection orgSection = (OrganizationSection) section;
                            List<Organization> emptyFirstOrganizations = new ArrayList<>();
                            emptyFirstOrganizations.add(Organization.EMPTY);
                            if (orgSection != null) {
                                for (Organization org : orgSection.getList()) {
                                    List<Organization.Position> emptyFirstPositions = new ArrayList<>();
                                    emptyFirstPositions.add(Organization.Position.EMPTY);
                                    emptyFirstPositions.addAll(org.getPositions());
                                    emptyFirstOrganizations.add(new Organization(org.getSectionHeader(), emptyFirstPositions));
                                }
                            }
                            section = new OrganizationSection(emptyFirstOrganizations);
                            break;
                    }
                    r.addSection(type, section);
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")).forward(request, response);
    }
}
