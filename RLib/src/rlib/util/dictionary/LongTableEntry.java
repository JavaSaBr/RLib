package rlib.util.dictionary;

import rlib.util.pools.Reusable;

/**
 * Реализация чеяки таблицы для таблиц с примитивным long ключем.
 *
 * @author JavaSaBr
 */
public class LongTableEntry<V> implements Reusable {

    /**
     * Следующая ячейка.
     */
    private LongTableEntry<V> next;

    /**
     * Значение.
     */
    private V value;

    /**
     * Ключ.
     */
    private long key;

    /**
     * Хэш ключа.
     */
    private int hash;

    @Override
    public boolean equals(final Object object) {

        if (object == null || object.getClass() != LongTableEntry.class) {
            return false;
        }

        final LongTableEntry<?> entry = (LongTableEntry<?>) object;

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
    public void free() {
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
    public LongTableEntry<V> getNext() {
        return next;
    }

    /**
     * @param next следующая цепочка.
     */
    public void setNext(final LongTableEntry<V> next) {
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
    public void reuse() {
        hash = 0;
    }

    public void set(final int hash, final long key, final V value, final LongTableEntry<V> next) {
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
