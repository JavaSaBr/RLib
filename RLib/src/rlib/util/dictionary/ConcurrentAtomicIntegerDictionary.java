package rlib.util.dictionary;

import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкурентного словаря с примитивным ключем int, где миханизм
 * синхранизации выступают атомарные счетчики.
 *
 * @author Ronn
 */
public class ConcurrentAtomicIntegerDictionary<V> extends ConcurrentIntegerDictionary<V> {

	public ConcurrentAtomicIntegerDictionary() {
		super();
	}

	public ConcurrentAtomicIntegerDictionary(float loadFactor, int initCapacity) {
		super(loadFactor, initCapacity);
	}

	public ConcurrentAtomicIntegerDictionary(float loadFactor) {
		super(loadFactor);
	}

	public ConcurrentAtomicIntegerDictionary(int initCapacity) {
		super(initCapacity);
	}

	@Override
	protected AsynReadSynWriteLock createLocker() {
		return LockFactory.newPrimitiveAtomicARSWLock();
	}
}
