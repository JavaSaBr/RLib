package rlib.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import rlib.logging.Logger;
import rlib.logging.Loggers;
import rlib.util.array.Array;
import rlib.util.array.Arrays;


/**
 * Менеджер для очистки БД от ненужных записей.
 * Хранит список запросов необходимых для очистки БД.
 * 
 * @author Ronn
 */
public abstract class CleaningManager
{
	private static final Logger log = Loggers.getLogger(CleaningManager.class);
	
	/** список запросов для очистки */
	public static final Array<CleaningQuery> query = Arrays.toArray(CleaningQuery.class);
	
	/**
	 * Добавление запроса для очистки БД.
	 * 
	 * @param name название таблицы.
	 * @param squery запрос для очистки.
	 */
	public static void addQuery(String name, String squery)
	{
		query.add(new CleaningQuery(name, squery));
	}
	
	/**
	 * Очистка БД.
	 */
	public static void cleaning(ConnectFactory connects)
	{
		Connection con = null;
		Statement statement = null;
		
		try
		{
			// получаем коннект к БД
			con = connects.getConnection();
			// создаем запрос
			statement = con.createStatement();
			
			for(CleaningQuery clean : query)
				// исполняем запрос
				log.info(clean.getName().replace("{count}", String.valueOf(statement.executeUpdate(clean.getQuery()))) + ".");
		}
		catch(SQLException e)
		{
			log.warning(e);
		}
		finally
		{
			DBUtils.closeDatabaseCS(con, statement);
		}
	}
}
