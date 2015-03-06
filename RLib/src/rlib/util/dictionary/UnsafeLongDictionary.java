package rlib.util.dictionary;

/**
 * Интерфейс для реализации внутреннего API словаря с примитивным ключем long.
 * 
 * @author Ronn
 */
public interface UnsafeLongDictionary<V> extends LongDictionary<V> {

	/**
	 * @return массив ячеяк словаря.
	 */
	public LongEntry<V>[] content();

	/**
	 * Удаление значения из ячейки по указанному ключу.
	 * 
	 * @param key ключ ячейки.
	 * @return удаленная ячейка.
	 */
	public LongEntry<V> removeEntryForKey(long key);
}
