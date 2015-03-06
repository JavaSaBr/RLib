package rlib.util.dictionary;

import java.util.function.BiConsumer;
import java.util.function.Function;

import rlib.util.array.Array;
import rlib.util.pools.Foldable;

/**
 * Интерфей для реализации словаря.
 * 
 * @author Ronn
 * @created 27.02.2012
 */
public interface Dictionary<K, V> extends Iterable<V>, Foldable {

	/** размер словаря по умолчанию */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;

	/** максимальный размер словаря */
	public static final int MAXIMUM_CAPACITY = 1 << 30;

	/** фактор загружености словаря, для определения момента ее расширения */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * Обработать значения и ключи в словаре.
	 */
	public default void accept(final BiConsumer<? super K, ? super V> consumer) {
	}

	/**
	 * Применить функцию к словарю для обновления значений.
	 */
	public default void apply(final Function<? super V, V> function) {
	}

	/**
	 * Очищает словарь
	 */
	public default void clear() {
		if(size() > 0) {
			clear();
		}
	}

	/**
	 * Проверка наличия указанного значения в словаре.
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
	 * @return тип словаря по возможным ключам.
	 */
	public default TableType getType() {
		throw new RuntimeException("not supported.");
	}

	/**
	 * @return пустой ли словарь.
	 */
	public default boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Перенос данных в указанный словарь.
	 * 
	 * @param dictionary новый контейнер данных.
	 */
	public default void moveTo(final Dictionary<? super K, ? super V> dictionary) {

		if(getType() != dictionary.getType()) {
			throw new IllegalArgumentException("incorrect table type.");
		}
	}

	/**
	 * Вставка в всловарь данных из другого словаря.
	 * 
	 * @param dictionary вставляемый словарь.
	 */
	public default void put(final Dictionary<K, V> dictionary) {
		dictionary.moveTo(this);
	}

	/**
	 * @return кол-во значений в словаре.
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
}
