package rlib.util.dictionary;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкурентнго словаря с примитивным ключем int, где миханизм синхранизации выступает
 * {@link ReentrantReadWriteLock}.
 *
 * @author JavaSaBr
 */
public class ConcurrentReentrantReadWriteLockIntegerDictionary<V> extends AbstractIntegerDictionary<V> implements ConcurrentIntegerDictionary<V> {

    /**
     * Блокировщик.
     */
    protected final AsyncReadSyncWriteLock locker;

    /**
     * Кол-во элементов в словаре.
     */
    private final AtomicInteger size;

    /**
     * Таблица элементов.
     */
    private volatile IntegerEntry<V>[] content;

    /**
     * Следующий размер для метода изминения размера (capacity * load factor).
     */
    private volatile int threshold;

    protected ConcurrentReentrantReadWriteLockIntegerDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentReentrantReadWriteLockIntegerDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentReentrantReadWriteLockIntegerDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.threshold = (int) (initCapacity * loadFactor);
        this.content = new IntegerEntry[DEFAULT_INITIAL_CAPACITY];
        this.size = new AtomicInteger();
        this.locker = createLocker();
    }

    protected ConcurrentReentrantReadWriteLockIntegerDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    public void clear() {
        super.clear();
        size.set(0);
    }

    protected AsyncReadSyncWriteLock createLocker() {
        return LockFactory.newARSWLock();
    }

    @Override
    protected int decrementSizeAndGet() {
        return size.decrementAndGet();
    }

    @Override
    protected int incrementSizeAndGet() {
        return size.incrementAndGet();
    }

    @Override
    public long readLock() {
        locker.asyncLock();
        return 0;
    }

    @Override
    public void readUnlock(final long stamp) {
        locker.asyncUnlock();
    }

    @Override
    public final int size() {
        return size.get();
    }

    @Override
    public long writeLock() {
        locker.syncLock();
        return 0;
    }

    @Override
    public void writeUnlock(final long stamp) {
        locker.syncUnlock();
    }
}
