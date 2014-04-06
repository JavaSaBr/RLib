package rlib.database.sql.builder.args;

import rlib.database.sql.builder.condition.SqlCondition;

/**
 * Интерфейс для реализации аргументов к условия оператора.
 * 
 * @author Ronn
 */
public interface SqlConditionArgs<T extends SqlCondition> {

	/**
	 * Выдача необходимых условию аргументов.
	 * 
	 * @param condition условия SQL оператора.
	 */
	public default void applyArgs(T condition) {

		applyArgsImpl(condition);

		if(!condition.isReady()) {
			throw new IllegalArgumentException("not mandatory args for condition " + condition);
		}
	}

	/**
	 * Реализация выдачи необходимых оператору аргументов.
	 * 
	 * @param condition условия SQL оператора.
	 */
	public void applyArgsImpl(T condition);
}
