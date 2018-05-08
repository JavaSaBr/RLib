package com.ss.rlib.common.database.impl;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.ss.rlib.common.database.ConnectionFactory;
import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The implementation of connection factory base on {@link BoneCP}.
 *
 * @author JavaSaBr
 */
public final class BoneCPConnectionFactory implements ConnectionFactory {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(BoneCPConnectionFactory.class);

    /**
     * The source.
     */
    private BoneCP source;

    @Override
    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    /**
     * Init a connection pool.
     *
     * @param config the config.
     * @param driver the driver.
     * @throws SQLException the sql exception
     */
    public synchronized void init(@NotNull final BoneCPConfig config, @NotNull final String driver) throws SQLException {
        try {
            Class.forName(driver).newInstance();
            source = new BoneCP(config);
            source.getConnection().close();
        } catch (final Exception e) {
            LOGGER.warning(new SQLException("could not init DB connection:" + e));
        }
    }

    /**
     * Close the source.
     */
    public synchronized void close() {
        source.close();
        source.shutdown();
    }
}