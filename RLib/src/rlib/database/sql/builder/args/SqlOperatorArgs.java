package rlib.database.sql.builder.args;

import rlib.database.sql.builder.operator.SqlOperator;

/**
 * Интерфейс для реализации аргументов к оператору SQL.
 * 
 * @author Ronn
 */
public interface SqlOperatorArgs<T extends SqlOperator> {

	/**
	 * Выдача необходимых оператору аргументов.
	 * 
	 * @param operator SQL оператор.
	 */
	public default void applyArgs(final T operator) {

		applyArgsImpl(operator);

		if(!operator.isReady()) {
			throw new IllegalArgumentException("not mandatory args for operator " + operator);
		}
	}

	/**
	 * Реализация выдачи необходимых оператору аргументов.
	 * 
	 * @param operator SQL оператор.
	 */
	public void applyArgsImpl(T operator);
}
