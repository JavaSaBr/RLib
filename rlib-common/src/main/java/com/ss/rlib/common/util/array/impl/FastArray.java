package com.ss.rlib.common.util.array.impl;

import static com.ss.rlib.common.util.ArrayUtils.copyOf;
import static java.lang.Math.max;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayIterator;
import com.ss.rlib.common.util.array.UnsafeArray;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The fast implementation of the array. This array is not threadsafe.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
public class FastArray<E> extends AbstractArray<E> implements UnsafeArray<E> {

    private static final long serialVersionUID = -8477384427415127978L;

    /**
     * The unsafe array.
     */
    @SuppressWarnings("NullableProblems")
    protected E @NotNull [] array;

    /**
     * The current size of this array.
     */
    protected int size;

    public FastArray(@NotNull Class<? super E> type) {
        super(type);
    }

    public FastArray(@NotNull Class<? super E> type, final int size) {
        super(type, size);
    }

    public FastArray(@NotNull E[] array) {
        super(array);
    }

    @Override
    public boolean add(@NotNull E object) {

        if (size == array.length) {
            array = ArrayUtils.copyOfAndExtend(array, max(array.length >> 1, 1));
        }

        return unsafeAdd(object);
    }

    @Override
    public boolean addAll(@NotNull Array<? extends E> elements) {

        if (elements.isEmpty()) {
            return false;
        }

        int current = array.length;
        int selfSize = size();
        int targetSize = elements.size();
        int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, max(current >> 1, diff));
        }

        processAdd(elements, selfSize, targetSize);
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> collection) {

        if (collection.isEmpty()) {
            return false;
        }

        int current = array.length;
        int selfSize = size();
        int targetSize = collection.size();
        int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, max(current >> 1, diff));
        }

        for (E element : collection) {
            unsafeAdd(element);
        }

        return true;
    }

    @Override
    public boolean addAll(@NotNull E[] elements) {

        if (elements.length < 1) {
            return false;
        }

        int current = array.length;
        int selfSize = size();
        int targetSize = elements.length;
        int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, max(current >> 1, diff));
        }

        processAdd(elements, selfSize, targetSize);
        return true;
    }

    @Override
    public final @NotNull E[] array() {
        return array;
    }

    @Override
    public void prepareForSize(int size) {

        int current = array.length;
        int selfSize = size();
        int diff = selfSize + size - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, max(current >> 1, diff));
        }
    }

    @Override
    public @NotNull E fastRemove(int index) {

        if (index < 0 || index >= size) {
            throw new NoSuchElementException();
        }

        size -= 1;

        E old = array[index];

        array[index] = array[size];
        array[size] = null;

        return old;
    }

    @Override
    public final @NotNull E get(int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        return array[index];
    }

    @Override
    public final @NotNull ArrayIterator<E> iterator() {
        return new DefaultArrayIterator<>(this);
    }

    /**
     * The process of adding the array.
     *
     * @param elements   the elements.
     * @param selfSize   the self size.
     * @param targetSize the target size.
     */
    protected void processAdd(@NotNull Array<? extends E> elements, int selfSize, int targetSize) {
        System.arraycopy(elements.array(), 0, array, selfSize, targetSize);
        size = selfSize + targetSize;
    }

    /**
     * The process of adding the array.
     *
     * @param elements   the elements.
     * @param selfSize   the self size.
     * @param targetSize the target size.
     */
    protected void processAdd(@NotNull E[] elements, int selfSize, int targetSize) {
        System.arraycopy(elements, 0, array, selfSize, targetSize);
        size = selfSize + targetSize;
    }

    @Override
    public void replace(int index, @NotNull E element) {

        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }

        array[index] = element;
    }

    @Override
    protected final void setArray(E @NotNull [] array) {
        this.array = array;
    }

    @Override
    protected final void setSize(int size) {
        this.size = size;
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public @NotNull E remove(int index) {

        if (index < 0 || index >= size) {
            throw new NoSuchElementException();
        }

        int numMoved = size - index - 1;
        E old = array[index];

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        size -= 1;
        array[size] = null;

        return old;
    }

    @Override
    public @NotNull FastArray<E> trimToSize() {

        if (size == array.length) {
            return this;
        }

        array = ArrayUtils.copyOfRange(array, 0, size);

        return this;
    }

    @Override
    public boolean unsafeAdd(@NotNull E object) {
        array[size++] = object;
        return true;
    }

    @Override
    public void unsafeSet(int index, @NotNull E element) {
        if (array[index] != null) size -= 1;
        array[index] = element;
        size += 1;
    }

    @Override
    public E unsafeGet(int index) {
        return array[index];
    }

    @Override
    public @NotNull UnsafeArray<E> asUnsafe() {
        return this;
    }

    @Override
    public @NotNull FastArray<E> clone() throws CloneNotSupportedException {
        var clone = (FastArray<E>) super.clone();
        clone.array = ArrayUtils.copyOfAndExtend(array, size());
        return clone;
    }
}
