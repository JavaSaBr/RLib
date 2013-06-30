package rlib.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCPConfig;

/**
 * Интерфейс для реализации фабрики подкюлчений к БД.
 * 
 * @author Ronn
 */
public abstract class ConnectFactory
{
	public static final ConnectFactory newBoneCPConnectFactory(BoneCPConfig config, String driver)
	{
		try
		{
			// создаем фабрику
			BoneCPConnectFactory connects = new BoneCPConnectFactory();
			
			// инициализируем
			connects.init(config, driver);
			
			return connects;
		}
		catch(SQLException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Метод получения подключения для работы с БД.
	 * 
	 * @return connection подключение, для работы с БД.
	 * @throws SQLException.
	 */
	public abstract Connection getConnection() throws SQLException;
}
