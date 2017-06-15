package com.ss.rlib.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The interface for implementing the connection factory to database.
 *
 * @author JavaSaBr
 */
public interface ConnectFactory {

    /**
     * Get new connection to database.
     *
     * @return the new connection.
     * @throws SQLException the sql exception
     */
    Connection getConnection() throws SQLException;
}
