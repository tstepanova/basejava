package ru.javawebinar.basejava.util;

import org.apache.commons.lang3.StringEscapeUtils;
import ru.javawebinar.basejava.model.*;

import java.util.List;
import java.util.Map;

public class HtmlUtil {

    public static Resume escapeHtml(Resume resume) {
        resume.setFullName(StringEscapeUtils.escapeHtml4(resume.getFullName()));
        Map<ContactType, String> contacts = resume.getContacts();
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            ContactType type = entry.getKey();
            String contact = entry.getValue();
            resume.addContact(type, StringEscapeUtils.escapeHtml4(contact));
        }

        Map<SectionType, AbstractSection> sections = resume.getSections();
        for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
            SectionType type = entry.getKey();
            AbstractSection section = entry.getValue();
            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    String text = ((TextSection) section).getText();
                    resume.addSection(type, new TextSection(StringEscapeUtils.escapeHtml4(text)));
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    List<String> list = ((ListSection) section).getList();
                    for(int i=0; i< list.size(); i++){
                        list.set(i, StringEscapeUtils.escapeHtml4(list.get(i)));
                    }
                    resume.addSection(type, new ListSection(list));
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    List<Organization> organizations = ((OrganizationSection) section).getList();
                    for(int i=0; i< organizations.size(); i++){
                        Link sectionHeader = organizations.get(i).getSectionHeader();
                        sectionHeader.setText(StringEscapeUtils.escapeHtml4(sectionHeader.getText()));
                        List<Organization.Position> positions = organizations.get(i).getPositions();
                        for(int j=0; j< positions.size(); j++){
                            Organization.Position position = positions.get(j);
                            position.setTextHeader(StringEscapeUtils.escapeHtml4(position.getTextHeader()));
                            position.setText(StringEscapeUtils.escapeHtml4(position.getText()));
                            positions.set(j, position);
                        }
                        organizations.set(i, new Organization(sectionHeader, positions));
                    }
                    resume.addSection(type, new OrganizationSection(organizations));
                    break;
            }
        }

        return resume;
    }
}