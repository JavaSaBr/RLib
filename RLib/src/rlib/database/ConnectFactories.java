package rlib.database;

import com.jolbox.bonecp.BoneCPConfig;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

import rlib.database.impl.BoneCPConnectFactory;

/**
 * The factory for creating the connection factories.
 *
 * @author JavaSaBr
 */
public final class ConnectFactories {

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
