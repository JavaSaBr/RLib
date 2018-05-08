package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link ConcurrentLongDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractConcurrentLongDictionary<V> extends AbstractLongDictionary<V> implements ConcurrentLongDictionary<V> {

    /**
     * Тhe array of entries.
     */
    private volatile LongEntry<V>[] content;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private volatile int threshold;

    /**
     * The count of values in this {@link Dictionary}.
     */
    private volatile int size;

    /**
     * Instantiates a new Abstract concurrent long dictionary.
     */
    protected AbstractConcurrentLongDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Abstract concurrent long dictionary.
     *
     * @param loadFactor the load factor
     */
    protected AbstractConcurrentLongDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Abstract concurrent long dictionary.
     *
     * @param initCapacity the init capacity
     */
    protected AbstractConcurrentLongDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    /**
     * Instantiates a new Abstract concurrent long dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    protected AbstractConcurrentLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    public void setContent(@NotNull final LongEntry<V>[] content) {
        this.content = content;
    }

    @NotNull
    @Override
    public LongEntry<V>[] content() {
        return content;
    }

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    protected void setSize(final int size) {
        this.size = size;
    }

    @Override
    public void setThreshold(final int threshold) {
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
