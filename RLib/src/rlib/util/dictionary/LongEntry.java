package rlib.util.dictionary;

import rlib.util.pools.Foldable;

/**
 * Реализация ячейки для словарей с примитивным ключем long.
 *
 * @author Ronn
 */
public class LongEntry<V> implements Foldable {

    /**
     * следующая ячейка
     */
    private LongEntry<V> next;
    /**
     * значение
     */
    private V value;

    /**
     * ключ
     */
    private long key;
    /**
     * хэш ключа
     */
    private int hash;

    @Override
    public boolean equals(final Object object) {

        if (object == null || object.getClass() != LongEntry.class) {
            return false;
        }

        final LongEntry<?> entry = (LongEntry<?>) object;

        final long firstKey = getKey();
        final long secondKey = entry.getKey();

        if (firstKey == secondKey) {

            final Object firstValue = getValue();
            final Object secondValue = entry.getValue();

            if (firstValue == secondValue || firstValue != null && firstValue.equals(secondValue)) {
                return true;
            }
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
    public long getKey() {
        return key;
    }

    /**
     * @return следующая ячейка.
     */
    public LongEntry<V> getNext() {
        return next;
    }

    /**
     * @param next следующая цепочка.
     */
    public void setNext(final LongEntry<V> next) {
        this.next = next;
    }

    /**
     * @return значение ячейки.
     */
    public V getValue() {
        return value;
    }

    @Override
    public final int hashCode() {
        return (int) (key ^ (value == null ? 0 : value.hashCode()));
    }

    @Override
    public void reinit() {
        hash = 0;
    }

    public void set(final int hash, final long key, final V value, final LongEntry<V> next) {
        this.value = value;
        this.next = next;
        this.key = key;
        this.hash = hash;
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