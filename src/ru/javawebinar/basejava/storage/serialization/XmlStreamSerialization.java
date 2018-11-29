package ru.javawebinar.basejava.storage.serialization;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStreamSerialization implements SerializationStrategy {
    private XmlParser xmlParser;

    public XmlStreamSerialization() {
        xmlParser = new XmlParser(Resume.class, OrganizationSection.class, Link.class, TextSection.class, ListSection.class, Organization.Position.class);
    }

    @Override
    public void doUpdateElement(Resume r, OutputStream os) throws IOException {
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(r, w);
        }
    }

    @Override
    public Resume doGetElement(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }
}