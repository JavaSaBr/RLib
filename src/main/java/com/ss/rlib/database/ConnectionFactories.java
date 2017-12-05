package com.ss.rlib.database;

import com.jolbox.bonecp.BoneCPConfig;
import com.ss.rlib.database.impl.BoneCPConnectionFactory;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 * The factory of connection factories.
 *
 * @author JavaSaBr
 */
public final class ConnectionFactories {

    /**
     * Create a new {@link BoneCPConnectionFactory}.
     *
     * @param config the config.
     * @param driver the driver.
     * @return the connection factory.
     */
    public static ConnectionFactory newBoneCPConnectFactory(@NotNull final BoneCPConfig config,
                                                            @NotNull final String driver) {
        try {
            final BoneCPConnectionFactory factory = new BoneCPConnectionFactory();
            factory.init(config, driver);
            return factory;
        } catch (final SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
