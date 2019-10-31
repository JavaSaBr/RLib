package com.ss.rlib.common.util.dictionary;

import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.util.ArrayUtils;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link ConcurrentIntegerDictionary}.
 *
 * @param <V> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractConcurrentIntegerDictionary<V> extends AbstractIntegerDictionary<V> implements
    ConcurrentIntegerDictionary<V> {

    private final @NotNull AtomicInteger size;

    private volatile @NotNull IntegerEntry<V>[] entries;

    private volatile int threshold;

    protected AbstractConcurrentIntegerDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentIntegerDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractConcurrentIntegerDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected AbstractConcurrentIntegerDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
        this.entries = ArrayUtils.create(getEntryType(), initCapacity);
        this.size = new AtomicInteger(0);
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
    public int getThreshold() {
        return threshold;
    }

    @Override
    protected void setSize(int size) {
        this.size.set(size);
    }

    @Override
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Override
    protected int decrementSizeAndGet() {
        return size.decrementAndGet();
    }

    @Override
    protected int incrementSizeAndGet() {
        return size.incrementAndGet();
    }

    @Override
    public final int size() {
        return size.get();
    }
}
