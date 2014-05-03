package rlib.util.table;

import java.util.function.BiConsumer;
import java.util.function.Function;

import rlib.util.array.Array;
import rlib.util.array.IntegerArray;
import rlib.util.array.LongArray;
import rlib.util.pools.Foldable;

/**
 * Интерфей для реализации таблицы.
 * 
 * @author Ronn
 * @created 27.02.2012
 */
public interface Table<K, V> extends Iterable<V>, Foldable {

	/** размер таблицы по умолчанию */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;

	/** максимальный размер таблицы */
	public static final int MAXIMUM_CAPACITY = 1 << 30;

	/** фактор загружености таблицы, для определения момента ее расширения */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * Обработать значения и ключи в таблице.
	 */
	public default void accept(final BiConsumer<? super K, ? super V> consumer) {
	}

	/**
	 * Применить функцию к таблице для обновления значений.
	 */
	public default void apply(final Function<? super V, V> function) {
	}

	/**
	 * Очищает таблицу
	 */
	public default void clear() {
	}

	/**
	 * Проверка наличия значения в таблице по указанному ключу.
	 * 
	 * @param key проверяемый ключ.
	 */
	public default boolean containsKey(final int key) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Проверка наличия значения в таблице по указанному ключу.
	 * 
	 * @param key проверяемый ключ.
	 */
	public default boolean containsKey(final K key) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Проверка наличия значения в таблице по указанному ключу.
	 * 
	 * @param key проверяемый ключ.
	 */
	public default boolean containsKey(final long key) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Проверка наличия указанного значения в таблице.
	 * 
	 * @param value проверяемое значение.
	 */
	public default boolean containsValue(final V value) {
		throw new RuntimeException("not supported.");
	}

	@Override
	public default void finalyze() {
		clear();
	}

	/**
	 * Вовзращает значения по указанному ключу.
	 * 
	 * @param key ключ.
	 */
	public default V get(final int key) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Вовзращает значения по указанному ключу.
	 * 
	 * @param key ключ.
	 */
	public default V get(final K key) {
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
	 * @return тип таблица по возможным ключам.
	 */
	public default TableType getType() {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @return пустая ли таблица.
	 */
	public default boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * @param container контейнер для ключей.
	 * @return массив ключей таблицы.
	 */
	public default Array<K> keyArray(final Array<K> container) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @return массив ключей таблицы.
	 */
	public default Array<K> keyArray(final Class<K> type) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @return массив ключей таблицы.
	 */
	public default IntegerArray keyIntegerArray() {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @param container контейнер для ключей.
	 * @return массив ключей таблицы.
	 */
	public default IntegerArray keyIntegerArray(final IntegerArray container) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @return массив ключей таблицы.
	 */
	public default LongArray keyLongArray() {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @param container контейнер для ключей.
	 * @return массив ключей таблицы.
	 */
	public default LongArray keyLongArray(final LongArray container) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Перенос данных в указанную таблицу.
	 * 
	 * @param table новый контейнер данных.
	 */
	public default void moveTo(final Table<? super K, ? super V> table) {

		if(getType() != table.getType()) {
			throw new IllegalArgumentException("incorrect table type.");
		}
	}

	/**
	 * Добавляет новое значение по указанному ключу, и если уже есть элемент с
	 * таким ключем, возвращает его.
	 * 
	 * @param key ключ значения.
	 * @param value вставляемое значение.
	 */
	public default V put(final int key, final V value) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Добавляет новое значение по указанному ключу, и если уже есть элемент с
	 * таким ключем, возвращает его.
	 * 
	 * @param key ключ значения.
	 * @param value вставляемое значение.
	 */
	public default V put(final K key, final V value) {
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
	 * Вставка в таблицу таблицы.
	 * 
	 * @param table вставляемая таблица.
	 */
	public default void put(final Table<K, V> table) {
		table.moveTo(this);
	}

	/**
	 * Блокировка изменение таблицы на время чтения его.
	 */
	public default void readLock() {
	}

	/**
	 * Разблокировка изменения таблицы.
	 */
	public default void readUnlock() {
	}

	/**
	 * Удаляет значение по ключу.
	 * 
	 * @param key ключ значения.
	 */
	public default V remove(final int key) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Удаляет значение по ключу.
	 * 
	 * @param key ключ значения.
	 */
	public default V remove(final K key) {
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

	/**
	 * @return кол-во значений в таблице.
	 */
	public default int size() {
		return 0;
	}

	/**
	 * @param container контейнер значений.
	 * @return итоговый список всех значений.
	 */
	public default Array<V> values(final Array<V> container) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @return итоговый список всех значений.
	 */
	public default Array<V> values(final Class<V> type) {
		throw new RuntimeException("not supported.");
	}

	/**
	 * Блокировка чтений для изменения таблицы.
	 */
	public default void writeLock() {
	}

	/**
	 * Разблокировка чтения таблицы.
	 */
	public default void writeUnlock() {
	}
}
