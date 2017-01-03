package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import rlib.util.ArrayUtils;
import rlib.util.ClassUtils;
import rlib.util.array.Array;

/**
 * The base implementation of the {@link Array}.
 *
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

    public AbstractArray(final Class<E> type) {
        this(type, DEFAULT_SIZE);
    }

    public AbstractArray(final Class<E> type, final int size) {
        super();

        if (size < 0) {
            throw new IllegalArgumentException("negative size");
        }

        setArray(ArrayUtils.create(type, size));
    }

    @NotNull
    @Override
    public Array<E> clear() {

        if (!isEmpty()) {
            ArrayUtils.clear(array());
            setSize(0);
        }

        return this;
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
     * @param array the new array.
     */
    protected abstract void setArray(E[] array);

    /**
     * @param size the new size of the array.
     */
    protected abstract void setSize(int size);

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
