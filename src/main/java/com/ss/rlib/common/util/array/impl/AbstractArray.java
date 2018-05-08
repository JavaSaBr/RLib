package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;

/**
 * The base implementation of the {@link Array}.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public abstract class AbstractArray<E> implements Array<E> {

    private static final long serialVersionUID = 2113052245369887690L;

    /**
     * The size of big array.
     */
    protected static final int SIZE_BIG_ARRAY = 10;

    /**
     * The default size of new array.
     */
    protected static final int DEFAULT_SIZE = 10;

    /**
     * Instantiates a new Abstract array.
     *
     * @param type the type
     */
    public AbstractArray(final Class<E> type) {
        this(type, DEFAULT_SIZE);
    }

    /**
     * Instantiates a new Abstract array.
     *
     * @param type the type
     * @param size the size
     */
    public AbstractArray(final Class<E> type, final int size) {
        super();

        if (size < 0) {
            throw new IllegalArgumentException("negative size");
        }

        setArray(ArrayUtils.create(type, size));
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            ArrayUtils.clear(array());
            setSize(0);
        }
    }

    @Override
    public final void free() {
        clear();
    }

    @Override
    public AbstractArray<E> clone() throws CloneNotSupportedException {
        return ClassUtils.unsafeCast(super.clone());
    }

    /**
     * Sets array.
     *
     * @param array the new array.
     */
    protected abstract void setArray(@NotNull E[] array);

    /**
     * Sets size.
     *
     * @param size the new size of the array.
     */
    protected abstract void setSize(final int size);

    @Override
    public boolean fastRemove(@NotNull final Object object) {
        final int index = indexOf(object);
        if (index >= 0) fastRemove(index);
        return index >= 0;
    }

    @Override
    public final boolean slowRemove(@NotNull final Object object) {
        final int index = indexOf(object);
        if (index >= 0) slowRemove(index);
        return index >= 0;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " size = " + size() + " :\n " + ArrayUtils.toString(this);
    }
}
