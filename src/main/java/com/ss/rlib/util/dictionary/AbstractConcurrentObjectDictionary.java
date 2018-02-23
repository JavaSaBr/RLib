package com.ss.rlib.util.dictionary;

import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link ConcurrentObjectDictionary}.
 *
 * @param <K> the key's type.
 * @param <V> the value's type.
 * @author JavaSaBr
 */
public abstract class AbstractConcurrentObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> implements ConcurrentObjectDictionary<K, V> {

    /**
     * Тhe array of entries.
     */
    private volatile ObjectEntry<K, V>[] content;

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

    protected AbstractConcurrentObjectDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentObjectDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected AbstractConcurrentObjectDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
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
