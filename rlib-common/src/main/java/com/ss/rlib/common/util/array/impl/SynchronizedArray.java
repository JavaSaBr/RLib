package com.ss.rlib.common.util.array.impl;

import static com.ss.rlib.common.util.ArrayUtils.copyOf;
import static java.lang.Math.max;
import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayIterator;
import com.ss.rlib.common.util.array.UnsafeArray;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The implementation of the array with synchronization all methods.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
public class SynchronizedArray<E> extends AbstractArray<E> implements UnsafeArray<E> {

    private static final long serialVersionUID = -7288153859732883548L;

    /**
     * The count of elements in this array.
     */
    private final AtomicInteger size;

    /**
     * The unsafe array.
     */
    private volatile E[] array;


    public SynchronizedArray(@NotNull Class<? super E> type) {
        this(type, 10);
    }

    public SynchronizedArray(@NotNull Class<? super E> type, int size) {
        super(type, size);
        this.size = new AtomicInteger();
    }

    @Override
    public synchronized boolean add(@NotNull E element) {

        if (size() == array.length) {
            array = ArrayUtils.copyOfAndExtend(array, array.length >> 1);
        }

        array[size.getAndIncrement()] = element;
        return true;
    }

    @Override
    public synchronized final boolean addAll(@NotNull Array<? extends E> elements) {

        if (elements.isEmpty()) {
            return true;
        }

        final int current = array.length;
        final int selfSize = size();
        final int targetSize = elements.size();
        final int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, max(current >> 1, diff));
        }

        processAdd(elements, selfSize, targetSize);
        return true;
    }

    @Override
    public synchronized boolean addAll(@NotNull final Collection<? extends E> collection) {
        if (collection.isEmpty()) return true;

        final int current = array.length;
        final int diff = size() + collection.size() - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, Math.max(current >> 1, diff));
        }

        for (final E element : collection) unsafeAdd(element);
        return true;
    }

    @Override
    public synchronized final boolean addAll(@NotNull final E[] elements) {

        final int current = array.length;
        final int selfSize = size();
        final int targetSize = elements.length;
        final int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, max(current >> 1, diff));
        }

        processAdd(elements, selfSize, targetSize);
        return true;
    }

    @NotNull
    @Override
    public final E[] array() {
        return array;
    }

    @NotNull
    @Override
    public synchronized final E fastRemove(final int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        final int newSize = size.decrementAndGet();
        final E old = array[index];

        array[index] = array[newSize];
        array[newSize] = null;

        return old;
    }

    @NotNull
    @Override
    public synchronized final E get(final int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        return array[index];
    }

    @Override
    public synchronized @NotNull ArrayIterator<E> iterator() {
        return new DefaultArrayIterator<>(this);
    }

    @Override
    public void replace(int index, @NotNull E element) {

        if (index < 0 || index >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        array[index] = element;
    }

    @Override
    protected final void setArray(@NotNull final E[] array) {
        this.array = array;
    }

    @Override
    protected final void setSize(final int size) {
        this.size.getAndSet(size);
    }

    @Override
    public final int size() {
        return size.get();
    }

    @Override
    public synchronized @NotNull E remove(int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        var length = size();
        var numMoved = length - index - 1;

        var old = array[index];

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        array[size.decrementAndGet()] = null;
        return old;
    }

    @Override
    public synchronized @NotNull SynchronizedArray<E> trimToSize() {

        var size = size();

        if (size == array.length) {
            return this;
        }

        array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    protected void processAdd(@NotNull final Array<? extends E> elements, final int selfSize, final int targetSize) {
        System.arraycopy(elements.array(), 0, array, selfSize, targetSize);
        size.set(selfSize + targetSize);
    }

    protected void processAdd(@NotNull final E[] elements, final int selfSize, final int targetSize) {
        System.arraycopy(elements, 0, array, selfSize, targetSize);
        size.set(selfSize + targetSize);
    }
}
