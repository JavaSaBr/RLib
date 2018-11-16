package com.ss.rlib.common.util.dictionary;

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
public class ObjectEntry<K, V> implements Entry<ObjectEntry<K, V>, V> {

    /**
     * The next entry.
     */
    @Nullable
    private ObjectEntry<K, V> next;

    /**
     * The key of this entry.
     */
    @NotNull
    private K key;

    /**
     * The value of this entry.
     */
    @NotNull
    private V value;

    /**
     * The hash of the key.
     */
    private int hash;

    @Override
    public boolean equals(@Nullable Object object) {

        if (object == null || object.getClass() != ObjectEntry.class) {
            return false;
        }

        ObjectEntry<?, ?> entry = (ObjectEntry<?, ?>) object;

        Object firstKey = getKey();
        Object secondKey = entry.getKey();

        if (!Objects.equals(firstKey, secondKey)) {
            return false;
        }

        Object firstValue = getValue();
        Object secondValue = entry.getValue();

        return Objects.equals(firstValue, secondValue);
    }

    @Override
    public void free() {
        key = null;
        value = null;
        next = null;
        hash = 0;
    }

    @Override
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

    @Override
    public @Nullable ObjectEntry<K, V> getNext() {
        return next;
    }

    @Override
    public void setNext(@Nullable ObjectEntry<K, V> next) {
        this.next = next;
    }

    @Override
    public @NotNull V getValue() {
        return value;
    }

    @Override
    public final int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    @Override
    public void reuse() {
        hash = 0;
    }

    /**
     * Set all fields of this entry.
     *
     * @param hash  the hash.
     * @param key   the key.
     * @param value the value.
     * @param next  the next entry.
     */
    public void set(int hash, @NotNull K key, @NotNull V value, @Nullable ObjectEntry<K, V> next) {
        this.value = value;
        this.next = next;
        this.key = key;
        this.hash = hash;
    }

    @Override
    public @NotNull V setValue(@NotNull V value) {
        V old = getValue();
        this.value = value;
        return old;
    }

    @Override
    public final String toString() {
        return "Entry : " + key + " = " + value;
    }
}