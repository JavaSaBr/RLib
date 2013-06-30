package rlib.util.table;

/**
 * Интерфейс для реализации функции на значение в таблице.
 *
 * @author Ronn
 */
public interface FuncKeyValue<K, V>
{
	/**
	 * Применить функцию на указанное значение.
	 *
	 * @param key ключ этого значения.
	 * @param value значение в таблице.
	 */
	public void apply(K key, V value);
}
