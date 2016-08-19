package rlib.util.array.impl;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.LockFactory;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayIterator;

/**
 * Реализация динамического массива с возможностью потокобезопасно асинхронно читать и синхронно
 * записывать. Поддерживается рекурсивный вызов readLock/writeLock. Используется реализация
 * блокировщика {@link ReentrantReadWriteLock}, так что отлично подходит и для мест где мало записей
 * и много чтений так и на оборот, единственный минус в нагрузке на GC, так как создает много
 * временных объектов при активном использовании. <p> Все операции по чтению/записи массива
 * производить в блоке <p>
 * <pre>
 * array.readLock()/writeLock();
 * try {
 * 	// handle
 * } finally {
 * 	array.readUnlock()/writeUnlock();
 * }
 * </pre>
 *
 * @author JavaSaBr
 */
public class ConcurrentArray<E> extends AbstractArray<E> {

    private static final long serialVersionUID = -7985171224116955303L;

    /**
     * Кол-во элементов в колекции.
     */
    private final AtomicInteger size;

    /**
     * Блокировщик на чтение.
     */
    private final Lock readLock;

    /**
     * Блокировщик на запись.
     */
    private final Lock writeLock;

    /**
     * Массив элементов.
     */
    private volatile E[] array;

    /**
     * @param type тип элементов в массиве.
     */
    public ConcurrentArray(final Class<E> type) {
        this(type, 10);
    }

    /**
     * @param type тип элементов в массиве.
     * @param size размер массива.
     */
    public ConcurrentArray(final Class<E> type, final int size) {
        super(type, size);

        final ReadWriteLock readWriteLock = LockFactory.newRWLock();

        this.size = new AtomicInteger();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    @NotNull
    @Override
    public ConcurrentArray<E> add(@NotNull final E element) {

        if (size() == array.length) {
            array = ArrayUtils.copyOf(array, Math.max(array.length >> 1, 1));
        }

        array[size.getAndIncrement()] = element;

        return this;
    }

    @NotNull
    @Override
    public final ConcurrentArray<E> addAll(@NotNull final Array<? extends E> elements) {

        if (elements == null || elements.isEmpty()) {
            return this;
        }

        final int current = array.length;
        final int diff = size() + elements.size() - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        for (final E element : elements.array()) {
            if (element == null) break;
            add(element);
        }

        return this;
    }

    @NotNull
    @Override
    public Array<E> addAll(@NotNull final Collection<? extends E> collection) {

        if (collection == null || collection.isEmpty()) {
            return this;
        }

        final int current = array.length;
        final int diff = size() + collection.size() - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        for (final E element : collection) {
            add(element);
        }

        return this;
    }

    @NotNull
    @Override
    public final Array<E> addAll(@NotNull final E[] elements) {

        if (elements == null || elements.length < 1) {
            return this;
        }

        final int current = array.length;
        final int diff = size() + elements.length - current;

        if (diff > 0) {
            array = ArrayUtils.copyOf(array, Math.max(current >> 1, diff));
        }

        for (final E element : elements) {
            add(element);
        }

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

    @Override
    public final E fastRemove(final int index) {
        if (index < 0) return null;

        final E[] array = array();

        int length = size();

        if (length < 1 || index >= length) {
            return null;
        }

        length = size.decrementAndGet();

        final E old = array[index];

        array[index] = array[length];
        array[length] = null;

        return old;
    }

    @NotNull
    @Override
    public final E get(final int index) {
        return array[index];
    }

    @Override
    public final ArrayIterator<E> iterator() {
        return new ArrayIteratorImpl<>(this);
    }

    @Override
    public final void readLock() {
        readLock.lock();
    }

    @Override
    public final void readUnlock() {
        readLock.unlock();
    }

    @Override
    public final void set(final int index, @NotNull final E element) {

        if (index < 0 || index >= size() || element == null) {
            return;
        }

        final E[] array = array();

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

    @Override
    public final E slowRemove(final int index) {

        final int length = size();

        if (index < 0 || length < 1) {
            return null;
        }

        final E[] array = array();

        final int numMoved = length - index - 1;

        final E old = array[index];

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        size.decrementAndGet();

        array[size.get()] = null;

        return old;
    }

    @Override
    public final ConcurrentArray<E> trimToSize() {

        final int size = size();

        if (size == array.length) {
            return this;
        }

        array = ArrayUtils.copyOfRange(array, 0, size);
        return this;
    }

    @Override
    public final void writeLock() {
        writeLock.lock();
    }

    @Override
    public final void writeUnlock() {
        writeLock.unlock();
    }
}