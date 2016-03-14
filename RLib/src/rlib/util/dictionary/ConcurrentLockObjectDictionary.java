package rlib.util.dictionary;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкуретного словаря с объектным ключем, используя механизмом синхронизации в виде
 * {@link ReentrantReadWriteLock}.
 *
 * @author Ronn
 */
public class ConcurrentLockObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> implements ConcurrentObjectDictionary<K, V> {

    /**
     * Блокировщики.
     */
    private final AsyncReadSyncWriteLock locker;

    /**
     * Кол-во элементов в таблице.
     */
    private final AtomicInteger size;

    protected ConcurrentLockObjectDictionary() {
        this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentLockObjectDictionary(final float loadFactor) {
        this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentLockObjectDictionary(final float loadFactor, final int initCapacity) {
        this.size = new AtomicInteger();
        this.locker = createLocker();
    }

    protected ConcurrentLockObjectDictionary(final int initCapacity) {
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
    public final void readLock() {
        locker.asyncLock();
    }

    @Override
    public final void readUnlock() {
        locker.asyncUnlock();
    }

    @Override
    public final int size() {
        return size.get();
    }

    @Override
    public final void writeLock() {
        locker.syncLock();
    }

    @Override
    public final void writeUnlock() {
        locker.syncUnlock();
    }
}
