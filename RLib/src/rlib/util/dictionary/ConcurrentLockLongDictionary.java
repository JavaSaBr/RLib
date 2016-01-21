package rlib.util.dictionary;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Реализация конкурентного словая использующего примитивный ключ long,
 * используя механизм синхронизации через {@link ReentrantLock}.
 *
 * @author Ronn
 */
public class ConcurrentLockLongDictionary<V> extends AbstractLongDictionary<V> implements ConcurrentLongDictionary<V> {

    /**
     * Блокировщик.
     */
    private final AsyncReadSyncWriteLock locker;

    /**
     * Кол-во элементов в таблице.
     */
    private final AtomicInteger size;

    protected ConcurrentLockLongDictionary() {
        this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentLockLongDictionary(final float loadFactor) {
        this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentLockLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.size = new AtomicInteger();
        this.locker = createLocker();
    }

    protected ConcurrentLockLongDictionary(final int initCapacity) {
        this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
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
    public void readLock() {
        locker.asyncLock();
    }

    @Override
    public void readUnlock() {
        locker.asyncUnlock();
    }

    @Override
    public final int size() {
        return size.get();
    }

    @Override
    public void writeLock() {
        locker.syncLock();
    }

    @Override
    public void writeUnlock() {
        locker.syncUnlock();
    }
}
