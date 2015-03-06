package rlib.util.dictionary;

import rlib.util.array.LongArray;

/**
 * Интерфейс для реализации словаря с примитивным ключем long.
 * 
 * @author Ronn
 */
public interface LongDictionary<V> extends Dictionary<LongKey, V> {

	/**
	 * Проверка наличия значения в словаре по указанному ключу.
	 * 
	 * @param key проверяемый ключ.
	 */
	public default boolean containsKey(final long key) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Вовзращает значения по указанному ключу.
	 * 
	 * @param key ключ.
	 */
	public default V get(final long key) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @return массив ключей словаря.
	 */
	public default LongArray keyLongArray() {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @param container контейнер для ключей.
	 * @return массив ключей словаря.
	 */
	public default LongArray keyLongArray(final LongArray container) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Добавляет новое значение по указанному ключу, и если уже есть элемент с
	 * таким ключем, возвращает его.
	 * 
	 * @param key ключ значения.
	 * @param value вставляемое значение.
	 */
	public default V put(final long key, final V value) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Удаляет значение по ключу.
	 * 
	 * @param key ключ значения.
	 */
	public default V remove(final long key) {
		throw new RuntimeException("not supported.");
	}

}
