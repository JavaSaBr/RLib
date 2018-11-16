package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The entry of {@link LongDictionary}.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public final class LongEntry<V> implements Entry<LongEntry<V>, V> {

    /**
     * The next entry.
     */
    @Nullable
    private LongEntry<V> next;

    /**
     * The value of this entry.
     */
    @NotNull
    private V value;

    /**
     * The key of this entry.
     */
    private long key;
    /**
     * The hash of the key.
     */
    private int hash;

    @Override
    public boolean equals(@Nullable Object object) {

        if (object == null || object.getClass() != LongEntry.class) {
            return false;
        }

        LongEntry<?> entry = (LongEntry<?>) object;

        long firstKey = getKey();
        long secondKey = entry.getKey();

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
    public long getKey() {
        return key;
    }

    @Override
    public @Nullable LongEntry<V> getNext() {
        return next;
    }

    @Override
    public void setNext(@Nullable LongEntry<V> next) {
        this.next = next;
    }

    @Override
    public @NotNull V getValue() {
        return value;
    }

    @Override
    public final int hashCode() {
        return (int) (key ^ value.hashCode());
    }

    /**
     * Set all fields of this entry.
     *
     * @param hash  the hash.
     * @param key   the key.
     * @param value the value.
     * @param next  the next.
     */
    public void set(int hash, long key, @NotNull V value, @Nullable LongEntry<V> next) {
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
    public String toString() {
        return "LongEntry{" +
                "next=" + next +
                ", value=" + value +
                ", key=" + key +
                ", hash=" + hash +
                '}';
    }
}