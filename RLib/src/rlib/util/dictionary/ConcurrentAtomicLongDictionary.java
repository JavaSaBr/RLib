package rlib.util.dictionary;

import rlib.concurrent.lock.AsyncReadSyncWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкурентного словая использующего примитивный ключ long, используя механизм
 * синхронизации через атомарные счетчики.
 *
 * @author Ronn
 */
public class ConcurrentAtomicLongDictionary<V> extends ConcurrentLockLongDictionary<V> {

    protected ConcurrentAtomicLongDictionary() {
        this(Dictionary.DEFAULT_LOAD_FACTOR, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentAtomicLongDictionary(final float loadFactor) {
        this(loadFactor, Dictionary.DEFAULT_INITIAL_CAPACITY);
    }

    protected ConcurrentAtomicLongDictionary(final float loadFactor, final int initCapacity) {
        super(loadFactor, initCapacity);
    }

    protected ConcurrentAtomicLongDictionary(final int initCapacity) {
        this(Dictionary.DEFAULT_LOAD_FACTOR, initCapacity);
    }

    @Override
    protected AsyncReadSyncWriteLock createLocker() {
        return LockFactory.newPrimitiveAtomicARSWLock();
    }
}
