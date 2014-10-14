package rlib.database.impl;

import java.sql.Connection;
import java.sql.SQLException;

import rlib.database.ConnectFactory;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Фабрика подключений к БД.
 * 
 * @author Ronn
 * @created 27.03.2012
 */
public final class BoneCPConnectFactory implements ConnectFactory {

	private static final Logger LOGGER = LoggerManager.getLogger(BoneCPConnectFactory.class);

	/** основной комбо пул подключений */
	private BoneCP source;

	/**
	 * Выключение работы сервера с бд.
	 */
	public synchronized void close() {
		source.close();
		source.shutdown();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return source.getConnection();
	}

	/**
	 * Инициализация фабрики подключений к БД.
	 * 
	 * @param config настройка пула подключений.
	 * @param driver драйвер БД.
	 * @throws SQLException
	 */
	public synchronized void init(final BoneCPConfig config, final String driver) throws SQLException {
		try {
			Class.forName(driver).newInstance();
			source = new BoneCP(config);
			source.getConnection().close();
		} catch(final Exception e) {
			LOGGER.warning(new SQLException("could not init DB connection:" + e));
		}
	}
}