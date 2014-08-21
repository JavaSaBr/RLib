package rlib.util.table;

import java.util.Objects;

import rlib.util.pools.Foldable;

/**
 * Реализация элемента для конкурентных таблиц.
 * 
 * @author Ronn
 */
public class ConcurrentIntegerTableEntry<V> implements Foldable {

	/** следующая ячейка */
	private volatile ConcurrentIntegerTableEntry<V> next;

	/** значение */
	private volatile V value;

	/** хэш ключа */
	private volatile int hash;
	/** ключ */
	private volatile int key;

	@Override
	public boolean equals(final Object object) {

		if(object == null || object.getClass() != ConcurrentIntegerTableEntry.class) {
			return false;
		}

		final ConcurrentIntegerTableEntry<?> entry = (ConcurrentIntegerTableEntry<?>) object;

		final int firstKey = getKey();
		final int secondKey = entry.getKey();

		if(firstKey == secondKey) {

			final Object firstValue = getValue();
			final Object secondValue = entry.getValue();

			return Objects.equals(secondValue, firstValue);
		}

		return false;
	}

	@Override
	public void finalyze() {
		value = null;
		next = null;
		key = 0;
		hash = 0;
	}

	/**
	 * @return хэш ячейки.
	 */
	public int getHash() {
		return hash;
	}

	/**
	 * @return ключ ячейки.
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @return следующая ячейка.
	 */
	public ConcurrentIntegerTableEntry<V> getNext() {
		return next;
	}

	/**
	 * @return значение ячейки.
	 */
	public V getValue() {
		return value;
	}

	@Override
	public final int hashCode() {
		return key ^ (value == null ? 0 : value.hashCode());
	}

	@Override
	public void reinit() {
		hash = 0;
	}

	public void set(final int hash, final int key, final V value, final ConcurrentIntegerTableEntry<V> next) {
		this.value = value;
		this.next = next;
		this.key = key;
		this.hash = hash;
	}

	/**
	 * @param next следующая цепочка.
	 */
	public void setNext(final ConcurrentIntegerTableEntry<V> next) {
		this.next = next;
	}

	/**
	 * Установка нового значения.
	 *
	 * @param value новое значение.
	 * @return старое значение.
	 */
	public V setValue(final V value) {
		final V old = getValue();
		this.value = value;
		return old;
	}

	@Override
	public final String toString() {
		return "Entry : " + key + " = " + value;
	}
}
