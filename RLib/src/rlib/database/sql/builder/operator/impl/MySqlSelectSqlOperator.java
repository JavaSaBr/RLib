package rlib.database.sql.builder.operator.impl;

import rlib.database.sql.builder.args.WhereSqlConditionArgs;
import rlib.database.sql.builder.condition.WhereSqlCondition;
import rlib.database.sql.builder.operator.SelectSqlOperator;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * Реализация оператора SELECT для MySQL.
 *
 * @author Ronn
 */
public class MySqlSelectSqlOperator implements SelectSqlOperator {

    /**
     * список полей для запроса
     */
    private final Array<String> fields;
    /**
     * список таблиц для запроса
     */
    private final Array<String> tables;

    public MySqlSelectSqlOperator() {
        this.fields = ArrayFactory.newArray(String.class);
        this.tables = ArrayFactory.newArray(String.class);
    }

    @Override
    public void fields(final String... fieldNames) {
        for (final String fieldName : fieldNames) {

            if (fieldName.contains("*")) {
                throw new IllegalArgumentException("incorrect field name \"" + fieldName + "\"");
            }

            fields.add(fieldName);
        }
    }

    @Override
    public void from(final String... tableNames) {
        tables.addAll(tableNames);
    }

    @Override
    public boolean isReady() {
        return !tables.isEmpty();
    }

    @Override
    public void limit(final long limit) {
        // TODO Auto-generated method stub

    }

    @Override
    public void offset(final long offset) {
        // TODO Auto-generated method stub

    }

    @Override
    public WhereSqlCondition where(final WhereSqlConditionArgs args) {
        // TODO Auto-generated method stub
        return null;
    }
}
