package com.ss.rlib.common.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The interface to implement a connection factory to database.
 *
 * @author JavaSaBr
 */
public interface ConnectionFactory {

    /**
     * Get new connection to database.
     *
     * @return the new connection.
     * @throws SQLException the sql exception
     */
    Connection getConnection() throws SQLException;
}
