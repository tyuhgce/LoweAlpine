package com.tyuhgce.LoweAlpine.utils.jdbc;

import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Help class for ease connection to main storage
 *
 * @version 1.0
 * @since 1.0
 */
public interface JdbcConnector extends Closeable {
    int executeUpdate(String query, Object... args) throws SQLException;

    ResultSet executeQuery(String query, Object... args) throws SQLException;

    void executeQuery(String file) throws SQLException;
}
