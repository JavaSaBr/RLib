package rlib.util.array.impl;

import static java.lang.Math.max;
import static rlib.util.ArrayUtils.copyOf;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayIterator;
import rlib.util.array.ConcurrentArray;
import rlib.util.array.UnsafeArray;

/**
 * The base concurrent implementation of the array.
 *
 * @author JavaSaBr
 */
public abstract class AbstractConcurrentArray<E> extends AbstractArray<E> implements ConcurrentArray<E>, UnsafeArray<E> {

    private static final long serialVersionUID = -6291504312637658721L;

    /**
     * The count of elements in this array.
     */
    private final AtomicInteger size;

    /**
     * The unsafe array.
     */
    private volatile E[] array;

    public AbstractConcurrentArray(final Class<E> type) {
        this(type, 10);
    }

    public AbstractConcurrentArray(final Class<E> type, final int size) {
        super(type, size);

        this.size = new AtomicInteger();
    }

    @NotNull
    @Override
    public AbstractConcurrentArray<E> add(@NotNull final E element) {

        if (size() == array.length) {
            array = ArrayUtils.copyOf(array, Math.max(array.length >> 1, 1));
        }

        array[size.getAndIncrement()] = element;
        return this;
    }

    @NotNull
    @Override
    public final AbstractConcurrentArray<E> addAll(@NotNull final Array<? extends E> elements) {
        if (elements.isEmpty()) return this;

        final int current = array.length;
        final int selfSize = size();
        final int targetSize = elements.size();
        final int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = copyOf(array, max(current >> 1, diff));
        }

        processAdd(elements, selfSize, targetSize);
        return this;
    }

    @NotNull
    @Override
    public final AbstractConcurrentArray<E> addAll(@NotNull final Collection<? extends E> collection) {
        if (collection.isEmpty()) return this;

        final int current = array.length;
        final int diff = size() + collection.size() - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        for (final E element : collection) unsafeAdd(element);
        return this;
    }

    @NotNull
    @Override
    public final Array<E> addAll(@NotNull final E[] elements) {
        if (elements.length < 1) return this;

        final int current = array.length;
        final int selfSize = size();
        final int targetSize = elements.length;
        final int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = copyOf(array, max(current >> 1, diff));
        }

        processAdd(elements, selfSize, targetSize);
        return this;
    }

    @NotNull
    @Override
    public final E[] array() {
        return array;
    }

    @Override
    public void prepareForSize(final int size) {

        final int current = array.length;
        final int selfSize = size();
        final int diff = selfSize + size - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }
    }

    @NotNull
    @Override
    public final E fastRemove(final int index) {

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
    public final E get(final int index) {

        if (index < 0 || index >= size()) {
            throw new NoSuchElementException();
        }

        return array[index];
    }

    @Override
    public final ArrayIterator<E> iterator() {
        return new FinalArrayIterator<>(this);
    }

    @Override
    public final void set(final int index, @NotNull final E element) {

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
    protected final void setArray(final E[] array) {
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
    public final E slowRemove(final int index) {

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
    public final AbstractConcurrentArray<E> trimToSize() {

        final int size = size();
        if (size == array.length) return this;

        array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    @Override
    public Array<E> unsafeAdd(@NotNull E object) {
        array[size.getAndIncrement()] = object;
        return this;
    }

    @Override
    public E unsafeGet(final int index) {
        return array[index];
    }

    @Override
    public void unsafeSet(int index, @NotNull E element) {

        if (array[index] != null) {
            size.decrementAndGet();
        }

        array[index] = element;
        size.incrementAndGet();
    }

    protected void processAdd(final Array<? extends E> elements, final int selfSize, final int targetSize) {
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

    protected void processAdd(final E[] elements, final int selfSize, final int targetSize) {
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

    @Override
    public UnsafeArray<E> asUnsafe() {
        return this;
    }

    @NotNull
    @Override
    public AbstractConcurrentArray<E> clone() throws CloneNotSupportedException {
        final AbstractConcurrentArray<E> clone = (AbstractConcurrentArray<E>) super.clone();
        clone.array = ArrayUtils.copyOf(array, size());
        return clone;
    }
}