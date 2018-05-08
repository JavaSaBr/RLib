package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The entry of {@link IntegerDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public class IntegerEntry<V> implements Reusable {

    /**
     * The next entry.
     */
    private IntegerEntry<V> next;

    /**
     * The value of this entry.
     */
    private V value;

    /**
     * The key of this entry.
     */
    private int key;

    /**
     * The hash of the key.
     */
    private int hash;

    @Override
    public boolean equals(final Object object) {
        if (object == null || object.getClass() != IntegerEntry.class) return false;

        final IntegerEntry<?> entry = (IntegerEntry<?>) object;

        final int firstKey = getKey();
        final int secondKey = entry.getKey();

        if (firstKey != secondKey) return false;

        final Object firstValue = getValue();
        final Object secondValue = entry.getValue();

        return Objects.equals(firstValue, secondValue);
    }

    @Override
    public void free() {
        value = null;
        next = null;
    }

    /**
     * Gets hash.
     *
     * @return the hash of the key.
     */
    public int getHash() {
        return hash;
    }

    /**
     * Gets key.
     *
     * @return the key of this entry.
     */
    public int getKey() {
        return key;
    }

    /**
     * Gets next.
     *
     * @return the next entry.
     */
    @Nullable
    public IntegerEntry<V> getNext() {
        return next;
    }

    /**
     * Sets next.
     *
     * @param next the next entry.
     */
    public void setNext(@Nullable final IntegerEntry<V> next) {
        this.next = next;
    }

    /**
     * Gets value.
     *
     * @return the value of this entry.
     */
    @Nullable
    public V getValue() {
        return value;
    }

    @Override
    public final int hashCode() {
        return (key ^ (value == null ? 0 : value.hashCode()));
    }

    /**
     * Set.
     *
     * @param hash  the hash
     * @param key   the key
     * @param value the value
     * @param next  the next
     */
    public void set(final int hash, final int key, final V value, final IntegerEntry<V> next) {
        this.value = value;
        this.next = next;
        this.key = key;
        this.hash = hash;
    }

    /**
     * Sets value.
     *
     * @param value the new value of this entry.
     * @return the old value of null.
     */
    @Nullable
    public V setValue(@Nullable final V value) {
        final V old = getValue();
        this.value = value;
        return old;
    }

    @Override
    public final String toString() {
        return "Entry : " + key + " = " + value;
    }
}
