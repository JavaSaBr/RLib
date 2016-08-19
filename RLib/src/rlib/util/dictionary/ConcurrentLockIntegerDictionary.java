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
public class ConcurrentLockIntegerDictionary<V> extends AbstractIntegerDictionary<V> implements ConcurrentIntegerDictionary<V> {

    /**
     * Блокировщик.
     */
    private final AsyncReadSyncWriteLock locker;

    /**
     * Кол-во элементов в словаре.
     */
    private final AtomicInteger size;

    protected ConcurrentLockIntegerDictionary() {
        this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentLockIntegerDictionary(final float loadFactor) {
        this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentLockIntegerDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
        this.size = new AtomicInteger();
        this.locker = createLocker();
    }

    protected ConcurrentLockIntegerDictionary(final int initCapacity) {
        this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
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
