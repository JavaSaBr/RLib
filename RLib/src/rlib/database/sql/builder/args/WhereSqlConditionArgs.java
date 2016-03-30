package rlib.database.sql.builder.args;

import rlib.database.sql.builder.condition.WhereSqlCondition;

/**
 * Интерфейс для указания необходимых условию WHERE аргументов.
 *
 * @author Ronn
 */
public interface WhereSqlConditionArgs extends SqlConditionArgs<WhereSqlCondition> {

    @Override
    public void applyArgsImpl(WhereSqlCondition condition);
}
