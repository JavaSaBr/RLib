package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

/**
 * The base implementation of dynamic arrays.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
public abstract class AbstractArray<E> implements Array<E> {

    private static final long serialVersionUID = 2113052245369887690L;

    /**
     * The default size of new backend array.
     */
    protected static final int DEFAULT_CAPACITY = 10;

    public AbstractArray(@NotNull Class<? super E> type) {
        this(type, DEFAULT_CAPACITY);
    }

    public AbstractArray(@NotNull Class<? super E> type, int capacity) {
        super();

        if (capacity < 0) {
            throw new IllegalArgumentException("Negative capacity");
        }

        setArray(ArrayUtils.create(type, capacity));
    }

    public AbstractArray(@NotNull E[] array) {
        super();
        setArray(array);
        setSize(array.length);
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            ArrayUtils.clear(array());
            setSize(0);
        }
    }

    @Override
    public void free() {
        clear();
    }

    @Override
    public @NotNull AbstractArray<E> clone() throws CloneNotSupportedException {
        return ClassUtils.unsafeNNCast(super.clone());
    }

    protected abstract void setArray(E @NotNull [] array);
    protected abstract void setSize(int size);

    @Override
    public String toString() {
        return getClass().getSimpleName() + " size = " + size() +
                " :\n " + ArrayUtils.toString(this);
    }

    @Override
    public @NotNull String toString(@NotNull Function<E, @NotNull String> toString) {
        return getClass().getSimpleName() + " size = " + size() +
                " :\n " + ArrayUtils.toString(this, toString);
    }

    @Override
    public boolean equals(@Nullable Object another) {

        if (this == another) {
            return true;
        } else if (!(another instanceof Array)) {
            return false;
        }

        var array = (Array<?>) another;
        return size() == array.size() && Arrays.equals(array(), 0, size(), array.array(), 0, array.size());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size());
        result = 31 * result + Arrays.hashCode(array());
        return result;
    }
}
