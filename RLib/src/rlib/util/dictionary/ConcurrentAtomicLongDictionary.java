package rlib.util.dictionary;

import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкурентного словая использующего примитивный ключ long, используя механизм
 * синхронизации через атомарные счетчики.
 *
 * @author JavaSaBr
 */
public class ConcurrentAtomicLongDictionary<V> extends ConcurrentReentrantReadWriteLockLongDictionary<V> {

    protected ConcurrentAtomicLongDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentAtomicLongDictionary(final float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentAtomicLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    protected ConcurrentAtomicLongDictionary(final int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    protected AsyncReadSyncWriteLock createLocker() {
        return LockFactory.newPrimitiveAtomicARSWLock();
    }
}
