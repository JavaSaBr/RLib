package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;

/**
 * The fast implementation of {@link LongDictionary} without threadsafe supporting.
 *
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class FastLongDictionary<V> extends AbstractLongDictionary<V> {

    private @NotNull LongEntry<V>[] entries;

    private int threshold;
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
        this.entries = ArrayUtils.create(getEntryType(), initCapacity);
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
    public LongEntry<V> @NotNull [] entries() {
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
