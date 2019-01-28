package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlHelper.executeQuery("DELETE FROM resume", null, ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(uuid, conn -> {
            Resume resume = null;

            try (PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT * FROM resume r\n" +
                    "LEFT JOIN contact c ON r.uuid = c.resume_uuid\n" +
                    "WHERE r.uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
                do {
                    addContact(rs, resume);
                } while (rs.next());

            }

            try (PreparedStatement ps = conn.prepareStatement("" +
                    "SELECT * FROM section s\n" +
                    "WHERE s.resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                do {
                    addSection(rs, resume);
                } while (rs.next());

            }
            return resume;
        });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.transactionalExecute(resume.getUuid(), conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume r SET full_name =? WHERE uuid =?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            clearContacts(conn, resume.getUuid());
            saveContacts(conn, resume);
            clearSections(conn, resume.getUuid());
            saveSections(conn, resume);
            return null;
        });
    }

    private void clearContacts(Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE  FROM contact WHERE resume_uuid=?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    private void clearSections(Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE  FROM section WHERE resume_uuid=?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(resume.getUuid(), conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            saveContacts(conn, resume);
            saveSections(conn, resume);
            return null;
        });
    }

    private void saveContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void saveSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                SectionType type = entry.getKey();
                AbstractSection section = entry.getValue();
                ps.setString(1, resume.getUuid());
                ps.setString(2, type.name());
                ps.setString(3, JsonParser.write(section, AbstractSection.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeQuery("DELETE FROM resume r WHERE r.uuid =?", uuid, ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(null, conn -> {
            Map<String, Resume> resumesMap = new LinkedHashMap<>();
            Resume resume = null;

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resume = new Resume(uuid, rs.getString("full_name"));
                    resumesMap.put(uuid, resume);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact ORDER BY resume_uuid")) {
                ResultSet rs = ps.executeQuery();
                String uuid = "";
                while (rs.next()) {
                    String new_uuid = rs.getString("resume_uuid");
                    if (!uuid.equals(new_uuid)) {
                        uuid = new_uuid;
                        resume = resumesMap.get(uuid);
                    }
                    addContact(rs, resume);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section ORDER BY resume_uuid")) {
                ResultSet rs = ps.executeQuery();
                String uuid = "";
                while (rs.next()) {
                    String new_uuid = rs.getString("resume_uuid");
                    if (!uuid.equals(new_uuid)) {
                        uuid = new_uuid;
                        resume = resumesMap.get(uuid);
                    }
                    addSection(rs, resume);
                }
            }

            return new ArrayList<>(resumesMap.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.executeQuery("SELECT COUNT(*) FROM resume", null, ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void addContact(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            ContactType type = ContactType.valueOf(rs.getString("type"));
            resume.addContact(type, value);
        }
    }

    private void addSection(ResultSet rs, Resume resume) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            SectionType type = SectionType.valueOf(rs.getString("type"));
            resume.addSection(type, JsonParser.read(value, AbstractSection.class));
        }
    }
}
