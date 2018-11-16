package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The fast implementation of {@link LongDictionary} without threadsafe supporting.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public class FastLongDictionary<V> extends AbstractLongDictionary<V> {

    /**
     * The array of entries.
     */
    @NotNull
    private LongEntry<V>[] entries;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private int threshold;

    /**
     * The count of values in this {@link Dictionary}.
     */
    private int size;

    protected FastLongDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected FastLongDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected FastLongDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected FastLongDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setEntries(@NotNull LongEntry<V>[] content) {
        this.entries = content;
    }

    @Override
    public @NotNull LongEntry<V>[] entries() {
        return entries;
    }

    @Override
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    protected int decrementSizeAndGet() {
        return --size;
    }

    @Override
    protected int incrementSizeAndGet() {
        return ++size;
    }

    @Override
    public final int size() {
        return size;
    }
}
