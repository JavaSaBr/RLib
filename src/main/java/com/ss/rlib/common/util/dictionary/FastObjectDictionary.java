package com.ss.rlib.common.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The fast implementation of {@link ObjectDictionary} without threadsafe supporting.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public class FastObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> {

    /**
     * The array of entries.
     */
    private ObjectEntry<K, V>[] content;

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private int threshold;

    /**
     * The count of values in this {@link Dictionary}.
     */
    private int size;

    protected FastObjectDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected FastObjectDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected FastObjectDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected FastObjectDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    public void setSize(final int size) {
        this.size = size;
    }

    @Override
    public void setContent(@NotNull final ObjectEntry<K, V>[] content) {
        this.content = content;
    }

    @Override
    public @NotNull ObjectEntry<K, V>[] content() {
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
    public int size() {
        return size;
    }
}
