package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Link;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return sqlHelper.executeQuery("" +
                        "SELECT * FROM resume r\n" +
                        "LEFT JOIN contact c ON r.uuid = c.resume_uuid\n" +
                        "WHERE r.uuid =?", uuid,
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContact(rs, r);
                    } while (rs.next());

                    return r;
                });
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            ContactType type = ContactType.valueOf(rs.getString("type"));
            r.addContact(type, readLink(value));
        }
    }

    private Link readLink(String value) {
        String text = value;
        String url = null;
        if (value.substring(value.length() - 1, value.length()).equals(")")) {
            url = value.substring(value.indexOf("(") + 1, value.length() - 1);
            text = value.replace(" (" + url + ")", "");
        }
        return new Link(text, url);
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(r.getUuid(), conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE resume r SET full_name =? WHERE uuid =?")) {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, r.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(r.getUuid());
                        }
                    }
                    clearContacts(conn, r.getUuid());
                    saveContacts(conn, r);
                    return null;
                }
        );
    }

    private void clearContacts(Connection conn, String uuid) throws SQLException {
        sqlHelper.executeQuery("DELETE  FROM contact WHERE resume_uuid=?", uuid, ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(r.getUuid(), conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    }
                    saveContacts(conn, r);
                    return null;
                }
        );
    }

    private void saveContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, Link> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                Link link = new Link(e.getValue().getText(), e.getValue().getUrl());
                ps.setString(3, link.toString());
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
        return sqlHelper.executeQuery("" +
                "SELECT * FROM resume r\n" +
                "LEFT JOIN contact c ON r.uuid = c.resume_uuid\n" +
                "ORDER BY r.full_name, r.uuid", null, ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            String uuid = "";
            Resume resume = null;
            while (rs.next()) {
                String tmpUuid = rs.getString("uuid");
                if (!uuid.equals(tmpUuid)) {
                    if (!uuid.isEmpty()) list.add(resume);
                    resume = new Resume(tmpUuid, rs.getString("full_name"));
                    addContact(rs, resume);
                    uuid = tmpUuid;
                }
                addContact(rs, resume);
            }
            if (resume != null) list.add(resume);
            return list;
        });
    }

    @Override
    public int size() {
        return sqlHelper.executeQuery("SELECT COUNT(*) FROM resume", null, ps -> {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }
}
