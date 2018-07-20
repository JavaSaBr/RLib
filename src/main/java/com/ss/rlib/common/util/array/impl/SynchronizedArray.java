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
 * @param <E> the type parameter
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

    /**
     * Instantiates a new Synchronized array.
     *
     * @param type the type
     */
    public SynchronizedArray(@NotNull final Class<? super E> type) {
        this(type, 10);
    }

    /**
     * Instantiates a new Synchronized array.
     *
     * @param type the type
     * @param size the size
     */
    public SynchronizedArray(@NotNull final Class<? super E> type, final int size) {
        super(type, size);

        this.size = new AtomicInteger();
    }

    @Override
    public synchronized boolean add(@NotNull final E element) {

        if (size() == array.length) {
            array = ArrayUtils.copyOf(array, array.length >> 1);
        }

        array[size.getAndIncrement()] = element;
        return true;
    }

    @Override
    public synchronized final boolean addAll(@NotNull final Array<? extends E> elements) {
        if (elements.isEmpty()) return true;

        final int current = array.length;
        final int selfSize = size();
        final int targetSize = elements.size();
        final int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = copyOf(array, max(current >> 1, diff));
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
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
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
            array = copyOf(array, max(current >> 1, diff));
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

    @NotNull
    @Override
    public synchronized final ArrayIterator<E> iterator() {
        return new DefaultArrayIterator<>(this);
    }

    @Override
    public synchronized final void set(final int index, @NotNull final E element) {

        if (index < 0 || index >= size()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (array[index] != null) {
            size.decrementAndGet();
        }

        array[index] = element;
        size.incrementAndGet();
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

    @NotNull
    @Override
    public synchronized final E slowRemove(final int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        final int length = size();
        final int numMoved = length - index - 1;

        final E old = array[index];

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        array[size.decrementAndGet()] = null;

        return old;
    }

    @Override
    public final SynchronizedArray<E> trimToSize() {

        final int size = size();
        if (size == array.length) return this;

        array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    /**
     * Process add.
     *
     * @param elements   the elements
     * @param selfSize   the self size
     * @param targetSize the target size
     */
    protected void processAdd(@NotNull final Array<? extends E> elements, final int selfSize, final int targetSize) {
        // если надо срау большой массив добавить, то лучше черзе нативный метод
        if (targetSize > SIZE_BIG_ARRAY) {
            System.arraycopy(elements.array(), 0, array, selfSize, targetSize);
            size.set(selfSize + targetSize);
        } else {
            // если добавляемый массив небольшой, можно и обычным способом
            // внести
            for (final E element : elements.array()) {
                if (element == null) break;
                unsafeAdd(element);
            }
        }
    }

    /**
     * Process add.
     *
     * @param elements   the elements
     * @param selfSize   the self size
     * @param targetSize the target size
     */
    protected void processAdd(@NotNull final E[] elements, final int selfSize, final int targetSize) {
        // если надо срау большой массив добавить, то лучше черзе нативный метод
        if (targetSize > SIZE_BIG_ARRAY) {
            System.arraycopy(elements, 0, array, selfSize, targetSize);
            size.set(selfSize + targetSize);
        } else {
            // если добавляемый массив небольшой, можно и обычным способом
            // внести
            for (final E element : elements) {
                if (element == null) break;
                unsafeAdd(element);
            }
        }
    }
}