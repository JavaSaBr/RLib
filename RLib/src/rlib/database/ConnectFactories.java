package rlib.database;

import java.sql.SQLException;

import com.jolbox.bonecp.BoneCPConfig;

/**
 * Интерфейс для реализации фабрики подкюлчений к БД.
 * 
 * @author Ronn
 */
public final class ConnectFactories {

	public static final ConnectFactory newBoneCPConnectFactory(BoneCPConfig config, String driver) {
		try {
			BoneCPConnectFactory factory = new BoneCPConnectFactory();
			factory.init(config, driver);
			return factory;
		} catch(SQLException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
