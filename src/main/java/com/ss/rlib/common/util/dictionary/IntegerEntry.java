package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The entry of {@link IntegerDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class IntegerEntry<V> implements Entry<IntegerEntry<V>, V> {

    /**
     * The next entry.
     */
    @Nullable
    private IntegerEntry<V> next;

    /**
     * The value of this entry.
     */
    @NotNull
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
    public boolean equals(@Nullable Object object) {

        if (object == null || object.getClass() != IntegerEntry.class) {
            return false;
        }

        IntegerEntry<?> entry = (IntegerEntry<?>) object;

        int firstKey = getKey();
        int secondKey = entry.getKey();

        if (firstKey != secondKey) {
            return false;
        }

        Object firstValue = getValue();
        Object secondValue = entry.getValue();

        return Objects.equals(firstValue, secondValue);
    }

    @Override
    public void free() {
        value = null;
        next = null;
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
    public int getKey() {
        return key;
    }

    @Override
    public @Nullable IntegerEntry<V> getNext() {
        return next;
    }

    @Override
    public void setNext(@Nullable IntegerEntry<V> next) {
        this.next = next;
    }

    @Override
    public @NotNull V getValue() {
        return value;
    }

    @Override
    public final int hashCode() {
        return key ^ value.hashCode();
    }

    /**
     * Set all fields of this entry.
     *
     * @param hash  the hash.
     * @param key   the key.
     * @param value the value.
     * @param next  the next.
     */
    public void set(int hash, int key, @NotNull V value, @Nullable IntegerEntry<V> next) {
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
