package rlib.database.sql.builder;

import rlib.database.sql.builder.args.SelectSqlOperatorArgs;
import rlib.database.sql.builder.operator.DeleteSqlOperator;
import rlib.database.sql.builder.operator.InsertSqlOperator;
import rlib.database.sql.builder.operator.SelectSqlOperator;
import rlib.database.sql.builder.operator.UpdateSqlOperator;

/**
 * Интерфейс для реализации конструктора SQL запроса.
 * 
 * @author Ronn
 */
public interface PrepareSqlBuilder {

	/**
	 * @return оператор для формирования SELECT запроса.
	 */
	public SelectSqlOperator select(SelectSqlOperatorArgs args);

	/**
	 * @return оператор для формирования DELETE запроса.
	 */
	public DeleteSqlOperator delete();

	/**
	 * @return оператор для формирования UPDATE запроса.
	 */
	public UpdateSqlOperator update();

	/**
	 * @return оператор для формирования INSERT запроса.
	 */
	public InsertSqlOperator insert();
}
