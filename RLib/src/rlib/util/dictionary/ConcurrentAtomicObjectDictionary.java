package rlib.util.dictionary;

import rlib.concurrent.lock.AsynReadSynWriteLock;
import rlib.concurrent.lock.LockFactory;

/**
 * Реализация конкуретного словаря с объектным ключем, где механизмом
 * синхронизации выступает атомарные счетчики.
 * 
 * @author Ronn
 */
public class ConcurrentAtomicObjectDictionary<K, V> extends ConcurrentLockObjectDictionary<K, V> {

	public ConcurrentAtomicObjectDictionary() {
		super();
	}

	public ConcurrentAtomicObjectDictionary(float loadFactor, int initCapacity) {
		super(loadFactor, initCapacity);
	}

	public ConcurrentAtomicObjectDictionary(float loadFactor) {
		super(loadFactor);
	}

	public ConcurrentAtomicObjectDictionary(int initCapacity) {
		super(initCapacity);
	}

	@Override
	protected AsynReadSynWriteLock createLocker() {
		return LockFactory.newPrimitiveAtomicARSWLock();
	}
}
