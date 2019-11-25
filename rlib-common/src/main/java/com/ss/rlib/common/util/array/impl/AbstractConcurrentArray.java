package com.ss.rlib.common.util.array.impl;

import static com.ss.rlib.common.util.ArrayUtils.copyOf;
import static java.lang.Math.max;
import com.ss.rlib.common.concurrent.atomic.AtomicInteger;
import com.ss.rlib.common.util.ArrayUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayIterator;
import com.ss.rlib.common.util.array.ConcurrentArray;
import com.ss.rlib.common.util.array.UnsafeArray;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The base concurrent implementation of dynamic arrays.
 *
 * @param <E> the array's element type.
 * @author JavaSaBr
 */
@SuppressWarnings("NonAtomicOperationOnVolatileField")
public abstract class AbstractConcurrentArray<E> extends AbstractArray<E> implements ConcurrentArray<E>, UnsafeArray<E> {

    private static final long serialVersionUID = -6291504312637658721L;

    /**
     * The count of elements in this array.
     */
    private final AtomicInteger size;

    /**
     * The unsafe array.
     */
    @SuppressWarnings("NullableProblems")
    private volatile E @NotNull [] array;

    public AbstractConcurrentArray(@NotNull Class<? super E> type) {
        this(type, 10);
    }

    public AbstractConcurrentArray(@NotNull Class<? super E> type, int size) {
        super(type, size);
        this.size = new AtomicInteger();
    }

    @Override
    public boolean add(@NotNull E element) {

        if (size() == array.length) {
            array = ArrayUtils.copyOfAndExtend(array, Math.max(array.length >> 1, 1));
        }

        array[size.getAndIncrement()] = element;
        return true;
    }

    @Override
    public final boolean addAll(@NotNull Array<? extends E> elements) {

        if (elements.isEmpty()) {
            return false;
        }

        var current = array.length;
        var selfSize = size();
        var targetSize = elements.size();
        var diff = selfSize + targetSize - current;

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

        var current = array.length;
        var diff = size() + collection.size() - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, Math.max(current >> 1, diff));
        }

        for (var element : collection) {
            unsafeAdd(element);
        }

        return true;
    }

    @Override
    public final boolean addAll(@NotNull E[] elements) {

        if (elements.length < 1) {
            return false;
        }

        var current = array.length;
        var selfSize = size();
        var targetSize = elements.length;
        var diff = selfSize + targetSize - current;

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

        var current = array.length;
        var selfSize = size();
        var diff = selfSize + size - current;

        if (diff > 0) {
            array = ArrayUtils.copyOfAndExtend(array, Math.max(current >> 1, diff));
        }
    }

    @Override
    public final @NotNull E fastRemove(int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        var newSize = size.decrementAndGet();
        var old = array[index];

        array[index] = array[newSize];
        array[newSize] = null;

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

    @Override
    public void replace(int index, @NotNull E element) {

        if (index < 0 || index >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        array[index] = element;
    }

    @Override
    protected final void setArray(E @NotNull [] array) {
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
    public @NotNull E remove(int index) {

        if (index < 0 || index >= size()) {
            throw new ArrayIndexOutOfBoundsException();
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
    public final @NotNull AbstractConcurrentArray<E> trimToSize() {

        var size = size();

        if (size == array.length) {
            return this;
        }

        array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    @Override
    public boolean unsafeAdd(@NotNull E object) {
        array[size.getAndIncrement()] = object;
        return true;
    }

    @Override
    public @NotNull E unsafeGet(int index) {
        return array[index];
    }

    @Override
    public void unsafeSet(final int index, @NotNull final E element) {

        if (array[index] != null) {
            size.decrementAndGet();
        }

        array[index] = element;
        size.incrementAndGet();
    }

    /**
     * Process of adding the elements.
     *
     * @param elements   the elements
     * @param selfSize   the self size
     * @param targetSize the target size
     */
    protected void processAdd(@NotNull Array<? extends E> elements, int selfSize, int targetSize) {
        System.arraycopy(elements.array(), 0, array, selfSize, targetSize);
        size.set(selfSize + targetSize);
    }

    /**
     * Process of adding the elements.
     *
     * @param elements   the elements
     * @param selfSize   the self size
     * @param targetSize the target size
     */
    protected void processAdd(@NotNull E[] elements, int selfSize, int targetSize) {
        System.arraycopy(elements, 0, array, selfSize, targetSize);
        size.set(selfSize + targetSize);
    }

    @Override
    public @NotNull UnsafeArray<E> asUnsafe() {
        return this;
    }

    @Override
    public @NotNull AbstractConcurrentArray<E> clone() throws CloneNotSupportedException {
        var clone = (AbstractConcurrentArray<E>) super.clone();
        clone.array = ArrayUtils.copyOfAndExtend(array, size());
        return clone;
    }
}
