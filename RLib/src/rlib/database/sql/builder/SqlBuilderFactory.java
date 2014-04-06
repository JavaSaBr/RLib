package rlib.database.sql.builder;

import rlib.database.sql.builder.impl.MySqlBuilder;

/**
 * Реализация фабрики конструкторов SQL запросов.
 * 
 * @author Ronn
 */
public final class SqlBuilderFactory {

	/**
	 * @return конструктор запросов для MySQL.
	 */
	public static final PrepareSqlBuilder newMySqlBuilder() {
		return new MySqlBuilder();
	}

	private SqlBuilderFactory() {
		throw new RuntimeException();
	}

	static void s() {

		PrepareSqlBuilder builder = newMySqlBuilder();
		builder.select(operator -> {
			operator.fields("item_id", "order");
			operator.from("table_name");
			operator.where(condition -> {
				condition.equals("object_id");
			});
			operator.limit(1);
		});

		String SQL = builder.toString();
	}
}
