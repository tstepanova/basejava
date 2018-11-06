package ru.javawebinar.basejava.model;

import java.util.*;

public class Resume implements Comparable<Resume> {

    private final String uuid;
    private String fullName;
    private Map<ContactType, Link> contacts = new EnumMap<>(ContactType.class);
    private Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void addContact(ContactType contactType, Link contact) {
        contacts.put(contactType, contact);
    }

    public Link getContact(ContactType contactType) {
        return contacts.get(contactType);
    }

    public void deleteContact(ContactType contactType) {
        contacts.remove(contactType);
    }

    public void addSection(SectionType sectionType, AbstractSection section) {
        sections.put(sectionType, section);
    }

    public AbstractSection getSection(SectionType sectionType) {
        return sections.get(sectionType);
    }

    public void deleteSection(SectionType sectionType) {
        sections.remove(sectionType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String res = fullName + "\n\n";

        Iterator<ContactType> itr1 = contacts.keySet().iterator();
        while (itr1.hasNext()) {
            res = res + contacts.get(itr1.next()).toString() + '\n';
        }
        res = res + '\n';
        Iterator<SectionType> itr2 = sections.keySet().iterator();
        while (itr2.hasNext()) {
            SectionType key = itr2.next();
            res = res + key.getTitle() + '\n' + sections.get(key).toString() + "\n\n";
        }

        return res;
    }

    @Override
    public int compareTo(Resume o) {
        int cmp = fullName.compareTo(o.fullName);
        return cmp != 0 ? cmp : uuid.compareTo(o.uuid);
    }
}