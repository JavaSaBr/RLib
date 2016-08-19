package rlib.database.sql.builder.condition;

/**
 * Интерфейс для реализации SQL условия для исполнение SQL оператором.
 *
 * @author JavaSaBr
 */
public interface SqlCondition {

    public boolean isReady();
}
