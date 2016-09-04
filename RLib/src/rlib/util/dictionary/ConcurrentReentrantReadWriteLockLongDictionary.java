package rlib.util.dictionary;

import java.util.concurrent.locks.ReentrantLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкурентного словая использующего примитивный ключ long, используя механизм
 * синхронизации через {@link ReentrantLock}.
 *
 * @author JavaSaBr
 */
public class ConcurrentReentrantReadWriteLockLongDictionary<V> extends AbstractLongDictionary<V> implements ConcurrentLongDictionary<V> {

    /**
     * Блокировщик.
     */
    private final AsyncReadSyncWriteLock locker;

    /**
     * Кол-во элементов в таблице.
     */
    private final AtomicInteger size;

    protected ConcurrentReentrantReadWriteLockLongDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentReentrantReadWriteLockLongDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentReentrantReadWriteLockLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.size = new AtomicInteger();
        this.locker = createLocker();
    }

    protected ConcurrentReentrantReadWriteLockLongDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    public final void clear() {
        super.clear();
        size.getAndSet(0);
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
