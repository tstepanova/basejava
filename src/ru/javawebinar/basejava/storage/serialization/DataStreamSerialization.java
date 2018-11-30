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
                Link contact = entry.getValue();
                dos.writeUTF(contact.getText());
                if (contact.getUrl() != null) {
                    dos.writeUTF(contact.getUrl());
                } else {
                    dos.writeUTF("null");
                }
            }

            Map<SectionType, AbstractSection> sections = resume.getSections();

            //Позиция
            TextSection objectiveSection = (TextSection) sections.get(SectionType.OBJECTIVE);
            if (objectiveSection != null) dos.writeUTF(objectiveSection.getText());

            //Личные качества
            TextSection personalSection = (TextSection) sections.get(SectionType.PERSONAL);
            if (personalSection != null) dos.writeUTF(personalSection.getText());

            //Достижения
            ListSection achievementSection = (ListSection) sections.get(SectionType.ACHIEVEMENT);
            if (achievementSection != null) {
                dos.writeInt(achievementSection.getList().size());
                for (String element : achievementSection.getList()) {
                    dos.writeUTF(element);
                }
            }

            //Квалификация
            ListSection qualificationsSection = (ListSection) sections.get(SectionType.QUALIFICATIONS);
            if (qualificationsSection != null) {
                dos.writeInt(qualificationsSection.getList().size());
                for (String element : qualificationsSection.getList()) {
                    dos.writeUTF(element);
                }
            }

            //Опыт работы
            OrganizationSection experienceSection = (OrganizationSection) sections.get(SectionType.EXPERIENCE);
            if (experienceSection != null) {
                dos.writeInt(experienceSection.getList().size());
                for (Organization organization : experienceSection.getList()) {
                    Link contact = organization.getSectionHeader();
                    dos.writeUTF(contact.getText());
                    if (contact.getUrl() != null) {
                        dos.writeUTF(contact.getUrl());
                    } else {
                        dos.writeUTF("null");
                    }

                    dos.writeInt(organization.getPositions().size());
                    for (Organization.Position position : organization.getPositions()) {
                        dos.writeUTF(DateTimeFormatter.ofPattern(position.getPattern()).format(position.getStartDate()));
                        dos.writeUTF(DateTimeFormatter.ofPattern(position.getPattern()).format(position.getEndDate()));
                        dos.writeUTF(position.getTextHeader());
                        if (position.getText() != null) {
                            dos.writeUTF(position.getText());
                        } else {
                            dos.writeUTF("null");
                        }
                    }
                }
            }

            //Образование
            OrganizationSection educationSection = (OrganizationSection) sections.get(SectionType.EDUCATION);
            if (experienceSection != null) {
                dos.writeInt(educationSection.getList().size());
                for (Organization organization : educationSection.getList()) {
                    Link contact = organization.getSectionHeader();
                    dos.writeUTF(contact.getText());
                    if (contact.getUrl() != null) {
                        dos.writeUTF(contact.getUrl());
                    } else {
                        dos.writeUTF("null");
                    }

                    dos.writeInt(organization.getPositions().size());
                    for (Organization.Position position : organization.getPositions()) {
                        dos.writeUTF(DateTimeFormatter.ofPattern(position.getPattern()).format(position.getStartDate()));
                        dos.writeUTF(DateTimeFormatter.ofPattern(position.getPattern()).format(position.getEndDate()));
                        dos.writeUTF(position.getTextHeader());
                        if (position.getText() != null) {
                            dos.writeUTF(position.getText());
                        } else {
                            dos.writeUTF("null");
                        }
                    }
                }
            }
            dos.flush();
        }
    }

    @Override
    public Resume doGetElement(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);

            int sectionSize = dis.readInt();
            if (sectionSize == 0) return resume;
            for (int i = 0; i < sectionSize; i++) {
                String contactType = dis.readUTF();
                String text = dis.readUTF();
                String url = dis.readUTF();
                resume.addContact(ContactType.valueOf(contactType), new Link(text, url.equals("null") ? null : url));
            }

            //Позиция
            resume.addSection(SectionType.OBJECTIVE, new TextSection(dis.readUTF()));
            //Личные качества
            resume.addSection(SectionType.PERSONAL, new TextSection(dis.readUTF()));

            //Достижения
            sectionSize = dis.readInt();
            List<String> achievementList = new ArrayList<>(sectionSize);
            for (int i = 0; i < sectionSize; i++) {
                achievementList.add(dis.readUTF());
            }
            resume.addSection(SectionType.ACHIEVEMENT, new ListSection(achievementList));

            //Квалификация
            sectionSize = dis.readInt();
            List<String> qualificationsList = new ArrayList<>(sectionSize);
            for (int i = 0; i < sectionSize; i++) {
                qualificationsList.add(dis.readUTF());
            }
            resume.addSection(SectionType.QUALIFICATIONS, new ListSection(qualificationsList));

            //Опыт работы
            sectionSize = dis.readInt();
            OrganizationSection experienceSection = new OrganizationSection();
            for (int i = 0; i < sectionSize; i++) {
                String sectionHeaderText = dis.readUTF();
                String sectionHeaderUrl = dis.readUTF();
                int positionSize = dis.readInt();
                List<Organization.Position> positionList = new ArrayList<>(positionSize);
                for (int j = 0; j < positionSize; j++) {
                    LocalDate startDate = DateUtil.of(dis.readUTF(), Organization.Position.getPattern());
                    LocalDate endDate = DateUtil.of(dis.readUTF(), Organization.Position.getPattern());
                    String textHeader = dis.readUTF();
                    String text = dis.readUTF();
                    Organization.Position position = new Organization.Position(startDate, endDate, textHeader, text.equals("null") ? null : text);
                    positionList.add(j, position);
                }
                Organization organization = new Organization(new Link(sectionHeaderText, sectionHeaderUrl.equals("null") ? null : sectionHeaderUrl), positionList);
                experienceSection.setList(organization);
            }
            resume.addSection(SectionType.EXPERIENCE, experienceSection);

            //Образование
            sectionSize = dis.readInt();
            OrganizationSection educationSection = new OrganizationSection();
            for (int i = 0; i < sectionSize; i++) {
                String sectionHeaderText = dis.readUTF();
                String sectionHeaderUrl = dis.readUTF();
                int positionSize = dis.readInt();
                List<Organization.Position> positionList = new ArrayList<>(positionSize);
                for (int j = 0; j < positionSize; j++) {
                    LocalDate startDate = DateUtil.of(dis.readUTF(), Organization.Position.getPattern());
                    LocalDate endDate = DateUtil.of(dis.readUTF(), Organization.Position.getPattern());
                    String textHeader = dis.readUTF();
                    String text = dis.readUTF();
                    Organization.Position position = new Organization.Position(startDate, endDate, textHeader, text.equals("null") ? null : text);
                    positionList.add(j, position);
                }
                Organization organization = new Organization(new Link(sectionHeaderText, sectionHeaderUrl.equals("null") ? null : sectionHeaderUrl), positionList);
                educationSection.setList(organization);
            }
            resume.addSection(SectionType.EDUCATION, educationSection);

            return resume;
        }
    }
}
