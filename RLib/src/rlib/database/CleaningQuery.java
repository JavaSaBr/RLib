package rlib.database;

/**
 * Модель запроса для очистки таблицы.
 * 
 * @author Ronn
 */
public final class CleaningQuery {

	/** название таблицы, которую очищает запрос */
	private final String name;
	/** содержание запроса */
	private final String query;

	/**
	 * @param name имя таблицы
	 * @param query запрос
	 */
	public CleaningQuery(final String name, final String query) {
		this.name = name;
		this.query = query;
	}

	/**
	 * @return name название запроса.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return query содержание запроса.
	 */
	public String getQuery() {
		return query;
	}
}
