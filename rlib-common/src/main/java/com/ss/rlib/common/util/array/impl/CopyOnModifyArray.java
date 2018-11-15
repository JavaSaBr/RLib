package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.atomic.AtomicReference;
import com.ss.rlib.common.function.TriplePredicate;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayComparator;
import com.ss.rlib.common.util.array.ArrayIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * The implementation of the array which create a new back-end array for each modification.
 * Thread-safe.
 *
 * @param <E> the element's type.
 * @author JavaSaBr
 */
public class CopyOnModifyArray<E> extends AbstractArray<E> {

    private static final long serialVersionUID = -8477384427415127978L;

    /**
     * The unsafe array.
     */
    protected volatile AtomicReference<E[]> array;

    public CopyOnModifyArray(@NotNull Class<? super E> type, int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull E object) {

        E[] current = array.get();
        E[] newArray = ArrayUtils.copyOf(current, 1);
        newArray[current.length] = object;

        if (!array.compareAndSet(current, newArray)) {
            return add(object);
        }

        return true;
    }

    @Override
    public boolean addAll(@NotNull Array<? extends E> elements) {

        if (elements.isEmpty()) {
            return false;
        }

        E[] current = array.get();
        E[] newArray = ArrayUtils.copyOf(current, elements.size());

        for (int i = current.length, g = 0; i < newArray.length; i++, g++) {
            newArray[i] = elements.get(g);
        }

        if (!array.compareAndSet(current, newArray)) {
            return addAll(elements);
        }

        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> collection) {

        if (collection.isEmpty()) {
            return false;
        }

        E[] current = array.get();
        E[] newArray = ArrayUtils.copyOf(current, collection.size());

        Iterator<? extends E> iterator = collection.iterator();

        for (int i = current.length; i < newArray.length; i++) {
            newArray[i] = iterator.next();
        }

        if (!array.compareAndSet(current, newArray)) {
            return addAll(collection);
        }

        return true;
    }

    @Override
    public boolean addAll(@NotNull E[] elements) {

        if (elements.length < 1) {
            return false;
        }

        E[] current = array.get();
        E[] newArray = ArrayUtils.combine(current, elements);

        if (!array.compareAndSet(current, newArray)) {
            return addAll(elements);
        }

        return true;
    }

    @Override
    public final @NotNull E[] array() {
        return array.get();
    }

    @Override
    public @NotNull E fastRemove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final @NotNull E get(int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        return array.get()[index];
    }

    @Override
    public final @NotNull ArrayIterator<E> iterator() {
        return new DefaultArrayIterator<>(this);
    }

    @Override
    public void set(int index, @NotNull E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void setArray(@NotNull E[] array) {

        if (this.array == null) {
            this.array = new AtomicReference<>();
        }

        this.array.set(array);
    }

    @Override
    protected final void setSize(int size) {
    }

    @Override
    public final int size() {
        return array().length;
    }

    @Override
    public final @NotNull E slowRemove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Array<E> sort(@NotNull ArrayComparator<E> comparator) {

        E[] current = array();
        E[] newArray = ArrayUtils.copyOf(current, 0);

        ArrayUtils.sort(newArray, 0, newArray.length, comparator);

        if (!array.compareAndSet(current, newArray)) {
            return sort(comparator);
        }

        return this;
    }

    @Override
    public @Nullable E findAny(@NotNull Predicate<E> predicate) {

        for (E element : array()) {
            if (predicate.test(element)) {
                return element;
            }
        }

        return null;
    }

    @Override
    public <T> @Nullable E findAny(@Nullable T argument, @NotNull BiPredicate<? super E, T> predicate) {

        for (E element : array()) {
            if (predicate.test(element, argument)) {
                return element;
            }
        }

        return null;
    }

    @Override
    public <T> @Nullable E findAnyR(@Nullable T argument, @NotNull BiPredicate<T, ? super E> condition) {

        for (E element : array()) {
            if (condition.test(argument, element)) {
                return element;
            }
        }

        return null;
    }

    @Override
    public <F, S> @Nullable E findAny(
            @Nullable F first,
            @Nullable S second,
            @NotNull TriplePredicate<E, F, S> predicate
    ) {

        for (E element : array()) {
            if (predicate.test(element, first, second)) {
                return element;
            }
        }

        return null;
    }

    @Override
    public @NotNull CopyOnModifyArray<E> clone() throws CloneNotSupportedException {
        CopyOnModifyArray<E> clone = (CopyOnModifyArray<E>) super.clone();
        clone.array = new AtomicReference<>(ArrayUtils.copyOf(array(), size()));
        return clone;
    }
}