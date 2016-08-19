package rlib.database.sql.builder.impl;

import rlib.database.sql.builder.PrepareSqlBuilder;
import rlib.database.sql.builder.args.SelectSqlOperatorArgs;
import rlib.database.sql.builder.operator.DeleteSqlOperator;
import rlib.database.sql.builder.operator.InsertSqlOperator;
import rlib.database.sql.builder.operator.SelectSqlOperator;
import rlib.database.sql.builder.operator.UpdateSqlOperator;
import rlib.database.sql.builder.operator.impl.MySqlSelectSqlOperator;

/**
 * Реализация конструктора SQL запроса под MySQL.
 *
 * @author JavaSaBr
 */
public class MySqlBuilder implements PrepareSqlBuilder {

    @Override
    public DeleteSqlOperator delete() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InsertSqlOperator insert() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SelectSqlOperator select(final SelectSqlOperatorArgs args) {
        final MySqlSelectSqlOperator operator = new MySqlSelectSqlOperator();
        args.applyArgs(operator);
        return operator;
    }

    @Override
    public UpdateSqlOperator update() {
        // TODO Auto-generated method stub
        return null;
    }
}
