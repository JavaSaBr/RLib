package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.util.pools.Reusable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The entry of {@link ObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class ObjectEntry<K, V> implements Reusable {

    /**
     * The next entry.
     */
    private ObjectEntry<K, V> next;

    /**
     * The key of this entry.
     */
    private K key;

    /**
     * The value of this entry.
     */
    private V value;

    /**
     * The hash of the key.
     */
    private int hash;

    @Override
    public boolean equals(final Object object) {

        if (object == null || object.getClass() != ObjectEntry.class) {
            return false;
        }

        final ObjectEntry<?, ?> entry = (ObjectEntry<?, ?>) object;

        final Object firstKey = getKey();
        final Object secondKey = entry.getKey();

        if (!Objects.equals(firstKey, secondKey)) {
            return false;
        }

        final Object firstValue = getValue();
        final Object secondValue = entry.getValue();

        return Objects.equals(firstValue, secondValue);
    }

    @Override
    public void free() {
        key = null;
        value = null;
        next = null;
        hash = 0;
    }

    /**
     * Get the hash.
     *
     * @return the hash.
     */
    public int getHash() {
        return hash;
    }

    /**
     * Get the key.
     *
     * @return the key.
     */
    public @NotNull K getKey() {
        return key;
    }

    /**
     * Get the next entry.
     *
     * @return the next entry.
     */
    public @Nullable ObjectEntry<K, V> getNext() {
        return next;
    }

    /**
     * Set the next entry.
     *
     * @param next the next entry.
     */
    public void setNext(@Nullable final ObjectEntry<K, V> next) {
        this.next = next;
    }

    /**
     * Get the value.
     *
     * @return the value.
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

    /**
     * Set data of this entry.
     *
     * @param hash  the hash.
     * @param key   the key.
     * @param value the value.
     * @param next  the next entry.
     */
    public void set(final int hash, @NotNull final K key, @Nullable final V value, @Nullable final ObjectEntry<K, V> next) {
        this.value = value;
        this.next = next;
        this.key = key;
        this.hash = hash;
    }

    /**
     * Set the value.
     *
     * @param value the new value of this entry.
     * @return the old value of null.
     */
    public @Nullable V setValue(@Nullable final V value) {
        final V old = getValue();
        this.value = value;
        return old;
    }

    @Override
    public final String toString() {
        return "Entry : " + key + " = " + value;
    }
}