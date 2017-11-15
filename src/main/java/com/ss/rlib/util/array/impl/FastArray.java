package com.ss.rlib.util.array.impl;

import static com.ss.rlib.util.ArrayUtils.copyOf;
import static java.lang.Math.max;
import com.ss.rlib.util.ArrayUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayIterator;
import com.ss.rlib.util.array.UnsafeArray;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * The fast implementation of the array. This array is not threadsafe.
 *
 * @param <E> the type parameter
 * @author JavaSaBr
 */
public class FastArray<E> extends AbstractArray<E> implements UnsafeArray<E> {

    private static final long serialVersionUID = -8477384427415127978L;

    /**
     * The unsafe array.
     */
    protected E[] array;

    /**
     * The current size of this array.
     */
    protected int size;

    /**
     * Instantiates a new Fast array.
     *
     * @param type the type
     */
    public FastArray(@NotNull final Class<E> type) {
        super(type);
    }

    /**
     * Instantiates a new Fast array.
     *
     * @param type the type
     * @param size the size
     */
    public FastArray(@NotNull final Class<E> type, final int size) {
        super(type, size);
    }

    @Override
    public boolean add(@NotNull final E object) {

        if (size == array.length) {
            array = copyOf(array, max(array.length >> 1, 1));
        }

        return unsafeAdd(object);
    }

    @Override
    public final boolean addAll(@NotNull final Array<? extends E> elements) {
        if (elements.isEmpty()) return false;

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
    public boolean addAll(@NotNull final Collection<? extends E> collection) {
        if (collection.isEmpty()) return false;

        final int current = array.length;
        final int selfSize = size();
        final int targetSize = collection.size();
        final int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = copyOf(array, max(current >> 1, diff));
        }

        for (final E element : collection) unsafeAdd(element);
        return true;
    }

    @Override
    public final boolean addAll(@NotNull final E[] elements) {
        if (elements.length < 1) return false;

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

    @Override
    public void prepareForSize(final int size) {

        final int current = array.length;
        final int selfSize = size();
        final int diff = selfSize + size - current;

        if (diff > 0) {
            array = copyOf(array, max(current >> 1, diff));
        }
    }

    @NotNull
    @Override
    public final E fastRemove(final int index) {

        if (index < 0 || index >= size) {
            throw new NoSuchElementException();
        }

        size -= 1;

        final E old = array[index];

        array[index] = array[size];
        array[size] = null;

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

    @NotNull
    @Override
    public final ArrayIterator<E> iterator() {
        return new FinalArrayIterator<>(this);
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
            size = selfSize + targetSize;
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
            size = selfSize + targetSize;
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
    public final void set(final int index, @NotNull final E element) {

        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (array[index] != null) size -= 1;
        array[index] = element;

        size += 1;
    }

    @Override
    protected final void setArray(@NotNull final E[] array) {
        this.array = array;
    }

    @Override
    protected final void setSize(final int size) {
        this.size = size;
    }

    @Override
    public final int size() {
        return size;
    }

    @NotNull
    @Override
    public final E slowRemove(final int index) {

        if (index < 0 || index >= size) {
            throw new NoSuchElementException();
        }

        final int numMoved = size - index - 1;
        final E old = array[index];

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        size -= 1;
        array[size] = null;

        return old;
    }

    @Override
    public final FastArray<E> trimToSize() {
        if (size == array.length) return this;
        array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    @Override
    public boolean unsafeAdd(@NotNull final E object) {
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

    @NotNull
    @Override
    public UnsafeArray<E> asUnsafe() {
        return this;
    }

    @NotNull
    @Override
    public FastArray<E> clone() throws CloneNotSupportedException {
        final FastArray<E> clone = (FastArray<E>) super.clone();
        clone.array = ArrayUtils.copyOf(array, size());
        return clone;
    }
}