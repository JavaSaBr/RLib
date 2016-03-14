package rlib.util.dictionary;

import java.util.Objects;

import rlib.util.pools.Foldable;

/**
 * Реализация ячейки для словарей с ключем примитива int.
 *
 * @author Ronn
 */
public class IntegerDictionaryEntry<V> implements Foldable {

    /**
     * Следующая ячейка.
     */
    private IntegerDictionaryEntry<V> next;

    /**
     * Значение.
     */
    private V value;

    /**
     * Хэш ключа.
     */
    private int hash;

    /**
     * Ключ.
     */
    private int key;

    @Override
    public boolean equals(final Object object) {

        if (object == null || object.getClass() != IntegerDictionaryEntry.class) {
            return false;
        }

        final IntegerDictionaryEntry<?> entry = (IntegerDictionaryEntry<?>) object;

        final int firstKey = getKey();
        final int secondKey = entry.getKey();

        if (firstKey == secondKey) {

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
    public IntegerDictionaryEntry<V> getNext() {
        return next;
    }

    /**
     * @param next следующая ячейка.
     */
    public void setNext(final IntegerDictionaryEntry<V> next) {
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
        return key ^ (value == null ? 0 : value.hashCode());
    }

    @Override
    public void reinit() {
        hash = 0;
    }

    public void set(final int hash, final int key, final V value, final IntegerDictionaryEntry<V> next) {
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
