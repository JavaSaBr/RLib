package rlib.util.table;

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
public interface Table<K, V> extends Iterable<V>, Foldable
{
	/** размер таблицы по умолчанию */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;

	/** максимальный размер таблицы */
	public static final int MAXIMUM_CAPACITY = 1 << 30;

	/** фактор загружености таблицы, для определения момента ее расширения */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * Применить функцию на значения в таблице.
	 *
	 * @param func применяемая функция.
	 */
	public void apply(FuncKeyValue<K, V> func);

	/**
	 * Применить функцию на значения в таблице.
	 *
	 * @param func применяемая функция.
	 */
	public void apply(FuncValue<V> func);

	/**
	 * Очищает таблицу
	 */
	public void clear();

	/**
	 * Проверка наличия значения в таблице по указанному ключу.
	 *
	 * @param key проверяемый ключ.
	 */
	public boolean containsKey(int key);

	/**
	 * Проверка наличия значения в таблице по указанному ключу.
	 *
	 * @param key проверяемый ключ.
	 */
	public boolean containsKey(K key);

	/**
	 * Проверка наличия значения в таблице по указанному ключу.
	 *
	 * @param key проверяемый ключ.
	 */
	public boolean containsKey(long key);

	/**
	 * Проверка наличия указанного значения в таблице.
	 *
	 * @param value проверяемое значение.
	 */
	public boolean containsValue(V value);

	/**
	 * Вовзращает значения по указанному ключу.
	 *
	 * @param key ключ.
	 */
	public V get(int key);

	/**
	 * Вовзращает значения по указанному ключу.
	 *
	 * @param key ключ.
	 */
	public V get(K key);

	/**
	 * Вовзращает значения по указанному ключу.
	 *
	 * @param key ключ.
	 */
	public V get(long key);

	/**
	 * @return тип таблица по возможным ключам.
	 */
	public TableType getType();

	/**
	 * @return пустая ли таблица.
	 */
	public boolean isEmpty();

	/**
	 * @param container контейнер для ключей.
	 * @return массив ключей таблицы.
	 */
	public Array<K> keyArray(Array<K> container);

	/**
	 * @param container контейнер для ключей.
	 * @return массив ключей таблицы.
	 */
	public IntegerArray keyIntegerArray(IntegerArray container);

	/**
	 * @param container контейнер для ключей.
	 * @return массив ключей таблицы.
	 */
	public LongArray keyLongArray(LongArray container);

	/**
	 * Перенос данных в указанную таблицу.
	 *
	 * @param table новый контейнер данных.
	 */
	public void moveTo(Table<K, V> table);

	/**
	 * Добавляет новое значение по указанному ключу, и если уже есть элемент с таким ключем, возвращает его.
	 *
	 * @param key ключ значения.
	 * @param value вставляемое значение.
	 */
	public V put(int key, V value);

	/**
	 * Добавляет новое значение по указанному ключу, и если уже есть элемент с таким ключем, возвращает его.
	 *
	 * @param key ключ значения.
	 * @param value вставляемое значение.
	 */
	public V put(K key, V value);

	/**
	 * Добавляет новое значение по указанному ключу, и если уже есть элемент с таким ключем, возвращает его.
	 *
	 * @param key ключ значения.
	 * @param value вставляемое значение.
	 */
	public V put(long key, V value);

	/**
	 * Вставка в таблицу таблицы.
	 *
	 * @param table вставляемая таблица.
	 */
	public void put(Table<K, V> table);

	/**
	 * Блокировка изменение таблицы на время чтения его.
	 */
	public void readLock();

	/**
	 * Разблокировка изменения таблицы.
	 */
	public void readUnlock();

	/**
	 * Удаляет значение по ключу.
	 *
	 * @param key ключ значения.
	 */
	public V remove(int key);

	/**
	 * Удаляет значение по ключу.
	 *
	 * @param key ключ значения.
	 */
	public V remove(K key);

	/**
	 * Удаляет значение по ключу.
	 *
	 * @param key ключ значения.
	 */
	public V remove(long key);

	/**
	 * @return кол-во значений в таблице.
	 */
	public int size();

	/**
	 * Перенос всех значений в массив.
	 *
	 * @param container контейнер значений.
	 * @return итоговый список всех значений.
	 */
	public Array<V> values(Array<V> container);

	/**
	 * Блокировка чтений для изменения таблицы.
	 */
	public void writeLock();

	/**
	 * Разблокировка чтения таблицы.
	 */
	public void writeUnlock();
}
