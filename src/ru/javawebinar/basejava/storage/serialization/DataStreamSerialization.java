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

        append("[");
        append("CONTACTS");
        append(resume.getUuid());
        append(resume.getFullName());

        Map<ContactType, Link> contacts = resume.getContacts();
        append(contacts.size());
        for (Map.Entry<ContactType, Link> entry : contacts.entrySet()) {
            append(entry.getKey().name());
            Link link = entry.getValue();
            append(link.getText());
            append(link.getUrl());
        }
        append("]");

        Map<SectionType, AbstractSection> sections = resume.getSections();
        for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
            SectionType type = entry.getKey();
            AbstractSection section = entry.getValue();
            append("[");
            append(type);
            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    if (section != null) {
                        append(((TextSection) sections.get(type)).getText());
                    } else {
                        append("undefined");
                    }
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    if (section != null) {
                        for (String element : ((ListSection) sections.get(type)).getList()) {
                            append(element);
                        }
                    } else {
                        append(0);
                    }
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    if (section != null) {
                        for (Organization organization : ((OrganizationSection) sections.get(type)).getList()) {
                            Link link = organization.getSectionHeader();
                            append(link.getText());
                            append(link.getUrl());
                            List<Organization.Position> listPositions = organization.getPositions();
                            if (listPositions != null) {
                                append(listPositions.size());
                                for (Organization.Position position : listPositions) {
                                    append(DateTimeFormatter.ofPattern(position.getPattern()).format(position.getStartDate()));
                                    append(DateTimeFormatter.ofPattern(position.getPattern()).format(position.getEndDate()));
                                    append(position.getTextHeader());
                                    append(position.getText());
                                }
                            } else {
                                append(0);
                            }
                        }
                    } else {
                        append(0);
                    }
                    break;
            }
            append("]");
        }
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(sb.toString());
        }
    }

    private void append(Object text) {
        if (text != null) {
            sb.append(text);
        } else {
            sb.append("undefined");
        }
        sb.append(NEWLINE);
    }

    @Override
    public Resume doGetElement(InputStream is) throws IOException {
        Resume resume = null;
        String text = null;
        try (DataInputStream dis = new DataInputStream(is)) {
            text = dis.readUTF();
        }
        if (text != null) {
            List<String[]> sectionsList = new ArrayList<>();
            String[] sectionsArray = text.split("(\\[|\\])");
            for (int i = 0; i < sectionsArray.length; i++) {
                if (sectionsArray[i] != null && !sectionsArray[i].isEmpty()) {
                    String[] linesArray = sectionsArray[i].split(NEWLINE);
                    if (linesArray != null && linesArray.length > 0) sectionsList.add(linesArray);
                }
            }

            for (int i = 0; i < sectionsList.size(); i++) {
                String[] section = sectionsList.get(i);
                int start = 0;
                for (int j = 0; j < section.length; j++) {
                    if (section[j] != null && !section[j].isEmpty()) {
                        start = j;
                        break;
                    }
                }
                String type = section[start];
                switch (type) {
                    case "CONTACTS":
                        resume = new Resume(section[start + 1], section[start + 2]);
                        int contactsSize = Integer.valueOf(section[start + 3]);
                        for (int j = start + 4; j < start + 4 + contactsSize * 3; j = j + 3) {
                            resume.addContact(ContactType.valueOf(section[j]), new Link(read(section[j + 1]), read(section[j + 2])));
                        }
                        break;
                    case "PERSONAL":
                    case "OBJECTIVE":
                        resume.addSection(SectionType.valueOf(type), new TextSection(read(section[start + 1])));
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        List<String> list = new ArrayList<>();
                        for (int j = start + 1; j < section.length; j++) {
                            list.add(read(section[j]));
                        }
                        resume.addSection(SectionType.valueOf(type), new ListSection(list));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        OrganizationSection orgSection = new OrganizationSection();
                        int step = 1;
                        for (int j = start + 1; j < section.length - 3; j = j + step) {
                            String linkText = read(section[j]);
                            String linkUrl = read(section[j + 1]);
                            int cntPositions = Integer.valueOf(section[j + 2]);
                            int startLine = j + 3;
                            List<Organization.Position> positionList = new ArrayList<>(cntPositions);
                            for (int k = startLine; k < startLine + cntPositions * 4; k = k + 4) {
                                LocalDate startDate = DateUtil.of(read(section[k]), Organization.Position.getPattern());
                                LocalDate endDate = DateUtil.of(read(section[k + 1]), Organization.Position.getPattern());
                                Organization.Position position = new Organization.Position(startDate, endDate, read(section[k + 2]), read(section[k + 3]));
                                positionList.add(position);
                            }
                            orgSection.setList(new Organization(new Link(linkText, linkUrl), positionList));
                            step = 3 + cntPositions * 4;
                        }
                        resume.addSection(SectionType.valueOf(type), orgSection);
                        break;
                }
            }
        }
        return resume;
    }

    private String read(String text) {
        if (text.equals("undefined")) return null;
        return text;
    }
}
