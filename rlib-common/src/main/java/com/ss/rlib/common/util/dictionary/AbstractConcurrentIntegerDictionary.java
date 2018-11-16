package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link ConcurrentIntegerDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractConcurrentIntegerDictionary<V> extends AbstractIntegerDictionary<V>
        implements ConcurrentIntegerDictionary<V> {

    /**
     * Тhe array of entries.
     */
    @NotNull
    private volatile IntegerEntry<V>[] entries;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private volatile int threshold;

    /**
     * The count of values in this {@link Dictionary}.
     */
    private volatile int size;

    protected AbstractConcurrentIntegerDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentIntegerDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    protected AbstractConcurrentIntegerDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    public void setEntries(@NotNull IntegerEntry<V>[] entries) {
        this.entries = entries;
    }

    @Override
    public @NotNull IntegerEntry<V>[] entries() {
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
