package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerialization implements SerializationStrategy {

    @Override
    public void doUpdateElement(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            Map<ContactType, Link> contacts = resume.getContacts();
            writeCollection(dos, contacts.entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                Link link = entry.getValue();
                dos.writeUTF(link.getText());
                dos.writeUTF(link.getUrl() == null ? "" : link.getUrl());
            });

            writeCollection(dos, resume.getSections().entrySet(), entry -> {
                SectionType type = entry.getKey();
                AbstractSection section = entry.getValue();
                dos.writeUTF(type.name());
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) section).getText());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeCollection(dos, ((ListSection) section).getList(), dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        writeCollection(dos, ((OrganizationSection) section).getList(), organization -> {
                            Link link = organization.getSectionHeader();
                            dos.writeUTF(link.getText());
                            dos.writeUTF(link.getUrl() == null ? "" : link.getUrl());
                            writeCollection(dos, organization.getPositions(), position -> {
                                writeDate(dos, position.getStartDate());
                                writeDate(dos, position.getEndDate());
                                dos.writeUTF(position.getTextHeader());
                                dos.writeUTF(position.getText() == null ? "" : position.getText());
                            });
                        });
                        break;
                }
            });
        }
    }

    private void writeDate(DataOutputStream dos, LocalDate ld) throws IOException {
        String pattern = Organization.Position.getPattern();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        dos.writeUTF(dtf.format(ld));
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T item : collection) {
            writer.write(item);
        }
    }

    private interface Writer<T> {
        void write(T t) throws IOException;
    }

    @Override
    public Resume doGetElement(InputStream is) throws IOException {
        Resume resume;
        try (DataInputStream dis = new DataInputStream(is)) {
            resume = new Resume(dis.readUTF(), dis.readUTF());
            read(dis, () -> {
                String type = dis.readUTF();
                String linkText = dis.readUTF();
                String linkUrl = dis.readUTF();
                resume.addContact(ContactType.valueOf(type), new Link(linkText, linkUrl.isEmpty() ? null : linkUrl));
            });
            read(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSection(sectionType, readSection(dis, sectionType));
            });
        }
        return resume;
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return new ListSection(readList(dis, dis::readUTF));
            case EXPERIENCE:
            case EDUCATION:
                OrganizationSection orgSection = new OrganizationSection();
                List<Organization> organizationList = readList(dis, () -> {
                    String linkText = dis.readUTF();
                    String linkUrl = dis.readUTF();
                    List<Organization.Position> positionList = readList(dis, () -> {
                        LocalDate startDate = readDate(dis);
                        LocalDate endDate = readDate(dis);
                        String positionTextHeader = dis.readUTF();
                        String positionText = dis.readUTF();
                        return new Organization.Position(startDate, endDate, positionTextHeader, positionText.isEmpty() ? null : positionText);
                    });
                    return new Organization(new Link(linkText, linkUrl.isEmpty() ? null : linkUrl), positionList);
                });
                orgSection.setList(organizationList);
                return orgSection;
            default:
                throw new IllegalStateException();
        }
    }

    private LocalDate readDate(DataInputStream dis) throws IOException {
        String pattern = Organization.Position.getPattern();
        return DateUtil.of(dis.readUTF(), pattern);
    }

    private <T> List<T> readList(DataInputStream dis, Reader<T> reader) throws IOException {
        int size = dis.readInt();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(reader.read());
        }
        return list;
    }

    private interface Reader<T> {
        T read() throws IOException;
    }

    private void read(DataInputStream dis, SectionReader section) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            section.read();
        }
    }

    private interface SectionReader {
        void read() throws IOException;
    }
}
