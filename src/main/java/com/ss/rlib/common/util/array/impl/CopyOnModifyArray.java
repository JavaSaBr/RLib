package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.atomic.AtomicReference;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayComparator;
import com.ss.rlib.common.util.array.ArrayIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    protected AtomicReference<E[]> array;

    public CopyOnModifyArray(@NotNull Class<E> type) {
        super(type);
    }

    public CopyOnModifyArray(@NotNull Class<E> type, final int size) {
        super(type, size);
    }

    public CopyOnModifyArray(@NotNull E[] array) {
        super(array);
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
    public @NotNull CopyOnModifyArray<E> clone() throws CloneNotSupportedException {
        CopyOnModifyArray<E> clone = (CopyOnModifyArray<E>) super.clone();
        clone.array = new AtomicReference<>(ArrayUtils.copyOf(array(), size()));
        return clone;
    }
}