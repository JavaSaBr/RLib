package rlib.database.sql.builder.condition;

/**
 * Интерфейс для реализации условия WHERE в SQL.
 * 
 * @author Ronn
 */
public interface WhereSqlCondition extends SqlCondition {

	public void equals(String fieldName);

	public void gt(String fieldName);

	public void ge(String fieldName);

	public void le(String fieldName);

	public void lt(String fieldName);
}
