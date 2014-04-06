package rlib.database.sql.builder.operator;

/**
 * Интерфейс для реализации оператора SELECT.
 * 
 * @author Ronn
 */
public interface SelectSqlOperator extends SqlOperator {

	/**
	 * Добавление полей в результат запрооса, если поля не указаны, будут
	 * браться ВСЕ.
	 * 
	 * @param fieldNames названия колонок в таблицах.
	 */
	public void fields(String... fieldNames);

	/**
	 * Добавление таблиц, по которым будет происходить запрос.
	 * 
	 * @param tableName названия таблиц.
	 */
	public void from(String... tableNames);
}
