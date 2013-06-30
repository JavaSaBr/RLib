package rlib.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import rlib.logging.Logger;
import rlib.logging.Loggers;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;


/**
 * Менеджер для работы с БД.
 * 
 * Для работы его, к проекту должны быть подключены
 * mysql-connector-java.jar и c3p0.jar
 *
 * @author Ronn
 * @created 27.03.2012
 * @deprecated
 */
public final class DataBaseFactory
{
	private static final Logger log = Loggers.getLogger("DataBaseFactory");
	
	/** основной комбо пул подключений */
	private static BoneCP source;
	
	/** 
	 * Завершение использования указанного подключения.
	 *
	 * @param connection подключение, с каторым завершена работа.
	 */
	public static void closeConnection(Connection connection)
	{
		if(connection == null)
			return;
		
		try
		{
			connection.close();
		}
		catch(SQLException e)
		{
			log.warning(e);
		}
	}
	
	/**
	 * Закрыть подключение и запрос.
	 * 
	 * @param connection подключение, которое необходимо закрыть.
	 * @param statement запрос, который необходимо закрыть.
	 */
	public static void closeDatabaseCS(Connection connection, Statement statement)
	{
		closeStatement(statement);
		closeConnection(connection);
	}
	
	/**
	 * Закрыть подключение, запрос и результат.
	 * 
	 * @param connection подключение, которое необходимо закрыть.
	 * @param statement запрос, который необходимо закрыть.
	 * @param rset резуьтат, который необходимо закрыть.
	 */
	public static void closeDatabaseCSR(Connection connection, Statement statement, ResultSet rset)
	{
		closeResultSet(rset);
		closeStatement(statement);
		closeConnection(connection);
	}
	
	/**
	 * Закрыть запрос и результат.
	 * 
	 * @param statement запрос, который необходимо закрыть.
	 * @param rset результат, который необходимо закрыть.
	 */
	public static void closeDatabaseSR(Statement statement, ResultSet rset)
	{
		closeResultSet(rset);
		closeStatement(statement);
	}
	
	/**
	 * Закрыть результат.
	 * 
	 * @param rset результат, который необходимо закрыть.
	 */
	public static void closeResultSet(ResultSet rset)
	{
		if(rset == null)
			return;
		
		try
		{
			rset.close();
		}
		catch(SQLException e)
		{
			log.warning(e);
		}
	}
	
	/**
	 * Закрыть запрос.
	 * 
	 * @param statement запрос, который необходимо закрыть.
	 */
	public static void closeStatement(Statement statement)
	{
		if(statement == null)
			return;
		
		try
		{
			statement.close();
		}
		catch(SQLException e)
		{
			log.warning(e);
		}
	}

	/**
	 * Генерирует ключ для получения подключения для потоко, в котором происходит обращение к БД.
	 * 
	 * @return key сгенерированный ключ, в соотвествии с текущим потоком.
	 */
	public static int generateKey()
	{
		return Thread.currentThread().hashCode();
	}

	/**
	 * Метод получения подключения для работы с БД.
	 * 
	 * @return connection подключение, для работы с БД.
	 * @throws SQLException.
	 */
	public static Connection getConnection() throws SQLException
	{
		return source.getConnection();
	}

	/**
	 * Инициализация БД, если указанный конфиг пуст, будет использован по умолчанию.
	 * 
	 * @param newConfig новый конфиг, с которым нужно инициализировать БД.
	 * @throws SQLException
	 */
	public static synchronized void start(BoneCPConfig config, String driver) throws SQLException
	{
		try
		{
			Class.forName(driver).newInstance();

			source = new BoneCP(config);
			source.getConnection().close();
		}
		catch(Exception e)
		{
			throw new SQLException("could not init DB connection:" + e);
		}
	}

	/**
	 * Выключение работы сервера с бд.
	 */
	public static synchronized void stop()
	{
		//выключаем пул
		source.close();
		//выключаем пул
		source.shutdown();
	}
}