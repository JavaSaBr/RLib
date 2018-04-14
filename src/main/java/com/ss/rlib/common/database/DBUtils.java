package com.ss.rlib.common.database;

import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The class with utility methods for working with DB.
 *
 * @author JavaSaBr
 */
public final class DBUtils {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(DBUtils.class);

    /**
     * Close the connection.
     *
     * @param connection the connection.
     */
    public static void close(@Nullable final Connection connection) {
        if (connection == null) return;
        try {
            connection.close();
        } catch (final SQLException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Close the connection and the statement.
     *
     * @param connection the connection.
     * @param statement  the statement.
     */
    public static void close(@Nullable final Connection connection, @Nullable final Statement statement) {
        close(statement);
        close(connection);
    }

    /**
     * Close the connection, the statement and the result set.
     *
     * @param connection the connection.
     * @param statement  the statement.
     * @param rset       the result set.
     */
    public static void close(@Nullable final Connection connection, @Nullable final Statement statement,
                             @Nullable final ResultSet rset) {
        close(rset);
        close(statement);
        close(connection);
    }

    /**
     * Close the statement and the result set.
     *
     * @param statement the statement.
     * @param rset      the result set.
     */
    public static void close(@Nullable final Statement statement, @Nullable final ResultSet rset) {
        close(rset);
        close(statement);
    }

    /**
     * Close the result set.
     *
     * @param rset the result set.
     */
    public static void close(@Nullable final ResultSet rset) {
        if (rset == null) return;
        try {
            rset.close();
        } catch (final SQLException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * Close a statement.
     *
     * @param statement the statement.
     */
    public static void close(@Nullable final Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (final SQLException e) {
            LOGGER.warning(e);
        }
    }

    private DBUtils() {
        throw new IllegalArgumentException();
    }
}