package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayComparator;
import com.ss.rlib.common.util.array.ReadOnlyArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The read only version of the {@link FastArray}.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
public final class ReadOnlyFastArray<E> extends FastArray<E> implements ReadOnlyArray<E> {

    public ReadOnlyFastArray(@NotNull E[] array) {
        super(array);
    }

    @Override
    public void apply(@NotNull Function<? super E, ? extends E> function) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean add(@NotNull E object) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> collection) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean fastRemove(@NotNull Object object) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public @NotNull E remove(int index) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean addAll(@NotNull Array<? extends E> elements) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean addAll(@NotNull E[] elements) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public @NotNull E fastRemove(int index) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean unsafeAdd(@NotNull E object) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public E unsafeGet(int index) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public void unsafeSet(int index, @NotNull E element) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public void replace(int index, @NotNull E element) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public void clear() {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public @NotNull Array<E> sort(@NotNull ArrayComparator<E> comparator) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean remove(@Nullable Object object) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean removeAll(@NotNull Array<?> target) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> target) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean retainAll(@NotNull Array<?> target) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> target) {
        throw new IllegalStateException("This array is read only.");
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super E> filter) {
        throw new IllegalStateException("This array is read only.");
    }
}
