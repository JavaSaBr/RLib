package rlib.database.sql.builder.operator;

import rlib.database.sql.builder.args.WhereSqlConditionArgs;
import rlib.database.sql.builder.condition.WhereSqlCondition;

/**
 * Интерфейс для реализации оператора запроса в SQL.
 *
 * @author JavaSaBr
 */
public interface SqlOperator {

    /**
     * @return можно ли выполнять такой запрос.
     */
    public boolean isReady();

    /**
     * Указание лимита записей, на который может быть применят оператор.
     *
     * @param limit лимит записей для примиения.
     */
    public void limit(long limit);

    /**
     * Указание кол-во пропущенных записей, на которые должен был применет оператор.
     *
     * @param offset кол-во пропущенных записей.
     */
    public void offset(long offset);

    /**
     * Сформировать условие WHERE для оператора запроса.
     *
     * @param args аргументы к условию.
     * @return конструктора условия.
     */
    public WhereSqlCondition where(WhereSqlConditionArgs args);
}
