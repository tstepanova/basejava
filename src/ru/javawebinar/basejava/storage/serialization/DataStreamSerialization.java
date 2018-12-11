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

    @Override
    public void doUpdateElement(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            dos.writeUTF("[");
            dos.writeUTF("CONTACTS");
            Map<ContactType, Link> contacts = resume.getContacts();
            for (Map.Entry<ContactType, Link> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                Link link = entry.getValue();
                dos.writeUTF(link.getText());
                dos.writeUTF(link.getUrl() == null ? "" : link.getUrl());
            }
            dos.writeUTF("]");

            Map<SectionType, AbstractSection> sections = resume.getSections();
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                SectionType type = entry.getKey();
                dos.writeUTF("[");
                dos.writeUTF(type.name());
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) sections.get(type)).getText());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        for (String element : ((ListSection) sections.get(type)).getList()) {
                            dos.writeUTF(element);
                        }
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        for (Organization organization : ((OrganizationSection) sections.get(type)).getList()) {
                            Link link = organization.getSectionHeader();
                            dos.writeUTF(link.getText());
                            dos.writeUTF(link.getUrl() == null ? "" : link.getUrl());
                            List<Organization.Position> listPositions = organization.getPositions();
                            dos.writeUTF(String.valueOf(listPositions.size()));
                            for (Organization.Position position : listPositions) {
                                dos.writeUTF(DateTimeFormatter.ofPattern(Organization.Position.getPattern()).format(position.getStartDate()));
                                dos.writeUTF(DateTimeFormatter.ofPattern(Organization.Position.getPattern()).format(position.getEndDate()));
                                dos.writeUTF(position.getTextHeader());
                                dos.writeUTF(position.getText() == null ? "" : position.getText());
                            }
                        }
                        break;
                }
                dos.writeUTF("]");
            }
        }
    }

    @Override
    public Resume doGetElement(InputStream is) throws IOException {
        String text;
        Resume resume;
        try (DataInputStream dis = new DataInputStream(is)) {
            resume = new Resume(dis.readUTF(), dis.readUTF());
            while (dis.available() > 0) {
                if (dis.readUTF().equals("[")) {
                    String type = dis.readUTF();
                    switch (type) {
                        case "CONTACTS":
                            while (!(text = dis.readUTF()).equals("]")) {
                                String linkText = dis.readUTF();
                                String linkUrl = dis.readUTF();
                                resume.addContact(ContactType.valueOf(text), new Link(linkText, linkUrl.isEmpty() ? null : linkUrl));
                            }
                            break;
                        case "PERSONAL":
                        case "OBJECTIVE":
                            while (!(text = dis.readUTF()).equals("]")) {
                                resume.addSection(SectionType.valueOf(type), new TextSection(text));
                            }
                            break;
                        case "ACHIEVEMENT":
                        case "QUALIFICATIONS":
                            List<String> list = new ArrayList<>();
                            while (!(text = dis.readUTF()).equals("]")) {
                                list.add(text);
                            }
                            resume.addSection(SectionType.valueOf(type), new ListSection(list));
                            break;
                        case "EXPERIENCE":
                        case "EDUCATION":
                            OrganizationSection orgSection = new OrganizationSection();
                            while (!(text = dis.readUTF()).equals("]")) {
                                String linkUrl = dis.readUTF();
                                int cntPositions = Integer.valueOf(dis.readUTF());
                                List<Organization.Position> positionList = new ArrayList<>(cntPositions);
                                for (int i = 0; i < cntPositions; i++) {
                                    LocalDate startDate = DateUtil.of(dis.readUTF(), Organization.Position.getPattern());
                                    LocalDate endDate = DateUtil.of(dis.readUTF(), Organization.Position.getPattern());
                                    String positionTextHeader = dis.readUTF();
                                    String positionText = dis.readUTF();
                                    Organization.Position position = new Organization.Position(startDate, endDate, positionTextHeader, positionText.isEmpty() ? null : positionText);
                                    positionList.add(position);
                                }
                                orgSection.setList(new Organization(new Link(text, linkUrl.isEmpty() ? null : linkUrl), positionList));
                            }
                            resume.addSection(SectionType.valueOf(type), orgSection);
                            break;
                    }
                }
            }
        }
        return resume;
    }
}
