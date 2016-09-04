package rlib.util.dictionary;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкуретного словаря с объектным ключем, используя механизмом синхронизации в виде
 * {@link ReentrantReadWriteLock}.
 *
 * @author JavaSaBr
 */
public class ConcurrentReentrantReadWriteLockObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> implements ConcurrentObjectDictionary<K, V> {

    /**
     * Блокировщики.
     */
    private final AsyncReadSyncWriteLock locker;

    /**
     * Кол-во элементов в таблице.
     */
    private final AtomicInteger size;

    protected ConcurrentReentrantReadWriteLockObjectDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentReentrantReadWriteLockObjectDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentReentrantReadWriteLockObjectDictionary(final float loadFactor, final int initCapacity) {
        this.size = new AtomicInteger();
        this.locker = createLocker();
    }

    protected ConcurrentReentrantReadWriteLockObjectDictionary(final int initCapacity) {
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
