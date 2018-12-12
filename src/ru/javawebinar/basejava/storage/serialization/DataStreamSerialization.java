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

            Map<ContactType, Link> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, Link> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                Link link = entry.getValue();
                dos.writeUTF(link.getText());
                dos.writeUTF(link.getUrl() == null ? "" : link.getUrl());
            }

            Map<SectionType, AbstractSection> sections = resume.getSections();
            for (Map.Entry<SectionType, AbstractSection> entry : sections.entrySet()) {
                SectionType type = entry.getKey();
                dos.writeUTF(type.name());
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) sections.get(type)).getText());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> listString = ((ListSection) sections.get(type)).getList();
                        dos.writeInt(listString.size());
                        for (String element : listString) {
                            dos.writeUTF(element);
                        }
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> listOrganization = ((OrganizationSection) sections.get(type)).getList();
                        dos.writeInt(listOrganization.size());
                        for (Organization organization : listOrganization) {
                            Link link = organization.getSectionHeader();
                            dos.writeUTF(link.getText());
                            dos.writeUTF(link.getUrl() == null ? "" : link.getUrl());
                            List<Organization.Position> listPositions = organization.getPositions();
                            dos.writeInt(listPositions.size());
                            for (Organization.Position position : listPositions) {
                                dos.writeUTF(DateTimeFormatter.ofPattern(Organization.Position.getPattern()).format(position.getStartDate()));
                                dos.writeUTF(DateTimeFormatter.ofPattern(Organization.Position.getPattern()).format(position.getEndDate()));
                                dos.writeUTF(position.getTextHeader());
                                dos.writeUTF(position.getText() == null ? "" : position.getText());
                            }
                        }
                        break;
                }
            }
        }
    }

    @Override
    public Resume doGetElement(InputStream is) throws IOException {
        String text;
        Resume resume;
        try (DataInputStream dis = new DataInputStream(is)) {
            resume = new Resume(dis.readUTF(), dis.readUTF());
            int sectionSize = dis.readInt();
            for (int i = 0; i < sectionSize; i++) {
                String type = dis.readUTF();
                String linkText = dis.readUTF();
                String linkUrl = dis.readUTF();
                resume.addContact(ContactType.valueOf(type), new Link(linkText, linkUrl.isEmpty() ? null : linkUrl));
            }
            while (dis.available() > 0) {
                String type = dis.readUTF();
                switch (type) {
                    case "PERSONAL":
                    case "OBJECTIVE":
                        resume.addSection(SectionType.valueOf(type), new TextSection(dis.readUTF()));
                        break;
                    case "ACHIEVEMENT":
                    case "QUALIFICATIONS":
                        sectionSize = dis.readInt();
                        List<String> list = new ArrayList<>(sectionSize);
                        for (int i = 0; i < sectionSize; i++) {
                            list.add(dis.readUTF());
                        }
                        resume.addSection(SectionType.valueOf(type), new ListSection(list));
                        break;
                    case "EXPERIENCE":
                    case "EDUCATION":
                        sectionSize = dis.readInt();
                        OrganizationSection orgSection = new OrganizationSection();
                        for (int i = 0; i < sectionSize; i++) {
                            String linkText = dis.readUTF();
                            String linkUrl = dis.readUTF();
                            int cntPositions = dis.readInt();
                            List<Organization.Position> positionList = new ArrayList<>(cntPositions);
                            for (int j = 0; j < cntPositions; j++) {
                                LocalDate startDate = DateUtil.of(dis.readUTF(), Organization.Position.getPattern());
                                LocalDate endDate = DateUtil.of(dis.readUTF(), Organization.Position.getPattern());
                                String positionTextHeader = dis.readUTF();
                                String positionText = dis.readUTF();
                                Organization.Position position = new Organization.Position(startDate, endDate, positionTextHeader, positionText.isEmpty() ? null : positionText);
                                positionList.add(position);
                            }
                            orgSection.setList(new Organization(new Link(linkText, linkUrl.isEmpty() ? null : linkUrl), positionList));
                        }
                        resume.addSection(SectionType.valueOf(type), orgSection);
                        break;
                }
            }
        }
        return resume;
    }
}
