package com.ss.rlib.util.dictionary;

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
    private LongEntry<V>[] content;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private int threshold;

    /**
     * The count of values in this {@link Dictionary}.
     */
    private int size;

    /**
     * Instantiates a new Fast long dictionary.
     */
    protected FastLongDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Fast long dictionary.
     *
     * @param loadFactor the load factor
     */
    protected FastLongDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Instantiates a new Fast long dictionary.
     *
     * @param initCapacity the init capacity
     */
    protected FastLongDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    /**
     * Instantiates a new Fast long dictionary.
     *
     * @param loadFactor   the load factor
     * @param initCapacity the init capacity
     */
    protected FastLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    public void setSize(final int size) {
        this.size = size;
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
    public void setThreshold(final int threshold) {
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
