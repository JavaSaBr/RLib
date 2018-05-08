package com.ss.rlib.common.database;

import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The manager to clean a DataBase.
 *
 * @author JavaSaBr
 */
public abstract class CleaningManager {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(CleaningManager.class);

    /**
     * The list of cleaning queries.
     */
    @NotNull
    private static final Array<CleaningQuery> QUERIES = ArrayFactory.newArray(CleaningQuery.class);

    /**
     * Add the cleaning query.
     *
     * @param description the description.
     * @param query       the query.
     */
    public static void addQuery(@NotNull final String description, @NotNull final String query) {
        QUERIES.add(new CleaningQuery(description, query));
    }

    /**
     * Clean the DB of the connection factory.
     *
     * @param connectionFactory the connect factory
     */
    public static void clean(@NotNull final ConnectionFactory connectionFactory) {

        Connection con = null;
        Statement statement = null;
        try {

            con = connectionFactory.getConnection();
            statement = con.createStatement();

            for (final CleaningQuery clean : QUERIES) {
                LOGGER.info(clean.getDescription().replace("{count}", String.valueOf(statement.executeUpdate(clean.getQuery()))) + ".");
            }

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.close(con, statement);
        }
    }
}
