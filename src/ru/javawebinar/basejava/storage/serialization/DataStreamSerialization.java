package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Link;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
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
            // TODO implements sections
        }
    }

    @Override
    public Resume doGetElement(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String contactType = dis.readUTF();
                String text = dis.readUTF();
                String url = dis.readUTF();
                resume.addContact(ContactType.valueOf(contactType), new Link(text, url.equals("null") ? null : url));
            }
            // TODO implements sections
            return resume;
        }
    }
}
