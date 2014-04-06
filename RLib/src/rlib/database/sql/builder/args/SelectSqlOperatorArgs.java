package rlib.database.sql.builder.args;

import rlib.database.sql.builder.operator.SelectSqlOperator;

/**
 * Интерфейс для указания SQL оператору необходимых аргументов.
 * 
 * @author Ronn
 */
@FunctionalInterface
public interface SelectSqlOperatorArgs extends SqlOperatorArgs<SelectSqlOperator> {

	@Override
	public void applyArgsImpl(SelectSqlOperator operator);
}
