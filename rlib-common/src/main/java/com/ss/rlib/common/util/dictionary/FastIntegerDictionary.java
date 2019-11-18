package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;

/**
 * The fast implementation of {@link IntegerDictionary} without threadsafe supporting.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public class FastIntegerDictionary<V> extends AbstractIntegerDictionary<V> implements UnsafeIntegerDictionary<V> {

    private @NotNull IntegerEntry<V>[] entries;

    private int threshold;
    private int size;

    protected FastIntegerDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected FastIntegerDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected FastIntegerDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected FastIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
        this.entries = ArrayUtils.create(getEntryType(), initCapacity);
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setEntries(@NotNull IntegerEntry<V>[] entries) {
        this.entries = entries;
    }

    @Override
    public IntegerEntry<V> @NotNull [] entries() {
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
