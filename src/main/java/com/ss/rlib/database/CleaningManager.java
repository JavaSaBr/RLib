package com.ss.rlib.database;

import com.ss.rlib.logging.Logger;
import com.ss.rlib.logging.LoggerManager;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * THe manager to clean a DataBase.
 *
 * @author JavaSaBr
 */
public abstract class CleaningManager {

    private static final Logger LOGGER = LoggerManager.getLogger(CleaningManager.class);

    /**
     * The list of cleaning queries.
     */
    @NotNull
    public static final Array<CleaningQuery> QUERY = ArrayFactory.newArray(CleaningQuery.class);

    /**
     * Add a cleaning query.
     *
     * @param name  the table name.
     * @param query the query.
     */
    public static void addQuery(@NotNull final String name, @NotNull final String query) {
        QUERY.add(new CleaningQuery(name, query));
    }

    /**
     * Clean the DB of the connection factory.
     *
     * @param connectFactory the connect factory
     */
    public static void cleaning(@NotNull final ConnectFactory connectFactory) {

        Connection con = null;
        Statement statement = null;
        try {

            con = connectFactory.getConnection();
            statement = con.createStatement();

            for (final CleaningQuery clean : QUERY) {
                LOGGER.info(clean.getName().replace("{count}", String.valueOf(statement.executeUpdate(clean.getQuery()))) + ".");
            }

        } catch (final SQLException e) {
            LOGGER.warning(e);
        } finally {
            DBUtils.close(con, statement);
        }
    }
}
