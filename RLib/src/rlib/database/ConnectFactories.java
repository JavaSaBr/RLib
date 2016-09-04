package rlib.database;

import com.jolbox.bonecp.BoneCPConfig;

import java.sql.SQLException;

import rlib.database.impl.BoneCPConnectFactory;

/**
 * The factory for creating the connection factories.
 *
 * @author JavaSaBr
 */
public final class ConnectFactories {

    public static ConnectFactory newBoneCPConnectFactory(final BoneCPConfig config, final String driver) {
        try {
            final BoneCPConnectFactory factory = new BoneCPConnectFactory();
            factory.init(config, driver);
            return factory;
        } catch (final SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
