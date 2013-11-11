package rlib.util.table;

/**
 * Интерфейс для реализации функции на значение в таблице.
 * 
 * @author Ronn
 */
public interface FuncValue<V> {

	/**
	 * Применить функцию на указанное значение.
	 * 
	 * @param value значение в таблице.
	 */
	public void apply(V value);
}
