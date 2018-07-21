package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link ConcurrentObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public abstract class AbstractConcurrentObjectDictionary<K, V> extends AbstractObjectDictionary<K, V>
        implements ConcurrentObjectDictionary<K, V> {

    /**
     * Тhe array of entries.
     */
    @NotNull
    private volatile ObjectEntry<K, V>[] entries;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private volatile int threshold;

    /**
     * The count of values in this {@link Dictionary}.
     */
    private volatile int size;

    protected AbstractConcurrentObjectDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentObjectDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentObjectDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected AbstractConcurrentObjectDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    public void setEntries(@NotNull ObjectEntry<K, V>[] entries) {
        this.entries = entries;
    }

    @Override
    public @NotNull ObjectEntry<K, V>[] entries() {
        return entries;
    }

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    protected void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Override
    protected int decrementSizeAndGet() {
        size -= 1;
        return size;
    }

    @Override
    protected int incrementSizeAndGet() {
        size += 1;
        return size;
    }

    @Override
    public final int size() {
        return size;
    }
}
