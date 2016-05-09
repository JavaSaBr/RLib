package rlib.util.dictionary;

import java.util.Objects;

import rlib.util.pools.Reusable;

/**
 * Реализация элемента словаря с объектным ключем.
 *
 * @author Ronn
 */
public class ObjectDictionaryEntry<K, V> implements Reusable {

    /**
     * Следующая ячейка.
     */
    private ObjectDictionaryEntry<K, V> next;

    /**
     * Ключ.
     */
    private K key;

    /**
     * Значение.
     */
    private V value;

    /**
     * Хэш ключа.
     */
    private int hash;

    @Override
    public boolean equals(final Object object) {

        if (object == null || object.getClass() != ObjectDictionaryEntry.class) {
            return false;
        }

        final ObjectDictionaryEntry<?, ?> entry = (ObjectDictionaryEntry<?, ?>) object;

        final Object firstKey = getKey();
        final Object secondKey = entry.getKey();

        if (Objects.equals(firstKey, secondKey)) {

            final Object firstValue = getValue();
            final Object secondValue = entry.getValue();

            return Objects.equals(firstValue, secondValue);
        }

        return false;
    }

    @Override
    public void free() {
        key = null;
        value = null;
        next = null;
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
    public K getKey() {
        return key;
    }

    /**
     * @return следующая ячейка.
     */
    public ObjectDictionaryEntry<K, V> getNext() {
        return next;
    }

    /**
     * @param next следующая цепочка.
     */
    public void setNext(final ObjectDictionaryEntry<K, V> next) {
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
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    @Override
    public void reuse() {
        hash = 0;
    }

    public void set(final int hash, final K key, final V value, final ObjectDictionaryEntry<K, V> next) {
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