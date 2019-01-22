package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> {
            org.postgresql.Driver postgresqlDriver = new org.postgresql.Driver();
            try {
                Class.forName(postgresqlDriver.getClass().getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        };
    }

    public interface ExecutorQuery<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    public <T> T executeQuery(String sqlQuery, String uuid, ExecutorQuery<T> executor) {
        try (Connection conn = connectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e, uuid);
        }
    }

    public <T> T transactionalExecute(String uuid, SqlTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionUtil.convertException(e, uuid);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}