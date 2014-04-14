package rlib.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Набор утильных методов для работы с БД.
 * 
 * @author Ronn
 * @created 27.03.2012
 */
public final class DBUtils {

	private static final Logger LOGGER = LoggerManager.getLogger(DBUtils.class);

	/**
	 * Завершение использования указанного подключения.
	 * 
	 * @param connection подключение, с каторым завершена работа.
	 */
	public static void closeConnection(Connection connection) {

		if(connection == null) {
			return;
		}

		try {
			connection.close();
		} catch(SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * Закрыть подключение и запрос.
	 * 
	 * @param connection подключение, которое необходимо закрыть.
	 * @param statement запрос, который необходимо закрыть.
	 */
	public static void closeDatabaseCS(Connection connection, Statement statement) {
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
	public static void closeDatabaseCSR(Connection connection, Statement statement, ResultSet rset) {
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
	public static void closeDatabaseSR(Statement statement, ResultSet rset) {
		closeResultSet(rset);
		closeStatement(statement);
	}

	/**
	 * Закрыть результат.
	 * 
	 * @param rset результат, который необходимо закрыть.
	 */
	public static void closeResultSet(ResultSet rset) {

		if(rset == null) {
			return;
		}

		try {
			rset.close();
		} catch(SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * Закрыть запрос.
	 * 
	 * @param statement запрос, который необходимо закрыть.
	 */
	public static void closeStatement(Statement statement) {

		if(statement == null) {
			return;
		}

		try {
			statement.close();
		} catch(SQLException e) {
			LOGGER.warning(e);
		}
	}

	private DBUtils() {
		throw new IllegalArgumentException();
	}
}