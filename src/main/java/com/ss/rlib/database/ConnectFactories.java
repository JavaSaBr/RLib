package com.ss.rlib.database;

import com.jolbox.bonecp.BoneCPConfig;

import com.ss.rlib.database.impl.BoneCPConnectFactory;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

/**
 * The factory for creating the connection factories.
 *
 * @author JavaSaBr
 */
public final class ConnectFactories {

    /**
     * New bone cp connect factory connect factory.
     *
     * @param config the config
     * @param driver the driver
     * @return the connect factory
     */
    public static ConnectFactory newBoneCPConnectFactory(@NotNull final BoneCPConfig config, @NotNull final String driver) {
        try {
            final BoneCPConnectFactory factory = new BoneCPConnectFactory();
            factory.init(config, driver);
            return factory;
        } catch (final SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
