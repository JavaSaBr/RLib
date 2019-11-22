package com.ss.rlib.common.util.array.impl;

import com.ss.rlib.common.concurrent.atomic.AtomicReference;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayComparator;
import com.ss.rlib.common.util.array.ArrayFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The implementation of the array which create a new back-end array for each modification.
 * Thread-safe.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
public class CopyOnModifyArray<E> extends AbstractArray<E> {

    private static final long serialVersionUID = -8477384427415127978L;

    @SuppressWarnings("NullableProblems")
    protected volatile @NotNull AtomicReference<E[]> array;

    public CopyOnModifyArray(@NotNull Class<? super E> type, int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull E object) {

        var current = array.get();
        var newArray = ArrayUtils.copyOfAndExtend(current, 1);
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

        var current = array.get();
        var newArray = ArrayUtils.copyOfAndExtend(current, elements.size());

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

        var current = array.get();
        var newArray = ArrayUtils.copyOfAndExtend(current, collection.size());

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

        var current = array.get();
        var newArray = ArrayUtils.combine(current, elements);

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
    public @NotNull E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Array<?> target) {

        if (target.isEmpty()) {
            return false;
        }

        var current = array();
        var modifiable = ArrayFactory.asArray(current);

        if(!modifiable.removeAll(target)) {
            return false;
        }

        var newArray = modifiable.toArray();

        if (!array.compareAndSet(current, newArray)) {
            return removeAll(target);
        }

        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> target) {

        if (target.isEmpty()) {
            return false;
        }

        var current = array();
        var modifiable = ArrayFactory.asArray(current);

        if(!modifiable.removeAll(target)) {
            return false;
        }

        var newArray = modifiable.toArray();

        if (!array.compareAndSet(current, newArray)) {
            return removeAll(target);
        }

        return true;
    }

    @Override
    public boolean remove(@NotNull Object object) {

        var current = array.get();
        var index = ArrayUtils.indexOf(current, object);

        if (index == -1) {
            return false;
        }

        var newArray = ArrayUtils.create(current, current.length - 1);

        for (int i = 0, j = 0; i < current.length; i++, j++) {

            if (i == index) {
                i++;
                if (i >= current.length) {
                    break;
                }
            }

            newArray[j] = current[i];
        }

        if (!array.compareAndSet(current, newArray)) {
            return fastRemove(object);
        }

        return true;
    }

    @Override
    public boolean fastRemove(@NotNull Object object) {
        return remove(object);
    }

    @Override
    public @NotNull E fastRemove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {

        var current = array.get();

        if (current.length < 1) {
            return;
        }

        var newArray = ArrayUtils.create(current, 0);

        if (!array.compareAndSet(current, newArray)) {
            clear();
        }
    }

    @Override
    public final @NotNull E get(int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        return array.get()[index];
    }

    @Override
    public void replace(int index, @NotNull E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void setArray(E @NotNull [] array) {

        //noinspection ConstantConditions
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
    public @NotNull Array<E> sort(@NotNull ArrayComparator<E> comparator) {

        var current = array();
        var newArray = ArrayUtils.copyOfAndExtend(current, 0);

        ArrayUtils.sort(newArray, 0, newArray.length, comparator);

        if (!array.compareAndSet(current, newArray)) {
            return sort(comparator);
        }

        return this;
    }

    @Override
    public @NotNull CopyOnModifyArray<E> clone() throws CloneNotSupportedException {
        var clone = (CopyOnModifyArray<E>) super.clone();
        clone.array = new AtomicReference<>(ArrayUtils.copyOf(array()));
        return clone;
    }
}
