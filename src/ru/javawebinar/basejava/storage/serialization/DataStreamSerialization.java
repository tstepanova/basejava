package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerialization implements SerializationStrategy {

    private static final String NEWLINE = System.getProperty("line.separator");
    private StringBuilder sb;

    @Override
    public void doUpdateElement(Resume resume, OutputStream os) throws IOException {
        sb = new StringBuilder();
        resumeToText(resume);
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(sb.toString());
            System.out.println("");
        }
    }

    @Override
    public Resume doGetElement(InputStream is) throws IOException {
        String text = null;
        try (DataInputStream dis = new DataInputStream(is)) {
            text = dis.readUTF();
        }
        return textToResume(text);
    }

    private void append(String text) {
        if (text != null && !text.isEmpty()) {
            sb.append(text);
        } else {
            sb.append("undefined");
        }
        sb.append(NEWLINE);
    }

    private void append(int text) {
        sb.append(text);
        sb.append(NEWLINE);
    }

    private void append(Link link) {
        append(link.getText());
        append(link.getUrl());
    }

    private void append(TextSection section) {
        if (section != null) {
            append(section.getText());
        } else {
            append("undefined");
        }
    }

    private void append(ListSection section) {
        if (section != null) {
            for (String element : section.getList()) {
                append(element);
            }
        } else {
            append(0);
        }
    }

    private void append(OrganizationSection section) {
        if (section != null) {
            for (Organization organization : section.getList()) {
                append(organization.getSectionHeader());
                append(organization.getPositions());
            }
        } else {
            append(0);
        }
    }

    private void append(List<Organization.Position> list) {
        if (list != null) {
            append(list.size());
            for (Organization.Position position : list) {
                append(DateTimeFormatter.ofPattern(position.getPattern()).format(position.getStartDate()));
                append(DateTimeFormatter.ofPattern(position.getPattern()).format(position.getEndDate()));
                append(position.getTextHeader());
                append(position.getText());
            }
        } else {
            append(0);
        }
    }

    private void resumeToText(Resume resume) {
        append(resume.getUuid());
        append(resume.getFullName());

        append("[CONTACTS]");
        Map<ContactType, Link> contacts = resume.getContacts();
        append(contacts.size());
        for (Map.Entry<ContactType, Link> entry : contacts.entrySet()) {
            append(entry.getKey().name());
            append(entry.getValue());
        }

        Map<SectionType, AbstractSection> sections = resume.getSections();
        append("[OBJECTIVE]");
        append((TextSection) sections.get(SectionType.OBJECTIVE));
        append("[PERSONAL]");
        append((TextSection) sections.get(SectionType.PERSONAL));
        append("[ACHIEVEMENT]");
        append((ListSection) sections.get(SectionType.ACHIEVEMENT));
        append("[QUALIFICATIONS]");
        append((ListSection) sections.get(SectionType.QUALIFICATIONS));
        append("[EXPERIENCE]");
        append((OrganizationSection) sections.get(SectionType.EXPERIENCE));
        append("[EDUCATION]");
        append((OrganizationSection) sections.get(SectionType.EDUCATION));
    }

    private String read(String text) {
        if (text.equals("undefined")) return null;
        return text;
    }

    private List<Organization.Position> readPositions(String[] text, int startLine, int cntPositions) {
        List<Organization.Position> positionList = new ArrayList<>(cntPositions);
        for (int i = startLine; i < startLine + cntPositions * 4; i = i + 4) {
            LocalDate startDate = DateUtil.of(read(text[i]), Organization.Position.getPattern());
            LocalDate endDate = DateUtil.of(read(text[i + 1]), Organization.Position.getPattern());
            Organization.Position position = new Organization.Position(startDate, endDate, read(text[i + 2]), read(text[i + 3]));
            positionList.add(position);
        }
        return positionList;
    }

    private OrganizationSection readOrgSection(String[] text) {
        OrganizationSection section = new OrganizationSection();
        int step = 1;
        for (int i = 0; i < text.length - 3; i = i + step) {
            String linkText = read(text[i]);
            String linkUrl = read(text[i + 1]);
            int cntPositions = Integer.valueOf(text[i + 2]);
            int start = i + 3;
            Organization organization = new Organization(new Link(linkText, linkUrl), readPositions(text, start, cntPositions));
            section.setList(organization);
            step = 3 + cntPositions * 4;
        }
        return section;
    }

    private ListSection readListSection(String[] text) {
        int sectionSize = text.length;
        List<String> list = new ArrayList<>(sectionSize);
        for (int i = 0; i < sectionSize; i++) {
            list.add(read(text[i]));
        }
        return new ListSection(list);
    }

    private Resume textToResume(String text) {
        if (text != null) {
            String[] head = text.split(NEWLINE + "\\[CONTACTS\\]" + NEWLINE)[0].split(NEWLINE);
            String[] contacts = text.split(NEWLINE + "\\[OBJECTIVE\\]" + NEWLINE)[0].split(NEWLINE + "\\[CONTACTS\\]" + NEWLINE)[1].split(NEWLINE);
            String[] objective = text.split(NEWLINE + "\\[PERSONAL\\]" + NEWLINE)[0].split(NEWLINE + "\\[OBJECTIVE\\]" + NEWLINE)[1].split(NEWLINE);
            String[] personal = text.split(NEWLINE + "\\[ACHIEVEMENT\\]" + NEWLINE)[0].split(NEWLINE + "\\[PERSONAL\\]" + NEWLINE)[1].split(NEWLINE);
            String[] achievement = text.split(NEWLINE + "\\[QUALIFICATIONS\\]" + NEWLINE)[0].split(NEWLINE + "\\[ACHIEVEMENT\\]" + NEWLINE)[1].split(NEWLINE);
            String[] qualifications = text.split(NEWLINE + "\\[EXPERIENCE\\]" + NEWLINE)[0].split(NEWLINE + "\\[QUALIFICATIONS\\]" + NEWLINE)[1].split(NEWLINE);
            String[] experience = text.split(NEWLINE + "\\[EDUCATION\\]" + NEWLINE)[0].split(NEWLINE + "\\[EXPERIENCE\\]" + NEWLINE)[1].split(NEWLINE);
            String[] education = text.split(NEWLINE + "\\[EDUCATION\\]" + NEWLINE)[1].split(NEWLINE);

            Resume resume = new Resume(head[0], head[1]);

            int contactsSize = Integer.valueOf(contacts[0]);
            for (int i = 1; i < contactsSize * 3; i = i + 3) {
                resume.addContact(ContactType.valueOf(read(contacts[i])), new Link(read(contacts[i + 1]), read(contacts[i + 2])));
            }

            resume.addSection(SectionType.OBJECTIVE, new TextSection(read(objective[0])));
            resume.addSection(SectionType.PERSONAL, new TextSection(read(personal[0])));
            resume.addSection(SectionType.ACHIEVEMENT, readListSection(achievement));
            resume.addSection(SectionType.QUALIFICATIONS, readListSection(qualifications));
            resume.addSection(SectionType.EXPERIENCE, readOrgSection(experience));
            resume.addSection(SectionType.EDUCATION, readOrgSection(education));

            return resume;
        }
        return null;
    }
}
