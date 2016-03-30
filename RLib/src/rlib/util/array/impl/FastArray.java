package rlib.util.array.impl;

import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayIterator;

/**
 * Реализация не потокобезопасного динамического массива для работы с локальными данными.
 *
 * @author Ronn
 */
public class FastArray<E> extends AbstractArray<E> {

    private static final long serialVersionUID = -8477384427415127978L;

    /**
     * Размер массива, который считается большим.
     */
    protected static final int SIZE_BIG_ARRAY = 10;

    /**
     * Массив элементов.
     */
    protected E[] array;

    /**
     * Кол-во элементов в колекции.
     */
    protected int size;

    /**
     * @param type тип элементов в массиве.
     */
    public FastArray(final Class<E> type) {
        super(type);
    }

    /**
     * @param type тип элементов в массиве.
     * @param size размер массива.
     */
    public FastArray(final Class<E> type, final int size) {
        super(type, size);
    }

    @Override
    public FastArray<E> add(final E object) {

        if (size == array.length) {
            array = ArrayUtils.copyOf(array, Math.max(array.length >> 1, 1));
        }

        return unsafeAdd(object);
    }

    @Override
    public final FastArray<E> addAll(final Array<? extends E> elements) {

        if (elements.isEmpty()) {
            return this;
        }

        final int current = array.length;
        final int selfSize = size();
        final int targetSize = elements.size();
        final int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        processAdd(elements, selfSize, targetSize);

        return this;
    }

    @Override
    public final Array<E> addAll(final E[] elements) {

        if (elements == null || elements.length < 1) {
            return this;
        }

        final int current = array.length;
        final int selfSize = size();
        final int targetSize = elements.length;
        final int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        processAdd(elements, selfSize, targetSize);

        return this;
    }

    @Override
    public final E[] array() {
        return array;
    }

    @Override
    public void checkSize(final int size) {

        final int current = array.length;
        final int selfSize = size();
        final int diff = selfSize + size - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }
    }

    @Override
    public final E fastRemove(final int index) {

        final E[] array = array();

        if (index < 0 || size < 1 || index >= size) {
            return null;
        }

        size -= 1;

        final E old = array[index];

        array[index] = array[size];
        array[size] = null;

        return old;
    }

    @Override
    public final E get(final int index) {
        return array[index];
    }

    @Override
    public final ArrayIterator<E> iterator() {
        return new ArrayIteratorImpl<>(this);
    }

    protected void processAdd(final Array<? extends E> elements, final int selfSize, final int targetSize) {

        // если надо срау большой массив добавить, то лучше черзе нативный метод
        if (targetSize > SIZE_BIG_ARRAY) {
            System.arraycopy(elements.array(), 0, array, selfSize, targetSize);
            size = selfSize + targetSize;
        } else {

            // если добавляемый массив небольшой, можно и обычным способом
            // внести
            for (final E element : elements.array()) {

                if (element == null) {
                    break;
                }

                unsafeAdd(element);
            }
        }
    }

    protected void processAdd(final E[] elements, final int selfSize, final int targetSize) {

        // если надо срау большой массив добавить, то лучше черзе нативный метод
        if (targetSize > SIZE_BIG_ARRAY) {
            System.arraycopy(elements, 0, array, selfSize, targetSize);
            size = selfSize + targetSize;
        } else {

            // если добавляемый массив небольшой, можно и обычным способом
            // внести
            for (final E element : elements) {

                if (element == null) {
                    break;
                }

                unsafeAdd(element);
            }
        }
    }

    @Override
    public final void set(final int index, final E element) {

        if (index < 0 || index >= size || element == null) {
            return;
        }

        final E[] array = array();

        if (array[index] != null) {
            size -= 1;
        }

        array[index] = element;

        size += 1;
    }

    @Override
    protected final void setArray(final E[] array) {
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

    @Override
    public final E slowRemove(final int index) {

        if (index < 0 || size < 1) {
            return null;
        }

        final E[] array = array();

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

        if (size == array.length) {
            return this;
        }

        array = ArrayUtils.copyOfRange(array, 0, size);

        return this;
    }

    @Override
    public FastArray<E> unsafeAdd(final E object) {
        array[size++] = object;
        return this;
    }
}