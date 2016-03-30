package rlib.database;

import com.jolbox.bonecp.BoneCPConfig;

import java.sql.SQLException;

import rlib.database.impl.BoneCPConnectFactory;

/**
 * Интерфейс для реализации фабрики подкюлчений к БД.
 *
 * @author Ronn
 */
public final class ConnectFactories {

    public static final ConnectFactory newBoneCPConnectFactory(final BoneCPConfig config, final String driver) {
        try {
            final BoneCPConnectFactory factory = new BoneCPConnectFactory();
            factory.init(config, driver);
            return factory;
        } catch (final SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
